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


	public static boolean isConfigurableItemValid(ConfigurationSection itemSection, String itemPath, boolean silence) {


		// Get the item configuration
		if (itemSection == null) {
			ConsoleLogger.error("Invalid item path '" + itemPath + "': the item section is missing.");
			return false;
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
		boolean isValid = true;
		if (!ItemPropertyParser.isEnabled(enabled, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isSlotValid(slot, itemPath, rows, silence)) isValid = false;
		if (!ItemPropertyParser.isMaterialValid(material, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isDataValid(data, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isAmountValid(amount, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isDisplayNameValid(displayName, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.isLoreValid(lore, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.areEnchantmentsValid(enchantments, itemPath, silence)) isValid = false;
		if (!ItemPropertyParser.areItemFlagsValid(itemFlags, itemPath, silence)) isValid = false;
		if (!ConfigurableExtraParser.isConfigurableExtraValid(itemSection, itemPath, silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
