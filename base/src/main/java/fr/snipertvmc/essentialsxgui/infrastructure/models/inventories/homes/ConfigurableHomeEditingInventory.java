package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableHomeEditingInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem previewHomeItem;

	private final ConfigurableItem changeDisplayNameItem;
	private final ConfigurableItem changeIconItem;
	private final ConfigurableItem deleteHomeItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableHomeEditingInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.previewHomeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.previewHomeItem"));

		this.changeDisplayNameItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeDisplayNameItem"));
		this.changeIconItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeIconItem"));
		this.deleteHomeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.deleteHomeItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPreviewHomeItem() {
		return previewHomeItem;
	}

	public ConfigurableItem getChangeDisplayNameItem() {
		return changeDisplayNameItem;
	}
	public ConfigurableItem getChangeIconItem() {
		return changeIconItem;
	}
	public ConfigurableItem getDeleteHomeItem() {
		return deleteHomeItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
