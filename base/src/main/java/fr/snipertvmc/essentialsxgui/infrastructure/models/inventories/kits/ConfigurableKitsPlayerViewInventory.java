package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableKitsPlayerViewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem kitItem;
	private final ConfigurableItem noKitsItem;

	private final ConfigurableItem switchToAdminModeItem;
	private final ConfigurableItem searchKitItem;
	private final ConfigurableItem cancelSearchKitItem;
	private final ConfigurableItem noSearchKitResultsItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableKitsPlayerViewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.kitItem = inventoryFile.getItem("kitItem");
		this.noKitsItem = inventoryFile.getItem("noKitsItem");

		this.switchToAdminModeItem = inventoryFile.getItem("switchToAdminModeItem");
		this.searchKitItem = inventoryFile.getItem("searchKitItem");
		this.cancelSearchKitItem = inventoryFile.getItem("cancelSearchKitItem");
		this.noSearchKitResultsItem = inventoryFile.getItem("noSearchKitResultsItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getKitItem() {
		return kitItem;
	}
	public ConfigurableItem getNoKitsItem() {
		return noKitsItem;
	}

	public ConfigurableItem getSwitchToAdminModeItem() {
		return switchToAdminModeItem;
	}
	public ConfigurableItem getSearchKitItem() {
		return searchKitItem;
	}
	public ConfigurableItem getCancelSearchKitItem() {
		return cancelSearchKitItem;
	}
	public ConfigurableItem getNoSearchKitResultsItem() {
		return noSearchKitResultsItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
