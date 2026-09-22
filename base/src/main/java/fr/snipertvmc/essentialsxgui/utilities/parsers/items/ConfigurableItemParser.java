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


	public static boolean isConfigurableItemValid(ConfigurationSection itemSection, String itemPath) {


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

		Object extra = itemSection.get("extra");


		// Validate item properties
		return ItemPropertyParser.isEnabled(enabled, itemPath) &&

				ItemPropertyParser.isSlotValid(slot, itemPath, rows) &&

				ItemPropertyParser.isMaterialValid(material, itemPath) &&
				ItemPropertyParser.isDataValid(data, itemPath) &&
				ItemPropertyParser. isAmountValid(amount, itemPath) &&

				ItemPropertyParser.isDisplayNameValid(displayName, itemPath) &&
				ItemPropertyParser.isLoreValid(lore, itemPath) &&

				ItemPropertyParser.areEnchantmentsValid(enchantments, itemPath) &&
				ItemPropertyParser.areItemFlagsValid(itemFlags, itemPath) &&

				ConfigurableExtraParser.isConfigurableExtraValid(extra, itemPath);
	}


	// -------------------------------------------------- //
}
