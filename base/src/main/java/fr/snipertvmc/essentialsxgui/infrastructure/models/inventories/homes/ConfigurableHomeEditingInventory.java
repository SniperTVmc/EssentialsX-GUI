package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

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


		// Items
		this.previewHomeItem = inventoryFile.getItem("previewHomeItem");

		this.changeDisplayNameItem = inventoryFile.getItem("changeDisplayNameItem");
		this.changeIconItem = inventoryFile.getItem("changeIconItem");
		this.deleteHomeItem = inventoryFile.getItem("deleteHomeItem");

		this.backItem = inventoryFile.getItem("backItem");
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
