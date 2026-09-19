package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;

public class ConfigurablePaginatedInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final InventoryScheme inventoryScheme;

	private final ConfigurableItem nextPageItem;
	private final ConfigurableItem previousPageItem;
	private final ConfigurableItem currentPageItem;


	// -------------------------------------------------- //


	public ConfigurablePaginatedInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		this.inventoryScheme = inventoryFile.getInventoryScheme();

		this.nextPageItem = inventoryFile.getNextPageItem();
		this.previousPageItem = inventoryFile.getPreviousPageItem();
		this.currentPageItem = inventoryFile.getCurrentPageItem();
	}


	// -------------------------------------------------- //


	public InventoryScheme getInventoryScheme() {
		return inventoryScheme;
	}

	public ConfigurableItem getNextPageItem() {
		return nextPageItem;
	}
	public ConfigurableItem getPreviousPageItem() {
		return previousPageItem;
	}
	public ConfigurableItem getCurrentPageItem() {
		return currentPageItem;
	}


	// -------------------------------------------------- //
}
