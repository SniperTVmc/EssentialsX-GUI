package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules.*;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationParser {


	// -------------------------------------------------- //



	public static boolean isConfigurationValid(ConfigurationFile configuration, boolean silence) {


		// Get configuration sections
		ConfigurationSection generalSection = configuration.getYamlConfiguration().getConfigurationSection("general");
		ConfigurationSection homesSection = configuration.getYamlConfiguration().getConfigurationSection("homes");
		ConfigurationSection kitsSection = configuration.getYamlConfiguration().getConfigurationSection("kits");
		ConfigurationSection warpsSection = configuration.getYamlConfiguration().getConfigurationSection("warps");
		ConfigurationSection whoisSection = configuration.getYamlConfiguration().getConfigurationSection("whois");
		ConfigurationSection economySection = configuration.getYamlConfiguration().getConfigurationSection("economy");
		ConfigurationSection soundsSection = configuration.getYamlConfiguration().getConfigurationSection("sounds");
		ConfigurationSection storageSection = configuration.getYamlConfiguration().getConfigurationSection("storage");


		// Validate configuration properties
		boolean isValid = true;
		if (!GeneralConfigurationParser.isGeneralSectionValid(generalSection, silence)) isValid = false;
		if (!HomesConfigurationParser.isHomesModuleValid(homesSection, silence)) isValid = false;
		if (!KitsConfigurationParser.isKitsModuleValid(kitsSection, silence)) isValid = false;
		if (!WarpsConfigurationParser.isWarpsModuleValid(warpsSection, silence)) isValid = false;
		if (!WhoisConfigurationParser.isWhoisModuleValid(whoisSection, silence)) isValid = false;
		if (!EconomyConfigurationParser.isEconomyModuleValid(economySection, silence)) isValid = false;
		if (!SoundsConfigurationParser.isSoundsSectionValid(soundsSection, silence)) isValid = false;
		if (!StorageConfigurationParser.isStorageSectionValid(storageSection, silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
