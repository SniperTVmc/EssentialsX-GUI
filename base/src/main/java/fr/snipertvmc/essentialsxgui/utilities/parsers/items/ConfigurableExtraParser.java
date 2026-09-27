package fr.snipertvmc.essentialsxgui.utilities.parsers.items;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableExtraParser {


	// -------------------------------------------------- //


	public static int isConfigurableExtraValid(ConfigurationSection itemSection, String itemPath, boolean silence) {
		int errorsCount = 0;


		// Get the extra configuration
		Object extra = itemSection.get("extra");
		ConfigurationSection extraSection = (extra instanceof ConfigurationSection) ? (ConfigurationSection) extra : null;
		if (extraSection == null) {
			if (!ItemPropertyParser.isExtraRequired(itemPath)) return 0;
			if (silence) return 1;
			ConsoleLogger.error("Invalid item path '" + itemPath + "': the extra section is missing.");
			return 1;
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
		if (!ItemPropertyParser.isSkullOwnerValid(skullOwner, material, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isCustomModelDataValid(customModelData, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.areClickActionsValid(clickActions, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isUpdateItemIntervalValid(updateItemInterval, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isAmountValueValid(amountValue, slot, itemPath, silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
