package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

import java.util.Set;

public class ConfigurableCondenseInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem condenseItem;
	private final ConfigurableItem cancelItem;

	private final Set<Integer> borderSlots;


	// -------------------------------------------------- //


	public ConfigurableCondenseInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.condenseItem = inventoryFile.getItem("condenseItem");
		this.cancelItem = inventoryFile.getItem("cancelItem");

		this.borderSlots = inventoryFile.getBorderSlots();
	}


	// -------------------------------------------------- //


	public ConfigurableItem getCondenseItem() {
		return condenseItem;
	}
	public ConfigurableItem getCancelItem() {
		return cancelItem;
	}

	public Set<Integer> getBorderSlots() {
		return borderSlots;
	}


	// -------------------------------------------------- //
}
