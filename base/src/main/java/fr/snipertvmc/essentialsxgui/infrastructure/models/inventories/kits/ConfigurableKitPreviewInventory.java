package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableKitPreviewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem kitItem;
	private final ConfigurableItem emptyKitItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableKitPreviewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.kitItem = inventoryFile.getItem("kitItem");
		this.emptyKitItem = inventoryFile.getItem("emptyKitItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getKitItem() {
		return kitItem;
	}
	public ConfigurableItem getEmptyKitItem() {
		return emptyKitItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
