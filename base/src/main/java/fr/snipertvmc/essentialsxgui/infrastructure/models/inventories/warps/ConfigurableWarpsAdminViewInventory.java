package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWarpsAdminViewInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem warpItem;
	private final ConfigurableItem noWarpsItem;

	private final ConfigurableItem switchToPlayerModeItem;
	private final ConfigurableItem createWarpItem;
	private final ConfigurableItem searchWarpItem;
	private final ConfigurableItem cancelSearchWarpItem;
	private final ConfigurableItem noSearchWarpResultsItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableWarpsAdminViewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.warpItem = inventoryFile.getItem("warpItem");
		this.noWarpsItem = inventoryFile.getItem("noWarpsItem");

		this.switchToPlayerModeItem = inventoryFile.getItem("switchToPlayerModeItem");
		this.createWarpItem = inventoryFile.getItem("createWarpItem");
		this.searchWarpItem = inventoryFile.getItem("searchWarpItem");
		this.cancelSearchWarpItem = inventoryFile.getItem("cancelSearchWarpItem");
		this.noSearchWarpResultsItem = inventoryFile.getItem("noSearchWarpResultsItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getWarpItem() {
		return warpItem;
	}
	public ConfigurableItem getNoWarpsItem() {
		return noWarpsItem;
	}

	public ConfigurableItem getSwitchToPlayerModeItem() {
		return switchToPlayerModeItem;
	}
	public ConfigurableItem getCreateWarpItem() {
		return createWarpItem;
	}
	public ConfigurableItem getSearchWarpItem() {
		return searchWarpItem;
	}
	public ConfigurableItem getCancelSearchWarpItem() {
		return cancelSearchWarpItem;
	}
	public ConfigurableItem getNoSearchWarpResultsItem() {
		return noSearchWarpResultsItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
