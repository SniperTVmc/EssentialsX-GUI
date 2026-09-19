package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

import java.util.Set;

public class ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableInventoryTitle title;
	private final int rows;
	private final Set<ConfigurableItem> borderItems;


	// -------------------------------------------------- //


	public ConfigurableInventory(InventoryFile inventoryFile) {
		this.title = new ConfigurableInventoryTitle(inventoryFile.getTitle());
		this.rows = inventoryFile.getRows();
		this.borderItems = inventoryFile.getBorderItems();
	}


	// -------------------------------------------------- //


	public ConfigurableInventoryTitle getTitle() {
		return title.get();
	}
	public int getRows() {
		return rows;
	}
	public Set<ConfigurableItem> getBorderItems() {
		return borderItems;
	}


	// -------------------------------------------------- //
}
