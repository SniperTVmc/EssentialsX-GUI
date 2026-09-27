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


	public static int isConfigurableInventoryValid(InventoryFile inventoryFile) {
		int errorsCount = 0;


		// Get the inventory configuration
		ConfigurationSection inventorySection = inventoryFile.getYamlConfiguration().getConfigurationSection(inventoryFile.getFileName());
		if (inventorySection == null) {
			ConsoleLogger.error("Invalid inventory path '" + inventoryFile.getFileName() + "': the inventory section is missing.");
			return 1;
		}


		// Retrieve inventory properties
		Object title = inventorySection.get("title");
		Object rows = inventorySection.get("rows");

		Object inventoryScheme = inventorySection.get("inventoryScheme");


		// Validate inventory properties
		if (!InventoryPropertyParser.isTitleValid(title, inventoryFile.getFileName())) errorsCount++;
		if (!InventoryPropertyParser.areRowsValid(rows, inventoryFile.getFileName())) errorsCount++;
		if (!InventoryPropertyParser.isInventorySchemeValid(inventoryScheme, inventoryFile.getFileName(), rows)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
