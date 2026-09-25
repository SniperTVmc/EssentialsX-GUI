package fr.snipertvmc.essentialsxgui.utilities.parsers.items;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableExtraParser {


	// -------------------------------------------------- //


	public static boolean isConfigurableExtraValid(ConfigurationSection itemSection, String itemPath, boolean silence) {


		// Get the extra configuration
		Object extra = itemSection.get("extra");
		ConfigurationSection extraSection = (extra instanceof ConfigurationSection) ? (ConfigurationSection) extra : null;
		if (extraSection == null) {
			if (!ItemPropertyParser.isExtraRequired(itemPath)) return true;
			if (silence) return false;
			ConsoleLogger.error("Invalid item path '" + itemPath + "': the extra section is missing.");
			return false;
		}


		// Extra properties
		Object skullOwner = extraSection.get("skullOwner");
		Object customModelData = extraSection.get("customModelData");

		Object clickActions = extraSection.get("clickActions");

		Object updateItemInterval = extraSection.get("updateItemInterval");
		Object amountValue = extraSection.get("amountValue");


		// Non-extra properties
		Object material = itemSection.get("material");
		Object slot = itemSection.get("slot");


		// Validate item properties
		boolean isValid = true;
		if (!ItemPropertyParser.isSkullOwnerValid(skullOwner, material, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isCustomModelDataValid(customModelData, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.areClickActionsValid(clickActions, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isUpdateItemIntervalValid(updateItemInterval, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isAmountValueValid(amountValue, slot, itemPath, silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
