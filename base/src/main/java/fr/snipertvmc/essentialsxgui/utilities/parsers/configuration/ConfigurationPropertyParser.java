package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPropertyParser {


	// -------------------------------------------------- //



	public static boolean areInstantCreationDefaultValuesValid(ConfigurationFile configuration, boolean silence) {

		if (!configuration.skipDataEntryProcess()) return true;
		YamlConfiguration yamlConfiguration = configuration.getYamlConfiguration();
		ConfigurationSection instantCreationDefaultValuesSection = yamlConfiguration.getConfigurationSection("general.instantCreationDefaultValues");
		if (instantCreationDefaultValuesSection == null) {
			ConsoleLogger.error("Invalid configuration '" + configuration.getFileName() + "': the 'general.instantCreationDefaultValues' section is missing.");
			return false;
		}

		Object defaultHomeName = instantCreationDefaultValuesSection.get("homeName");

		Object defaultKitName = instantCreationDefaultValuesSection.get("kitName");
		Object defaultKitDelay = instantCreationDefaultValuesSection.get("kitDelay");

		Object defaultWarpName = instantCreationDefaultValuesSection.get("warpName");


		return isDefaultValueValid(defaultHomeName, "general.instantCreationDefaultValues.homeName", silence) &&

				isDefaultValueValid(defaultKitName, "general.instantCreationDefaultValues.kitName", silence) &&
				isDefaultValueValid(defaultKitDelay, "general.instantCreationDefaultValues.kitDelay", silence) &&

				isDefaultValueValid(defaultWarpName, "general.instantCreationDefaultValues.warpName", silence);
	}


	private static boolean isDefaultValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class, Integer.class)) return false;
		if (value instanceof String) {
			if (!((String) value).contains("%number%)")) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must contain the placeholder '%number%'.");
				return false;
			}
		}
		if (value instanceof Integer) return isPositive(configurationPath, silence, (Integer) value);
		return true;
	}


	public static boolean isHomesModuleValid(ConfigurationSection homesSection, boolean silence) {
		return isMultipleChoiceValueValid(homesSection.get("createNewHomeEntryType"), "homes.createNewHomeEntryType", silence) &&
				isMultipleChoiceValueValid(homesSection.get("searchHomeEntryType"), "homes.searchHomeEntryType", silence) &&
				isMultipleChoiceValueValid(homesSection.get("changeHomeDisplayNameEntryType"), "homes.changeHomeDisplayNameEntryType", silence) &&
				isMultipleChoiceValueValid(homesSection.get("changeHomeIconEntryType"), "homes.changeHomeIconEntryType", silence) &&
				isMultipleChoiceValueValid(homesSection.get("deleteHomeEntryType"), "homes.deleteHomeEntryType", silence);
	}


	public static boolean isKitsModuleValid(ConfigurationFile configuration, boolean silence) {
		return true;
	}


	public static boolean isWarpsModuleValid(ConfigurationFile configuration, boolean silence) {
		return true;
	}


	public static boolean isWhoisModuleValid(ConfigurationFile configuration, boolean silence) {
		return true;
	}


	public static boolean isEconomyModuleValid(ConfigurationFile configuration, boolean silence) {
		return true;
	}


	// -------------------------------------------------- //


	private static boolean isMultipleChoiceValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		return hasPossibleValue(value, configurationPath, silence);
	}



	private static boolean isPositive(String configurationPath, boolean silence, int... values) {
		for (int value : values) {
			if (value <= 0) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must be a positive integer.");
				return false;
			}
		}
		return true;
	}


	// -------------------------------------------------- //


	protected static boolean hasPossibleValue(Object value, String configurationPath, boolean silence) {
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


	protected static boolean isMissing(Object value, String configurationPath, boolean silence) {
		if (value == null) {
			if (silence) return true;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property is missing.");
			return true;
		}
		return false;
	}


	protected static boolean isNotTypeRequired(Object value, String configurationPath, boolean silence, Class<?>... requiredTypes) {
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


	private static final Map<String, List<String>> possibleValues = new HashMap<>() {{

		put("createNewHomeEntryType", List.of("CHAT", "ANVIL"));
		put("searchHomeEntryType", List.of("CHAT", "ANVIL"));
		put("changeHomeDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("changeHomeIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("deleteHomeEntryType", List.of("CHAT", "ANVIL"));
	}};


	// -------------------------------------------------- //
}
