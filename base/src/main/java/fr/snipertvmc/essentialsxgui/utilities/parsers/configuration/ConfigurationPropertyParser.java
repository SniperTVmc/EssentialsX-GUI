package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConfigurationPropertyParser {


	// -------------------------------------------------- //


	public static boolean isSectionValid(ConfigurationSection section, String path, boolean silence) {
		if (section != null) return true;
		if (!silence) ConsoleLogger.error("Invalid configuration: the '" + path + "' section is missing.");
		return false;
	}


	public static boolean isBooleanValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, Boolean.class)) return false;
		return true;
	}


	public static boolean isTimezoneValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		try {
			TimeZone.getTimeZone((String) value);
		} catch (Exception e) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid timezone.");
			return false;
		}
		return true;
	}


	public static boolean isPositiveValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, Number.class)) return false;
		return isPositive(configurationPath, silence, ((Number) value).intValue());
	}


	public static boolean isPositiveOrZeroValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, Number.class)) return false;
		return isPositiveOrZero(configurationPath, silence, ((Number) value).intValue());
	}


	public static boolean isMultipleChoiceValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		return hasPossibleValue(value, configurationPath, silence);
	}


	public static boolean isMaterialsListValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, List.class)) return false;
		List<?> materialsList = (List<?>) value;
		for (Object material : materialsList) {
			if (isNotTypeRequired(material, configurationPath, silence, String.class)) return false;
			if (!isMaterialValid(material, configurationPath, true)) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid material '" + material + "'.");
				return false;
			}
		}
		return true;
	}


	public static boolean isMaterialValid(Object material, String configurationPath, boolean silence) {
		if (material == null) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property is missing.");
			return false;
		}
		if (isNotTypeRequired(material, configurationPath, silence, String.class)) return false;
		return XMaterial.matchXMaterial(material.toString()).isPresent();
	}


	public static boolean isCharactersListValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		if (((String) value).isEmpty()) return true;
		if (((String) value).startsWith("regex:")) {
			String regex = ((String) value).substring("regex:".length());
			return isRegexValid(regex, configurationPath, silence);
		}
		return true;
	}


	public static boolean isRegexValid(String regex, String configurationPath, boolean silence) {
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid regex pattern.");
			return false;
		}
		return true;
	}


	public static boolean isDefaultIconValid(ConfigurationSection defaultIcon, String configurationPath, boolean silence) {
		if (defaultIcon == null) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the section is missing.");
			return false;
		}
		Object material = defaultIcon.get("material");
		Object data = defaultIcon.get("data");
		return isMaterialValid(material, configurationPath, silence) && isDataValid(data, configurationPath + ".data", silence);
	}


	public static boolean isDataValid(Object data, String configurationPath, boolean silence) {
		// Data is not used in versions > 1.12.2
		if (VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.v1_12_2_R01)) return true;
		if (data == null) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property is missing.");
			return false;
		}
		if (isNotTypeRequired(data, configurationPath, silence, Number.class)) return false;
		if (data instanceof Number dataValue) {
			if (dataValue.intValue() < 0 || dataValue.intValue() > 15) {
				if (silence) return false;
				ConsoleLogger.error("Invalid data for item '" + configurationPath + "': it must be between 0 and 15.");
				return false;
			}
		}
		return true;
	}


	public static boolean isElementsListValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class, List.class)) return false;
		if (value instanceof String stringValue) {
			if (stringValue.equalsIgnoreCase("NONE")) return true;
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be a list of strings or the string 'NONE'.");
			return false;
		}
		List<?> elementsList = (List<?>) value;
		for (Object element : elementsList) {
			if (isNotTypeRequired(element, configurationPath, silence, String.class)) return false;
		}
		return true;
	}


	public static boolean isPositive(String configurationPath, boolean silence, int... values) {
		for (int value : values) {
			if (value <= 0) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be a positive integer.");
				return false;
			}
		}
		return true;
	}


	public static boolean isPositiveOrZero(String configurationPath, boolean silence, int... values) {
		for (int value : values) {
			if (value < 0) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be a positive integer or zero.");
				return false;
			}
		}
		return true;
	}


	public static boolean isStringValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		return true;
	}


	// -------------------------------------------------- //


	public static boolean hasPossibleValue(Object value, String configurationPath, boolean silence) {
		if (value == null) return true;
		List<String> possibleValuesList = possibleValues.get(configurationPath);
		if (possibleValuesList == null) return true;
		if (!possibleValuesList.contains(value.toString())) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be one of the following values: " + String.join(", ", possibleValuesList) + ".");
			return false;
		}
		return true;
	}


	public static boolean isMissing(Object value, String configurationPath, boolean silence) {
		if (value == null) {
			if (silence) return true;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property is missing.");
			return true;
		}
		return false;
	}


	public static boolean isNotTypeRequired(Object value, String configurationPath, boolean silence, Class<?>... requiredTypes) {
		boolean isInstance = false;
		for (Class<?> requiredType : requiredTypes) {
			if (requiredType.isInstance(value)) {
				isInstance = true;
				break;
			}
		}
		if (!isInstance) {
			String requiredTypeNames = Arrays.stream(requiredTypes).map(Class::getSimpleName).reduce((s1, s2) -> s1 + " or " + s2).orElse("");
			if (silence) return true;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be of type " + requiredTypeNames + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //


	protected static final Map<String, List<String>> possibleValues = new HashMap<>() {{


		// Homes module
		put("homes.createNewHomeEntryType", List.of("CHAT", "ANVIL"));
		put("homes.searchHomeEntryType", List.of("CHAT", "ANVIL"));
		put("homes.changeHomeDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("homes.changeHomeIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("homes.deleteHomeEntryType", List.of("CHAT", "ANVIL"));


		// Kits module
		put("kits.createNewKitNameEntryType", List.of("CHAT", "ANVIL"));
		put("kits.createNewKitDelayEntryType", List.of("CHAT", "ANVIL"));
		put("kits.searchKitEntryType", List.of("CHAT", "ANVIL"));
		put("kits.changeKitDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("kits.changeKitIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("kits.deleteKitEntryType", List.of("CHAT", "ANVIL"));


		// Warps module
		put("warps.createNewWarpEntryType", List.of("CHAT", "ANVIL"));
		put("warps.searchWarpEntryType", List.of("CHAT", "ANVIL"));
		put("warps.changeWarpDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("warps.changeWarpIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("warps.deleteWarpEntryType", List.of("CHAT", "ANVIL"));


		// Economy Worth module
		put("economy.worth.searchWorthEntryType", List.of("CHAT", "ANVIL"));


		// Storage module
		put("storage.type", List.of("SQLite", "MySQL", "MariaDB"));
	}};


	// -------------------------------------------------- //
}
