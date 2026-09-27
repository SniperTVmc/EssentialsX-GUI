package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class KitsConfigurationParser {


	// -------------------------------------------------- //


	public static int isKitsModuleValid(ConfigurationSection kitsSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(kitsSection, "kits", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(kitsSection.get("enabled"), "kits.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isKitsModuleEnabled()) return 0;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("createNewKitNameEntryType"), "kits.createNewKitNameEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("createNewKitDelayEntryType"), "kits.createNewKitDelayEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("searchKitEntryType"), "kits.searchKitEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("changeKitDisplayNameEntryType"), "kits.changeKitDisplayNameEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("changeKitIconEntryType"), "kits.changeKitIconEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMaterialsListValid(kitsSection.get("changeKitIconMaterialsList"), "kits.changeKitIconMaterialsList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isCharactersListValid(kitsSection.get("changeKitDisplayNameCharactersList"), "kits.changeKitDisplayNameCharactersList", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("deleteKitEntryType"), "kits.deleteKitEntryType", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isBooleanValid(kitsSection.get("openKitAdminViewByDefault"), "kits.openKitAdminViewByDefault", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isElementsListValid(kitsSection.get("kitsVisibleWithoutPermission"), "kits.kitsVisibleWithoutPermission", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isElementsListValid(kitsSection.get("customKitsOrder"), "kits.customKitsOrder", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isDefaultIconValid(kitsSection.getConfigurationSection("defaultKitIcon"), "kits.defaultKitIcon", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
