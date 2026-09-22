package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableDataEntryGUI extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem materialIconItem;

	private final ConfigurableItem cancelItem;


	// -------------------------------------------------- //



	public ConfigurableDataEntryGUI(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.materialIconItem = inventoryFile.getItem("materialIconItem");

		this.cancelItem = inventoryFile.getItem("cancelItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getMaterialIconItem() {
		return materialIconItem.get();
	}

	public ConfigurableItem getCancelItem() {
		return cancelItem.get();
	}


	// -------------------------------------------------- //
}