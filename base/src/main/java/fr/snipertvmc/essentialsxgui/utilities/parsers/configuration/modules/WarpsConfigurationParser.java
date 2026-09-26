package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class WarpsConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isWarpsModuleValid(ConfigurationSection warpsSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(warpsSection, "warps", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(warpsSection.get("enabled"), "warps.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isWarpsModuleEnabled()) return true;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("createNewWarpEntryType"), "warps.createNewWarpEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("searchWarpEntryType"), "warps.searchWarpEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("changeWarpDisplayNameEntryType"), "warps.changeWarpDisplayNameEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("changeWarpIconEntryType"), "warps.changeWarpIconEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMaterialsListValid(warpsSection.get("changeWarpIconMaterialsList"), "warps.changeWarpIconMaterialsList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isCharactersListValid(warpsSection.get("changeWarpDisplayNameCharactersList"), "warps.changeWarpDisplayNameCharactersList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("deleteWarpEntryType"), "warps.deleteWarpEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isBooleanValid(warpsSection.get("openWarpAdminViewByDefault"), "warps.openWarpAdminViewByDefault", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isElementsListValid(warpsSection.get("warpsVisibleWithoutPermission"), "warps.warpsVisibleWithoutPermission", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isElementsListValid(warpsSection.get("customWarpsOrder"), "warps.customWarpsOrder", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isDefaultIconValid(warpsSection.getConfigurationSection("defaultWarpIcon"), "warps.defaultWarpIcon", silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
