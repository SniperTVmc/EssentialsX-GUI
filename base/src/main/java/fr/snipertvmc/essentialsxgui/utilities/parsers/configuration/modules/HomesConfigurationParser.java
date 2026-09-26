package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class HomesConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isHomesModuleValid(ConfigurationSection homesSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(homesSection, "homes", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(homesSection.get("enabled"), "homes.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isHomesModuleEnabled()) return true;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("createNewHomeEntryType"), "homes.createNewHomeEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("searchHomeEntryType"), "homes.searchHomeEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("changeHomeDisplayNameEntryType"), "homes.changeHomeDisplayNameEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("changeHomeIconEntryType"), "homes.changeHomeIconEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMaterialsListValid(homesSection.get("changeHomeIconMaterialsList"), "homes.changeHomeIconMaterialsList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isCharactersListValid(homesSection.get("changeHomeDisplayNameCharactersList"), "homes.changeHomeDisplayNameCharactersList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("deleteHomeEntryType"), "homes.deleteHomeEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isDefaultIconValid(homesSection.getConfigurationSection("defaultHomeIcon"), "homes.defaultHomeIcon", silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
