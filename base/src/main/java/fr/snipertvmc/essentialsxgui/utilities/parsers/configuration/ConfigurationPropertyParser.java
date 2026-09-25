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



	protected static boolean isGeneralSectionValid(ConfigurationSection generalSection, boolean silence) {


		// Get the configuration properties
		ConfigurationSection instantCreationDefaultValuesSection = generalSection.getConfigurationSection("instantCreationDefaultValues");
		Object detailedLoading = generalSection.get("detailedLoading");
		Object checkForUpdates = generalSection.get("checkForUpdates");
		Object dateTimezone = generalSection.get("dateTimezone");
		Object minNameLength = generalSection.get("minNameLength");
		Object maxNameLength = generalSection.get("maxNameLength");
		Object delayForTypingInChat = generalSection.get("delayForTypingInChat");
		Object skipDataEntryProcess = generalSection.get("skipDataEntryProcess");


		// Validate configuration properties
		boolean isValid = true;
		if (!isBooleanValid(detailedLoading, "general.detailedLoading", silence)) isValid = false;
		if (!isBooleanValid(checkForUpdates, "general.checkForUpdates", silence)) isValid = false;
		if (!isTimezoneValid(dateTimezone, "general.dateTimezone", silence)) isValid = false;
		if (!isPositive("general.minNameLength", silence, (Integer) minNameLength)) isValid = false;
		if (!isPositive("general.maxNameLength", silence, (Integer) maxNameLength)) isValid = false;
		if (!isPositive("general.delayForTypingInChat", silence, (Integer) delayForTypingInChat)) isValid = false;
		if (!isBooleanValid(skipDataEntryProcess, "general.skipDataEntryProcess", silence)) isValid = false;
		if (!areInstantCreationDefaultValuesValid(instantCreationDefaultValuesSection, skipDataEntryProcess, silence)) isValid = false;
		return isValid;
	}



	protected static boolean areInstantCreationDefaultValuesValid(ConfigurationSection instantCreationDefaultValuesSection, Object skipDataEntryProcess, boolean silence) {

		if (!(skipDataEntryProcess instanceof Boolean) || !((Boolean) skipDataEntryProcess)) return true;
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


	// -------------------------------------------------- //


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
		if (!isBooleanValid(kitsSection.get("openKitAdminViewByDefault"), "kits.openKitAdminViewByDefault", silence)) isValid = false;
		if (!isElementsListValid(kitsSection.get("kitsVisibleWithoutPermission"), "kits.kitsVisibleWithoutPermission", silence)) isValid = false;
		if (!isElementsListValid(kitsSection.get("customKitsOrder"), "kits.customKitsOrder", silence)) isValid = false;
		if (!isDefaultIconValid(kitsSection.getConfigurationSection("defaultKitIcon"), "kits.defaultKitIcon", silence)) isValid = false;
		return isValid;
	}


	protected static boolean isWarpsModuleValid(ConfigurationSection warpsSection, boolean silence) {
		boolean isValid = true;
		if (!isMultipleChoiceValueValid(warpsSection.get("createNewWarpEntryType"), "warps.createNewWarpEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(warpsSection.get("searchWarpEntryType"), "warps.searchWarpEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(warpsSection.get("changeWarpDisplayNameEntryType"), "warps.changeWarpDisplayNameEntryType", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(warpsSection.get("changeWarpIconEntryType"), "warps.changeWarpIconEntryType", silence)) isValid = false;
		if (!isMaterialsListValid(warpsSection.get("changeWarpIconMaterialsList"), "warps.changeWarpIconMaterialsList", silence)) isValid = false;
		if (!isCharactersListValid(warpsSection.get("changeWarpDisplayNameCharactersList"), "warps.changeWarpDisplayNameCharactersList", silence)) isValid = false;
		if (!isMultipleChoiceValueValid(warpsSection.get("deleteWarpEntryType"), "warps.deleteWarpEntryType", silence)) isValid = false;
		if (!isBooleanValid(warpsSection.get("openWarpAdminViewByDefault"), "warps.openWarpAdminViewByDefault", silence)) isValid = false;
		if (!isElementsListValid(warpsSection.get("warpsVisibleWithoutPermission"), "warps.warpsVisibleWithoutPermission", silence)) isValid = false;
		if (!isElementsListValid(warpsSection.get("customWarpsOrder"), "warps.customWarpsOrder", silence)) isValid = false;
		if (!isDefaultIconValid(warpsSection.getConfigurationSection("defaultWarpIcon"), "warps.defaultWarpIcon", silence)) isValid = false;
		return isValid;
	}


	protected static boolean isWhoisModuleValid(ConfigurationSection whoisSection, boolean silence) {
		return true;
	}


	protected static boolean isEconomyModuleValid(ConfigurationSection economySection, boolean silence) {
		return true;
	}


	protected static boolean isSoundsSectionValid(ConfigurationSection soundsSection, boolean silence) {
		return true;
	}


	protected static boolean isStorageSectionValid(ConfigurationSection soundsSection, boolean silence) {
		return true;
	}


	// -------------------------------------------------- //


	private static boolean isBooleanValid(Object value, String configurationPath, boolean silence) {
		if (isMissing(value, configurationPath, silence)) return false;
		if (isNotTypeRequired(value, configurationPath, silence, Boolean.class)) return false;
		return true;
	}


	private static boolean isTimezoneValid(Object value, String configurationPath, boolean silence) {
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


	protected static boolean isElementsListValid(Object value, String configurationPath, boolean silence) {
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
