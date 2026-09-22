package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

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


		// Items
		this.previewKitItem = inventoryFile.getItem("previewKitItem");

		this.changeDisplayNameItem = inventoryFile.getItem("changeDisplayNameItem");
		this.changeIconItem = inventoryFile.getItem("changeIconItem");
		this.deleteKitItem = inventoryFile.getItem("deleteKitItem");
		this.editKitContentsItem = inventoryFile.getItem("editKitContentsItem");

		this.backItem = inventoryFile.getItem("backItem");
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
