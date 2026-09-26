package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class KitsConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isKitsModuleValid(ConfigurationSection kitsSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(kitsSection, "kits", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(kitsSection.get("enabled"), "kits.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isKitsModuleEnabled()) return true;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("createNewKitNameEntryType"), "kits.createNewKitNameEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("createNewKitDelayEntryType"), "kits.createNewKitDelayEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("searchKitEntryType"), "kits.searchKitEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("changeKitDisplayNameEntryType"), "kits.changeKitDisplayNameEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("changeKitIconEntryType"), "kits.changeKitIconEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMaterialsListValid(kitsSection.get("changeKitIconMaterialsList"), "kits.changeKitIconMaterialsList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isCharactersListValid(kitsSection.get("changeKitDisplayNameCharactersList"), "kits.changeKitDisplayNameCharactersList", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(kitsSection.get("deleteKitEntryType"), "kits.deleteKitEntryType", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isBooleanValid(kitsSection.get("openKitAdminViewByDefault"), "kits.openKitAdminViewByDefault", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isElementsListValid(kitsSection.get("kitsVisibleWithoutPermission"), "kits.kitsVisibleWithoutPermission", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isElementsListValid(kitsSection.get("customKitsOrder"), "kits.customKitsOrder", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isDefaultIconValid(kitsSection.getConfigurationSection("defaultKitIcon"), "kits.defaultKitIcon", silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
