package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class GeneralConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isGeneralSectionValid(ConfigurationSection generalSection, boolean silence) {


		// Validate the general section
		if (!ConfigurationPropertyParser.isSectionValid(generalSection, "general", silence)) return false;


		// Get the instantCreationDefaultValues section
		ConfigurationSection instantCreationDefaultValuesSection = generalSection.getConfigurationSection("instantCreationDefaultValues");


		// Validate configuration properties
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("detailedLoading"), "general.detailedLoading", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("checkForUpdates"), "general.checkForUpdates", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isTimezoneValid(generalSection.get("dateTimezone"), "general.dateTimezone", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("minNameLength"), "general.minNameLength", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("maxNameLength"), "general.maxNameLength", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("delayForTypingInChat"), "general.delayForTypingInChat", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("skipDataEntryProcess"), "general.skipDataEntryProcess", silence)) isValid = false;
		if (!areInstantCreationDefaultValuesValid(instantCreationDefaultValuesSection, generalSection.get("skipDataEntryProcess"), silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //



	private static boolean areInstantCreationDefaultValuesValid(ConfigurationSection instantCreationDefaultValuesSection, Object skipDataEntryProcess, boolean silence) {
		if (!(skipDataEntryProcess instanceof Boolean) || !((Boolean) skipDataEntryProcess)) return true;
		boolean isValid = true;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("homeName"), "general.instantCreationDefaultValues.homeName", silence)) isValid = false;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("kitName"), "general.instantCreationDefaultValues.kitName", silence)) isValid = false;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("kitDelay"), "general.instantCreationDefaultValues.kitDelay", silence)) isValid = false;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("warpName"), "general.instantCreationDefaultValues.warpName", silence)) isValid = false;
		return isValid;
	}


	private static boolean isDefaultValueValid(Object value, String configurationPath, boolean silence) {
		if (ConfigurationPropertyParser.isMissing(value, configurationPath, silence)) return false;
		if (ConfigurationPropertyParser.isNotTypeRequired(value, configurationPath, silence, String.class, Number.class)) return false;
		if (value instanceof String) {
			if (!((String) value).contains("%number%)")) {
				if (silence) return false;
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property must contain the placeholder '%number%'.");
				return false;
			}
		}
		if (value instanceof Integer) return ConfigurationPropertyParser.isPositive(configurationPath, silence, (Integer) value);
		return true;
	}


	// -------------------------------------------------- //
}
