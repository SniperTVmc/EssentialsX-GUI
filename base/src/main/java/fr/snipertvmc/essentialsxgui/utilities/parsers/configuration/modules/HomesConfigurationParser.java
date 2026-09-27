package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class HomesConfigurationParser {


	// -------------------------------------------------- //


	public static int isHomesModuleValid(ConfigurationSection homesSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(homesSection, "homes", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(homesSection.get("enabled"), "homes.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isHomesModuleEnabled()) return 0;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("createNewHomeEntryType"), "homes.createNewHomeEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("searchHomeEntryType"), "homes.searchHomeEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("changeHomeDisplayNameEntryType"), "homes.changeHomeDisplayNameEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("changeHomeIconEntryType"), "homes.changeHomeIconEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMaterialsListValid(homesSection.get("changeHomeIconMaterialsList"), "homes.changeHomeIconMaterialsList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isCharactersListValid(homesSection.get("changeHomeDisplayNameCharactersList"), "homes.changeHomeDisplayNameCharactersList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(homesSection.get("deleteHomeEntryType"), "homes.deleteHomeEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isDefaultIconValid(homesSection.getConfigurationSection("defaultHomeIcon"), "homes.defaultHomeIcon", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
