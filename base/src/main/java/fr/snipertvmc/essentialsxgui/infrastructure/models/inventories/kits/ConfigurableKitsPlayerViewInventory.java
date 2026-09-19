package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableKitsPlayerViewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem kitItem;
	private final ConfigurableItem noKitsItem;

	private final ConfigurableItem switchToAdminModeItem;
	private final ConfigurableItem searchKitItem;
	private final ConfigurableItem cancelSearchKitItem;
	private final ConfigurableItem noSearchKitResultsItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableKitsPlayerViewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.kitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.kitItem"));
		this.noKitsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noKitsItem"));

		this.switchToAdminModeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.switchToAdminModeItem"));
		this.searchKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.searchKitItem"));
		this.cancelSearchKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelSearchKitItem"));
		this.noSearchKitResultsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.noSearchKitResultsItem"));

		this.closeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.closeItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getKitItem() {
		return kitItem;
	}
	public ConfigurableItem getNoKitsItem() {
		return noKitsItem;
	}

	public ConfigurableItem getSwitchToAdminModeItem() {
		return switchToAdminModeItem;
	}
	public ConfigurableItem getSearchKitItem() {
		return searchKitItem;
	}
	public ConfigurableItem getCancelSearchKitItem() {
		return cancelSearchKitItem;
	}
	public ConfigurableItem getNoSearchKitResultsItem() {
		return noSearchKitResultsItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
