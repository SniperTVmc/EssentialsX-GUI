package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConfigurationPropertyParser {


	// -------------------------------------------------- //



	protected static boolean areInstantCreationDefaultValuesValid(ConfigurationFile configuration, boolean silence) {

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

		boolean isValid = true;
		if (!isDefaultValueValid(defaultHomeName, "general.instantCreationDefaultValues.homeName", silence)) isValid = false;
		if (!isDefaultValueValid(defaultKitName, "general.instantCreationDefaultValues.kitName", silence)) isValid = false;
		if (!isDefaultValueValid(defaultKitDelay, "general.instantCreationDefaultValues.kitDelay", silence)) isValid = false;
		if (!isDefaultValueValid(defaultWarpName, "general.instantCreationDefaultValues.warpName", silence)) isValid = false;
		return isValid;
	}


	private static boolean isDefaultValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class, Number.class)) return false;
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


	protected static boolean isHomesModuleValid(ConfigurationSection homesSection, boolean silence) {
		boolean isValid = true;
		if (!isMultipleChoiceValueValid(homesSection.get("createNewHomeEntryType"), "homes.createNewHomeEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(homesSection.get("searchHomeEntryType"), "homes.searchHomeEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(homesSection.get("changeHomeDisplayNameEntryType"), "homes.changeHomeDisplayNameEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(homesSection.get("changeHomeIconEntryType"), "homes.changeHomeIconEntryType", silence)) isValid = false;
		if (!isMaterialsListValid(homesSection.get("changeHomeIconMaterialsList"), "homes.changeHomeIconMaterialsList", silence)) isValid = false;
		if (!isCharactersListValid(homesSection.get("changeHomeDisplayNameCharactersList"), "homes.changeHomeDisplayNameCharactersList", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(homesSection.get("deleteHomeEntryType"), "homes.deleteHomeEntryType", silence)) isValid = false;
		if (!isDefaultIconValid(homesSection.getConfigurationSection("defaultHomeIcon"), "homes.defaultHomeIcon", silence)) isValid = false;
		return isValid;
	}


	protected static boolean isKitsModuleValid(ConfigurationSection kitsSection, boolean silence) {
		boolean isValid = true;
		if (!isMultipleChoiceValueValid(kitsSection.get("createNewKitNameEntryType"), "kits.createNewKitNameEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(kitsSection.get("createNewKitDelayEntryType"), "kits.createNewKitDelayEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(kitsSection.get("searchKitEntryType"), "kits.searchKitEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(kitsSection.get("changeKitDisplayNameEntryType"), "kits.changeKitDisplayNameEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(kitsSection.get("changeKitIconEntryType"), "kits.changeKitIconEntryType", silence)) isValid = false;
		if (!isMaterialsListValid(kitsSection.get("changeKitIconMaterialsList"), "kits.changeKitIconMaterialsList", silence)) isValid = false;
		if (!isCharactersListValid(kitsSection.get("changeKitDisplayNameCharactersList"), "kits.changeKitDisplayNameCharactersList", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(kitsSection.get("deleteKitEntryType"), "kits.deleteKitEntryType", silence)) isValid = false;
		if (!isDefaultIconValid(kitsSection.getConfigurationSection("defaultKitIcon"), "kits.defaultKitIcon", silence)) isValid = false;
		return isValid;
	}


	protected static boolean isWarpsModuleValid(ConfigurationSection warpsSection, boolean silence) {
		return true;
	}


	protected static boolean isWhoisModuleValid(ConfigurationSection whoisSection, boolean silence) {
		return true;
	}


	protected static boolean isEconomyModuleValid(ConfigurationSection economySection, boolean silence) {
		return true;
	}


	// -------------------------------------------------- //


	private static boolean isMultipleChoiceValueValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		return hasPossibleValue(value, configurationPath, silence);
	}


	private static boolean isMaterialsListValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, List.class)) return false;
		List<?> materialsList = (List<?>) value;
		for (Object material : materialsList) {
			if (isNotTypeRequired(material, configurationPath, silence, String.class)) return false;
			if (isMaterialValid(material, configurationPath, true)) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid material '" + material + "'.");
				return false;
			}
		}
		return true;
	}


	private static boolean isMaterialValid(Object material, String configurationPath, boolean silence) {
		if (material == null) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property is missing.");
			return false;
		}
		if (isNotTypeRequired(material, "material", true, String.class)) return false;
		return XMaterial.matchXMaterial(material.toString()).isEmpty();
	}


	private static boolean isCharactersListValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		if (((String) value).isEmpty()) return true;
		if (((String) value).startsWith("regex:")) {
			String regex = ((String) value).substring("regex:".length());
			return isRegexValid(regex, configurationPath, silence);
		}
		return true;
	}


	private static boolean isRegexValid(String regex, String configurationPath, boolean silence) {
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid regex pattern.");
			return false;
		}
		return true;
	}


	private static boolean isDefaultIconValid(ConfigurationSection defaultIcon, String configurationPath, boolean silence) {
		if (defaultIcon == null) {
			if (silence) return false;
			ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the section is missing.");
			return false;
		}
		Object material = defaultIcon.get("material");
		Object data = defaultIcon.get("data");
		return isMaterialValid(material, configurationPath, silence) && isDataValid(data, configurationPath + ".data", true);
	}


	protected static boolean isDataValid(Object data, String configurationPath, boolean silence) {
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


		// Homes module
		put("createNewHomeEntryType", List.of("CHAT", "ANVIL"));
		put("searchHomeEntryType", List.of("CHAT", "ANVIL"));
		put("changeHomeDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("changeHomeIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("deleteHomeEntryType", List.of("CHAT", "ANVIL"));


		// Kits module
		put("createNewKitNameEntryType", List.of("CHAT", "ANVIL"));
		put("createNewKitDelayEntryType", List.of("CHAT", "ANVIL"));
		put("searchKitEntryType", List.of("CHAT", "ANVIL"));
		put("changeKitDisplayNameEntryType", List.of("CHAT", "ANVIL"));
		put("changeKitIconEntryType", List.of("CHAT", "ANVIL", "GUI", "ITEM_IN_HAND"));
		put("deleteKitEntryType", List.of("CHAT", "ANVIL"));
	}};


	// -------------------------------------------------- //
}
