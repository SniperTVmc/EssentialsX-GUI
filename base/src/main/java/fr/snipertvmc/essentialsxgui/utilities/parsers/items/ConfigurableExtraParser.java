package fr.snipertvmc.essentialsxgui.utilities.parsers.items;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableExtraParser {


	// -------------------------------------------------- //


	public static boolean isConfigurableExtraValid(Object extra, String itemPath) {


		// Get the inventory configuration
		ConfigurationSection extraSection = (extra instanceof ConfigurationSection) ? (ConfigurationSection) extra : null;
		if (extraSection == null) {
			if (!ItemPropertyParser.isExtraRequired(itemPath)) return true;
			ConsoleLogger.error("Invalid item path '" + itemPath + "': the extra section is missing.");
			return false;
		}


		// Retrieve item properties
		Object skullOwner = extraSection.get("skullOwner");
		Object customModelData = extraSection.get("customModelData");

		Object clickActions = extraSection.get("clickActions");

		Object updateItemInterval = extraSection.get("updateItemInterval");
		Object amountValue = extraSection.get("amountValue");


		// Validate item properties
		return ItemPropertyParser.isSkullOwnerValid(skullOwner, itemPath) &&
				ItemPropertyParser.isCustomModelDataValid(customModelData, itemPath) &&

				ItemPropertyParser.areClickActionsValid(clickActions, itemPath) &&

				ItemPropertyParser.isUpdateItemIntervalValid(updateItemInterval, itemPath) &&
				ItemPropertyParser.isAmountValueValid(amountValue, itemPath);
	}


	// -------------------------------------------------- //
}
