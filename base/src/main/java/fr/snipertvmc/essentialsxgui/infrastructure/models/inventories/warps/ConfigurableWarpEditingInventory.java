package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

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


		// Items
		this.previewWarpItem = inventoryFile.getItem("previewWarpItem");

		this.changeDisplayNameItem = inventoryFile.getItem("changeDisplayNameItem");
		this.changeIconItem = inventoryFile.getItem("changeIconItem");
		this.deleteWarpItem = inventoryFile.getItem("deleteWarpItem");

		this.backItem = inventoryFile.getItem("backItem");
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
