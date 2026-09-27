package fr.snipertvmc.essentialsxgui.utilities.parsers.items;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableItemParser {


	// -------------------------------------------------- //


	// --- Required properties by default --- //
	// - enabled
	// - slot
	// - material


	// -------------------------------------------------- //


	public static int isConfigurableItemValid(ConfigurationSection itemSection, String itemPath, boolean silence) {
		int errorsCount = 0;


		// Get the item configuration
		if (itemSection == null) {
			if (silence) return 1;
			ConsoleLogger.error("Invalid item path '" + itemPath + "': the item section is missing.");
			return 1;
		}


		// Get the inventory configuration
		InventoryFile inventoryFile = Main.getInstance().getInventoriesManager().getInventoryFileByItemPath(itemPath);
		int rows = (inventoryFile != null) ? inventoryFile.getRows() : 6;


		// Retrieve item properties
		Object enabled = itemSection.get("enabled");

		Object slot = itemSection.get("slot");

		Object material = itemSection.get("material");
		Object amount = itemSection.get("amount");
		Object data = itemSection.get("data");

		Object displayName = itemSection.get("displayName");
		Object lore = itemSection.get("lore");

		Object enchantments = itemSection.get("enchantments");
		Object itemFlags = itemSection.get("itemFlags");


		// Validate item properties
		if (!ItemPropertyParser.isEnabled(enabled, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isSlotValid(slot, itemPath, rows, silence)) errorsCount++;
		if (!ItemPropertyParser.isMaterialValid(material, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isDataValid(data, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isAmountValid(amount, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isDisplayNameValid(displayName, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isLoreValid(lore, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.areEnchantmentsValid(enchantments, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.areItemFlagsValid(itemFlags, itemPath, silence)) errorsCount++;
		errorsCount += ConfigurableExtraParser.isConfigurableExtraValid(itemSection, itemPath, silence);
		return errorsCount;
	}


	// -------------------------------------------------- //
}
