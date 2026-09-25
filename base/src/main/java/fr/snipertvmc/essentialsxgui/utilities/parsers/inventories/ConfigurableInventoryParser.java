package fr.snipertvmc.essentialsxgui.utilities.parsers.inventories;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableInventoryParser {


	// -------------------------------------------------- //


	// --- Required properties by default --- //
	// - title
	// - rows
	// - inventoryScheme


	// -------------------------------------------------- //


	public static boolean isConfigurableInventoryValid(InventoryFile inventoryFile) {


		// Get the inventory configuration
		ConfigurationSection inventorySection = inventoryFile.getYamlConfiguration().getConfigurationSection(inventoryFile.getFileName());
		if (inventorySection == null) {
			ConsoleLogger.error("Invalid inventory path '" + inventoryFile.getFileName() + "': the inventory section is missing.");
			return false;
		}


		// Retrieve inventory properties
		Object title = inventorySection.get("title");
		Object rows = inventorySection.get("rows");

		Object inventoryScheme = inventorySection.get("inventoryScheme");


		// Validate inventory properties
		boolean isValid = true;
		if (!InventoryPropertyParser.isTitleValid(title, inventoryFile.getFileName())) isValid = false;
		if (!InventoryPropertyParser.areRowsValid(rows, inventoryFile.getFileName())) isValid = false;
		if (!InventoryPropertyParser.isInventorySchemeValid(inventoryScheme, inventoryFile.getFileName(), rows)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //
}
