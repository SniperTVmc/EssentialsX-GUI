package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class GeneralConfigurationParser {


	// -------------------------------------------------- //


	public static int isGeneralSectionValid(ConfigurationSection generalSection, boolean silence) {
		int errorsCount = 0;


		// Validate the general section
		if (!ConfigurationPropertyParser.isSectionValid(generalSection, "general", silence)) return 1;


		// Get the instantCreationDefaultValues section
		ConfigurationSection instantCreationDefaultValuesSection = generalSection.getConfigurationSection("instantCreationDefaultValues");


		// Validate configuration properties
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("detailedLoading"), "general.detailedLoading", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("checkForUpdates"), "general.checkForUpdates", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isTimezoneValid(generalSection.get("dateTimezone"), "general.dateTimezone", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("minNameLength"), "general.minNameLength", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("maxNameLength"), "general.maxNameLength", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("delayForTypingInChat"), "general.delayForTypingInChat", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isBooleanValid(generalSection.get("skipDataEntryProcess"), "general.skipDataEntryProcess", silence)) errorsCount++;
		errorsCount += areInstantCreationDefaultValuesValid(instantCreationDefaultValuesSection, generalSection.get("skipDataEntryProcess"), silence);
		if (!ConfigurationPropertyParser.isPositiveValueValid(generalSection.get("dialogButtonsWidth"), "general.dialogButtonsWidth", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //



	private static int areInstantCreationDefaultValuesValid(ConfigurationSection instantCreationDefaultValuesSection, Object skipDataEntryProcess, boolean silence) {
		int errorsCount = 0;
		if (!(skipDataEntryProcess instanceof Boolean) || !((Boolean) skipDataEntryProcess)) return 0;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("homeName"), "general.instantCreationDefaultValues.homeName", silence)) errorsCount++;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("kitName"), "general.instantCreationDefaultValues.kitName", silence)) errorsCount++;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("kitDelay"), "general.instantCreationDefaultValues.kitDelay", silence)) errorsCount++;
		if (!isDefaultValueValid(instantCreationDefaultValuesSection.get("warpName"), "general.instantCreationDefaultValues.warpName", silence)) errorsCount++;
		return errorsCount;
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
