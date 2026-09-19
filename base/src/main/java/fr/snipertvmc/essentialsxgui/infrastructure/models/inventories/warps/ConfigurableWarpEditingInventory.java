package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableWarpEditingInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem previewWarpItem;

	private final ConfigurableItem changeDisplayNameItem;
	private final ConfigurableItem changeIconItem;
	private final ConfigurableItem deleteWarpItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableWarpEditingInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.previewWarpItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.previewWarpItem"));

		this.changeDisplayNameItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeDisplayNameItem"));
		this.changeIconItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeIconItem"));
		this.deleteWarpItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.deleteWarpItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPreviewWarpItem() {
		return previewWarpItem;
	}

	public ConfigurableItem getChangeDisplayNameItem() {
		return changeDisplayNameItem;
	}
	public ConfigurableItem getChangeIconItem() {
		return changeIconItem;
	}
	public ConfigurableItem getDeleteWarpItem() {
		return deleteWarpItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
