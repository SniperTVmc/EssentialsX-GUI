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
		return ItemPropertyParser.isEnabled(enabled, itemPath, silence) &&

				ItemPropertyParser.isSlotValid(slot, itemPath, rows, silence) &&

				ItemPropertyParser.isMaterialValid(material, itemPath, silence) &&
				ItemPropertyParser.isDataValid(data, itemPath, silence) &&
				ItemPropertyParser. isAmountValid(amount, itemPath, silence) &&

				ItemPropertyParser.isDisplayNameValid(displayName, itemPath, silence) &&
				ItemPropertyParser.isLoreValid(lore, itemPath, silence) &&

				ItemPropertyParser.areEnchantmentsValid(enchantments, itemPath, silence) &&
				ItemPropertyParser.areItemFlagsValid(itemFlags, itemPath, silence) &&

				ConfigurableExtraParser.isConfigurableExtraValid(itemSection, itemPath, silence);
	}


	// -------------------------------------------------- //
}
