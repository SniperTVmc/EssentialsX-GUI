package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

import java.util.Set;

public class ConfigurableEcoAmountInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final Set<ConfigurableItem> addItems;
	private final Set<ConfigurableItem> removeItems;

	private final ConfigurableItem confirmActionItem;
	private final ConfigurableItem cancelActionItem;


	// -------------------------------------------------- //


	public ConfigurableEcoAmountInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.addItems = inventoryFile.getItems("addItem");
		this.removeItems = inventoryFile.getItems("removeItem");

		this.confirmActionItem = inventoryFile.getItem("confirmActionItem");
		this.cancelActionItem = inventoryFile.getItem("cancelActionItem");
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getAddItems() {
		return addItems;
	}
	public Set<ConfigurableItem> getRemoveItems() {
		return removeItems;
	}

	public ConfigurableItem getConfirmActionItem() {
		return confirmActionItem;
	}
	public ConfigurableItem getCancelActionItem() {
		return cancelActionItem;
	}


	// -------------------------------------------------- //
}
