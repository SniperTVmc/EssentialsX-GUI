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
		return ItemPropertyParser.isSkullOwnerValid(skullOwner, material, itemPath, silence) &&
				ItemPropertyParser.isCustomModelDataValid(customModelData, itemPath, silence) &&

				ItemPropertyParser.areClickActionsValid(clickActions, itemPath, silence) &&

				ItemPropertyParser.isUpdateItemIntervalValid(updateItemInterval, itemPath, silence) &&
				ItemPropertyParser.isAmountValueValid(amountValue, slot, itemPath, silence);
	}


	// -------------------------------------------------- //
}
