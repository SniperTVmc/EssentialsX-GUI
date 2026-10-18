package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class OthersConfigurationParser {


	// -------------------------------------------------- //


	public static int isOthersModuleValid(ConfigurationSection othersModule, boolean silence) {
		int errorsCount = 0;



		// Validate the others section
		if (!ConfigurationPropertyParser.isSectionValid(othersModule, "others", silence)) return 1;


		// Get the others sections
		ConfigurationSection condenseSection = othersModule.getConfigurationSection("condense");


		// Validate the others module properties
		errorsCount += isCondenseModuleValid(condenseSection, silence);
		return errorsCount;
	}


	// -------------------------------------------------- //


	private static int isCondenseModuleValid(ConfigurationSection condenseSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(condenseSection, "others.condense", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(condenseSection.get("enabled"), "others.condense.enabled", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
