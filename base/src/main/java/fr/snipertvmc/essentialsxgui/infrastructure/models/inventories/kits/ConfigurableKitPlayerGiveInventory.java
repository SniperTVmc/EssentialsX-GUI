package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableKitPlayerGiveInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableKitPlayerGiveInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.playerItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
