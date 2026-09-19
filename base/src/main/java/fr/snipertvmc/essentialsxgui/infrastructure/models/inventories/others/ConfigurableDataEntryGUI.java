package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableDataEntryGUI extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem materialIconItem;

	private final ConfigurableItem cancelItem;


	// -------------------------------------------------- //



	public ConfigurableDataEntryGUI(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.materialIconItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.materialIconItem"));

		this.cancelItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getMaterialIconItem() {
		return materialIconItem.get();
	}

	public ConfigurableItem getCancelItem() {
		return cancelItem.get();
	}


	// -------------------------------------------------- //
}