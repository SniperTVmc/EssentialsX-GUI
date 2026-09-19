package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableWorthInventoryInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem worthItem;
	private final ConfigurableItem emptyInventoryItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableWorthInventoryInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.worthItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.worthItem"));
		this.emptyInventoryItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.emptyInventoryItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWorthItem() {
		return worthItem;
	}
	public ConfigurableItem getEmptyInventoryItem() {
		return emptyInventoryItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
