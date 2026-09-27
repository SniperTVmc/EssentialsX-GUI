package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class WarpsConfigurationParser {


	// -------------------------------------------------- //


	public static int isWarpsModuleValid(ConfigurationSection warpsSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(warpsSection, "warps", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(warpsSection.get("enabled"), "warps.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isWarpsModuleEnabled()) return 0;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("createNewWarpEntryType"), "warps.createNewWarpEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("searchWarpEntryType"), "warps.searchWarpEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("changeWarpDisplayNameEntryType"), "warps.changeWarpDisplayNameEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("changeWarpIconEntryType"), "warps.changeWarpIconEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMaterialsListValid(warpsSection.get("changeWarpIconMaterialsList"), "warps.changeWarpIconMaterialsList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isCharactersListValid(warpsSection.get("changeWarpDisplayNameCharactersList"), "warps.changeWarpDisplayNameCharactersList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(warpsSection.get("deleteWarpEntryType"), "warps.deleteWarpEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isBooleanValid(warpsSection.get("openWarpAdminViewByDefault"), "warps.openWarpAdminViewByDefault", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isElementsListValid(warpsSection.get("warpsVisibleWithoutPermission"), "warps.warpsVisibleWithoutPermission", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isElementsListValid(warpsSection.get("customWarpsOrder"), "warps.customWarpsOrder", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isDefaultIconValid(warpsSection.getConfigurationSection("defaultWarpIcon"), "warps.defaultWarpIcon", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
