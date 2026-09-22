package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWorthAllInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem worthItem;
	private final ConfigurableItem noWorthItem;

	private final ConfigurableItem searchWorthItem;
	private final ConfigurableItem cancelSearchWorthItem;
	private final ConfigurableItem noSearchWorthResultsItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableWorthAllInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.worthItem = inventoryFile.getItem("worthItem");
		this.noWorthItem = inventoryFile.getItem("noWorthItem");

		this.searchWorthItem = inventoryFile.getItem("searchWorthItem");
		this.cancelSearchWorthItem = inventoryFile.getItem("cancelSearchWorthItem");
		this.noSearchWorthResultsItem = inventoryFile.getItem("noSearchWorthResultsItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWorthItem() {
		return worthItem;
	}
	public ConfigurableItem getNoWorthItem() {
		return noWorthItem;
	}

	public ConfigurableItem getSearchWorthItem() {
		return searchWorthItem;
	}
	public ConfigurableItem getCancelSearchWorthItem() {
		return cancelSearchWorthItem;
	}
	public ConfigurableItem getNoSearchWorthItemsItem() {
		return noSearchWorthResultsItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
