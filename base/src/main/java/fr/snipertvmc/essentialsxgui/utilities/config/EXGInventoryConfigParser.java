package fr.snipertvmc.essentialsxgui.utilities.config;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EXGInventoryConfigParser {


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


	public static int isEXGInventoryConfigValid(InventoryFile inventoryFile) {


		// Get the inventory configuration
		YamlConfiguration config = inventoryFile.getYamlConfiguration();
		String inventoryName = inventoryFile.getFileName();


		// Retrieve inventory properties
		Object title = config.get(inventoryName + ".title");
		Object rows = config.get(inventoryName + ".rows");
		Object inventoryScheme = config.get(inventoryName + ".inventoryScheme");



		// Validate inventory properties
		if (!areRowsValid(rows, inventoryName)) {
			return 1;
		}

		int rowsValue = ((Number) rows).intValue();
		int errors = 0;

		errors += isTitleValid(title, inventoryName) ? 0 : 1;
		errors += isInventorySchemeValid(inventoryScheme, rowsValue, inventoryName) ? 0 : 1;
		return errors;
	}

	// -------------------------------------------------- //


	public static boolean isTitleValid(Object title, String inventoryName) {

		if (isIgnored(inventoryName, "title")) {
			return true;
		}

		return isMissing(title, inventoryName, "title", false) ||
				!isNotTypeRequired(title, inventoryName, "title", String.class);

	}


	public static boolean areRowsValid(Object rows, String inventoryName) {

		if (isIgnored(inventoryName, "rows")) {
			return true;
		}

		if (!isMissing(rows, inventoryName, "rows", false) &&
				isNotTypeRequired(rows, inventoryName, "rows", Integer.class)) {
			return false;
		}

		int rowsValue = ((Number) rows).intValue();

		if (rowsValue < 1 || rowsValue > 6) {
			ConsoleLogger.error("Invalid rows in inventory '" + inventoryName + "': it must be between 1 and 6.");
			return false;
		}

		return true;
	}


	public static boolean isInventorySchemeValid(Object inventoryScheme, int rows, String inventoryName) {

		if (isIgnored(inventoryName, "inventoryScheme")) {
			return true;
		}

		if (!isMissing(inventoryScheme, inventoryName, "inventoryScheme", true) &&
				isNotTypeRequired(inventoryScheme, inventoryName, "inventoryScheme", List.class)) {
			return false;
		}

		List<String> schemeLines = (List<String>) inventoryScheme;

		if (schemeLines.isEmpty() || schemeLines.size() != rows) {
			ConsoleLogger.error("Invalid inventory scheme in inventory '" + inventoryName + "': it must contain " + rows + " lines.");
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


	private static boolean isIgnored(String itemPath, String propertyName) {
		return ignoredPaths.containsKey(propertyName) && ignoredPaths.get(propertyName).contains(itemPath);
	}


	private static boolean isMissing(Object value, String inventoryName, String propertyName, boolean silence) {
		if (value == null) {
			if (!silence) {
				ConsoleLogger.error("Invalid value in inventory '" + inventoryName + "': '" + propertyName + "' is missing.");
			}
			return true;
		}
		return false;
	}


	private static boolean isNotTypeRequired(Object value, String inventoryName, String propertyName, Class<?> requiredType) {
		if (!requiredType.isInstance(value)) {
			ConsoleLogger.error("Invalid " + propertyName + " in inventory '" + inventoryName + "': '" + value + "' is not a " + requiredType.getSimpleName() + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //
}
