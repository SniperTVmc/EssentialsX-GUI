package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationParser {


	// -------------------------------------------------- //



	public static boolean isConfigurationValid(ConfigurationFile configuration, boolean silence) {


		// Get the configuration sections
		ConfigurationSection generalSection = configuration.getYamlConfiguration().getConfigurationSection("general");
		if (generalSection == null) {
			if (!silence) {
				ConsoleLogger.error("Invalid configuration: the 'general' section is missing.");
			}
			return false;
		}

		Map<String, ConfigurationSection> modulesSections = new HashMap<>() {{
			put("homes", configuration.getYamlConfiguration().getConfigurationSection("homes"));
			put("kits", configuration.getYamlConfiguration().getConfigurationSection("kits"));
			put("warps", configuration.getYamlConfiguration().getConfigurationSection("warps"));
			put("whois", configuration.getYamlConfiguration().getConfigurationSection("whois"));
			put("economy", configuration.getYamlConfiguration().getConfigurationSection("economy"));
		}};

		for (Map.Entry<String, ConfigurationSection> entry : modulesSections.entrySet()) {
			if (entry.getValue() == null) {
				if (!silence) {
					ConsoleLogger.error("Invalid configuration: the '" + entry.getKey() + "' section is missing.");
				}
				return false;
			}
		}


		// Validate configuration properties
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isGeneralSectionValid(generalSection, silence)) isValid = false;
		if (!ConfigurationPropertyParser.isHomesModuleValid(modulesSections.get("homes"), silence)) isValid = false;
		if (!ConfigurationPropertyParser.isKitsModuleValid(modulesSections.get("kits"), silence)) isValid = false;
		if (!ConfigurationPropertyParser.isWarpsModuleValid(modulesSections.get("warps"), silence)) isValid = false;
		if (!ConfigurationPropertyParser.isWhoisModuleValid(modulesSections.get("whois"), silence)) isValid = false;
		if (!ConfigurationPropertyParser.isEconomyModuleValid(modulesSections.get("economy"), silence)) isValid = false;
		if (!ConfigurationPropertyParser.isSoundsSectionValid(generalSection, silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStorageSectionValid(generalSection, silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
