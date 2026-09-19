package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableKitEditingInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem previewKitItem;

	private final ConfigurableItem changeDisplayNameItem;
	private final ConfigurableItem changeIconItem;
	private final ConfigurableItem deleteKitItem;
	private final ConfigurableItem editKitContentsItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableKitEditingInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.previewKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.previewKitItem"));

		this.changeDisplayNameItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeDisplayNameItem"));
		this.changeIconItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.changeIconItem"));
		this.deleteKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.deleteKitItem"));
		this.editKitContentsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.editKitContentsItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPreviewKitItem() {
		return previewKitItem;
	}

	public ConfigurableItem getChangeDisplayNameItem() {
		return changeDisplayNameItem;
	}
	public ConfigurableItem getChangeIconItem() {
		return changeIconItem;
	}
	public ConfigurableItem getDeleteKitItem() {
		return deleteKitItem;
	}
	public ConfigurableItem getEditKitContentsItem() {
		return editKitContentsItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
