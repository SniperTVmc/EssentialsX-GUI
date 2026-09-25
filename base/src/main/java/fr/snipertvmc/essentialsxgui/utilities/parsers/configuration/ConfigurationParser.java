package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;

public class ConfigurationParser {


	// -------------------------------------------------- //



	public static boolean isConfigurationValid(ConfigurationFile configuration, boolean silence) {

		return ConfigurationPropertyParser.areInstantCreationDefaultValuesValid(configuration, silence) &&

				ConfigurationPropertyParser.isHomesModuleValid(configuration.getYamlConfiguration().getConfigurationSection("homes"), silence) &&
				ConfigurationPropertyParser.isKitsModuleValid(configuration, silence) &&
				ConfigurationPropertyParser.isWarpsModuleValid(configuration, silence) &&
				ConfigurationPropertyParser.isWhoisModuleValid(configuration, silence) &&
				ConfigurationPropertyParser.isEconomyModuleValid(configuration, silence);
	}


	// -------------------------------------------------- //
}
