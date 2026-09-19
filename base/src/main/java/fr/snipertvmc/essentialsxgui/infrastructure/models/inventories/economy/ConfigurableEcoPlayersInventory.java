package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableEcoPlayersInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableEcoPlayersInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.playerItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerItem"));

		this.closeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.closeItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
