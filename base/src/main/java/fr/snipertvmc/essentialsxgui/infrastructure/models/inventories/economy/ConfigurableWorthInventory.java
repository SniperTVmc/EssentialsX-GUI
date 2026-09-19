package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableWorthInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem allItem;
	private final ConfigurableItem handItem;
	private final ConfigurableItem inventoryItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableWorthInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.allItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.allItem"));
		this.handItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.handItem"));
		this.inventoryItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.inventoryItem"));

		this.closeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.closeItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getAllItem() {
		return allItem;
	}
	public ConfigurableItem getHandItem() {
		return handItem;
	}
	public ConfigurableItem getInventoryItem() {
		return inventoryItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
