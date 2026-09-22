package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWorthInventoryInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem worthItem;
	private final ConfigurableItem emptyInventoryItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableWorthInventoryInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.worthItem = inventoryFile.getItem("worthItem");
		this.emptyInventoryItem = inventoryFile.getItem("emptyInventoryItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWorthItem() {
		return worthItem;
	}
	public ConfigurableItem getEmptyInventoryItem() {
		return emptyInventoryItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
