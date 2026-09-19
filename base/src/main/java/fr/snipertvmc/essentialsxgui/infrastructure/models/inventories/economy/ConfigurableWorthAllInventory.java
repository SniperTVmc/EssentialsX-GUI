package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableWorthAllInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem worthItem;
	private final ConfigurableItem noWorthItem;

	private final ConfigurableItem searchWorthItem;
	private final ConfigurableItem cancelSearchWorthItem;
	private final ConfigurableItem noSearchWorthResultsItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableWorthAllInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.worthItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.worthItem"));
		this.noWorthItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noWorthItem"));

		this.searchWorthItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.searchWorthItem"));
		this.cancelSearchWorthItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelSearchWorthItem"));
		this.noSearchWorthResultsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noSearchWorthResultsItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWorthItem() {
		return worthItem;
	}
	public ConfigurableItem getNoWorthItem() {
		return noWorthItem;
	}

	public ConfigurableItem getSearchWorthItem() {
		return searchWorthItem;
	}
	public ConfigurableItem getCancelSearchWorthItem() {
		return cancelSearchWorthItem;
	}
	public ConfigurableItem getNoSearchWorthItemsItem() {
		return noSearchWorthResultsItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
