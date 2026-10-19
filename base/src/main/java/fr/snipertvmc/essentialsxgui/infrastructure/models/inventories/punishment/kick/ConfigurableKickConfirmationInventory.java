package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableKickConfirmationInventory extends ConfigurableInventory {


	// -------------------------------------------------- //

	private final ConfigurableItem confirmItem;

	private final ConfigurableItem cancelItem;


	// -------------------------------------------------- //


	public ConfigurableKickConfirmationInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.confirmItem = inventoryFile.getItem("confirmItem");

		this.cancelItem = inventoryFile.getItem("cancelItem");
	}


	// -------------------------------------------------- //

	public ConfigurableItem getConfirmItem() {
		return confirmItem;
	}

	public ConfigurableItem getCancelItem() {
		return cancelItem;
	}


	// -------------------------------------------------- //
}
