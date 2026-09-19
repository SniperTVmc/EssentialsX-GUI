package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableKitPreviewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem kitItem;
	private final ConfigurableItem emptyKitItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableKitPreviewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.kitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.kitItem"));
		this.emptyKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.emptyKitItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getKitItem() {
		return kitItem;
	}
	public ConfigurableItem getEmptyKitItem() {
		return emptyKitItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
