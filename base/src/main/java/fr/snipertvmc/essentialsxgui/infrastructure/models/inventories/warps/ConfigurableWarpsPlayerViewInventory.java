package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableWarpsPlayerViewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem warpItem;
	private final ConfigurableItem noWarpsItem;

	private final ConfigurableItem switchToAdminModeItem;
	private final ConfigurableItem searchWarpItem;
	private final ConfigurableItem cancelSearchWarpItem;
	private final ConfigurableItem noSearchWarpResultsItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableWarpsPlayerViewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.warpItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.warpItem"));
		this.noWarpsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noWarpsItem"));

		this.switchToAdminModeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.switchToAdminModeItem"));
		this.searchWarpItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.searchWarpItem"));
		this.cancelSearchWarpItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelSearchWarpItem"));
		this.noSearchWarpResultsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noSearchWarpResultsItem"));

		this.closeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.closeItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWarpItem() {
		return warpItem;
	}
	public ConfigurableItem getNoWarpsItem() {
		return noWarpsItem;
	}

	public ConfigurableItem getSwitchToAdminModeItem() {
		return switchToAdminModeItem;
	}
	public ConfigurableItem getSearchWarpItem() {
		return searchWarpItem;
	}
	public ConfigurableItem getCancelSearchWarpItem() {
		return cancelSearchWarpItem;
	}
	public ConfigurableItem getNoSearchWarpResultsItem() {
		return noSearchWarpResultsItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
