package fr.snipertvmc.essentialsxgui.utilities.parsers;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationParser {


	// -------------------------------------------------- //



	public static boolean isConfigurationValid(YamlConfiguration configuration) {

		return areInstantCreationDefaultNameValid(configuration);
	}


	// -------------------------------------------------- //



	private static boolean areInstantCreationDefaultNameValid(YamlConfiguration configuration) {
		return true;
	}


	// -------------------------------------------------- //
}
