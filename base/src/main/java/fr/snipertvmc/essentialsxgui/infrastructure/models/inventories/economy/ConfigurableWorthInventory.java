package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWorthInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem allItem;
	private final ConfigurableItem handItem;
	private final ConfigurableItem inventoryItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableWorthInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.allItem = inventoryFile.getItem("allItem");
		this.handItem = inventoryFile.getItem("handItem");
		this.inventoryItem = inventoryFile.getItem("inventoryItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getAllItem() {
		return allItem;
	}
	public ConfigurableItem getHandItem() {
		return handItem;
	}
	public ConfigurableItem getInventoryItem() {
		return inventoryItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
