package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules.*;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationParser {


	// -------------------------------------------------- //



	public static int isConfigurationValid(ConfigurationFile configuration, boolean silence) {
		int errorsCount = 0;


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
		errorsCount += GeneralConfigurationParser.isGeneralSectionValid(generalSection, silence);
		errorsCount += HomesConfigurationParser.isHomesModuleValid(homesSection, silence);
		errorsCount += KitsConfigurationParser.isKitsModuleValid(kitsSection, silence);
		errorsCount += WarpsConfigurationParser.isWarpsModuleValid(warpsSection, silence);
		errorsCount += WhoisConfigurationParser.isWhoisModuleValid(whoisSection, silence);
		errorsCount += EconomyConfigurationParser.isEconomyModuleValid(economySection, silence);
		errorsCount += SoundsConfigurationParser.isSoundsSectionValid(soundsSection, silence);
		errorsCount += StorageConfigurationParser.isStorageSectionValid(storageSection, silence);
		return errorsCount;
	}


	// -------------------------------------------------- //
}
