package fr.snipertvmc.essentialsxgui.utilities.parsers.inventories;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryPropertyParser {



	// -------------------------------------------------- //


	public static boolean isTitleValid(Object title, String inventoryName) {
		if (isIgnored(inventoryName, "title")) return true;
		if (isMissing(title, inventoryName, "title", false)) return false;
		if (isNotTypeRequired(title, inventoryName, "title", String.class)) return false;
		return true;
	}


	public static boolean areRowsValid(Object rows, String inventoryName) {
		if (isIgnored(inventoryName, "title")) return true;
		if (isMissing(rows, inventoryName, "rows", false)) return false;
		if (isNotTypeRequired(rows, inventoryName, "rows", Number.class)) return false;
		int rowsValue = ((Number) rows).intValue();
		if (rowsValue < 1 || rowsValue > 6) {
			ConsoleLogger.error("Invalid rows in inventory '" + inventoryName + "': it must be between 1 and 6.");
			return false;
		}
		return true;
	}


	public static boolean isInventorySchemeValid(Object inventoryScheme, String inventoryName, Object rows) {
		if (isIgnored(inventoryName, "inventoryScheme")) return true;
		if (isNotTypeRequired(inventoryScheme, inventoryName, "inventoryScheme", List.class)) return false;
		if (((List<?>) inventoryScheme).stream().anyMatch(inventorySchemeLine -> !(inventorySchemeLine instanceof String))) {
			ConsoleLogger.error("Invalid inventory scheme for inventory '" + inventoryName + "': inventory scheme must contain strings.");
			return false;
		}
		List<String> schemeLines = (List<String>) inventoryScheme;
		int rowsValue = ((Number) rows).intValue();
		if (schemeLines.isEmpty() || schemeLines.size() != rowsValue) {
			ConsoleLogger.error("Invalid inventory scheme in inventory '" + inventoryName + "': it must contain " + rowsValue + " lines.");
			return false;
		}
		for (String schemeLine : schemeLines) {
			if (!schemeLine.matches("[ 1]{9}")) {
				ConsoleLogger.error("Invalid inventory scheme in inventory '" + inventoryName + "': it contains an invalid line '" + schemeLine + "'.");
				return false;
			}
		}
		return true;
	}


	// -------------------------------------------------- //


	private static boolean isIgnored(String inventoryName, String propertyName) {
		return ignoredPaths.containsKey(propertyName) && ignoredPaths.get(propertyName).contains(inventoryName);
	}


	private static boolean isMissing(Object value, String inventoryName, String propertyName, boolean silence) {
		if (value == null) {
			if (silence) return true;
			ConsoleLogger.error("Invalid value for inventory '" + inventoryName + "': '" + propertyName + "' is missing.");
			return true;
		}
		return false;
	}


	protected static boolean isNotTypeRequired(Object value, String inventoryName, String propertyName, Class<?>... requiredTypes) {
		boolean isInstance = false;
		for (Class<?> requiredType : requiredTypes) {
			if (requiredType.isInstance(value)) {
				isInstance = true;
				break;
			}
		}
		if (!isInstance) {
			String requiredTypeNames = Arrays.stream(requiredTypes).map(Class::getSimpleName).reduce((s1, s2) -> s1 + " or " + s2).orElse("");
			ConsoleLogger.error("Invalid " + propertyName + " for inventory '" + inventoryName + "': '" + value + "' is not a " + requiredTypeNames + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //


	private static final Map<String, List<String>> ignoredPaths = new HashMap<>() {{

		put("title", List.of(
		));

		put("rows", List.of(
		));

		put("inventoryScheme", List.of(
				"homeEditing",

				"kitEditing",
				"kitEditor",

				"warpEditing",

				"whoisView",

				"balanceTop",
				"ecoAction",
				"ecoAmount",
				"sell",
				"worth"
		));
	}};


	// -------------------------------------------------- //
}
