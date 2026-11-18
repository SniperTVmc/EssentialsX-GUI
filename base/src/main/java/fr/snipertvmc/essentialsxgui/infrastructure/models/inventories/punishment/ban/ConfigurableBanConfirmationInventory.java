package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableBanConfirmationInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem confirmPlayerItem;
	private final ConfigurableItem confirmIPItem;

	private final ConfigurableItem cancelItem;


	// -------------------------------------------------- //


	public ConfigurableBanConfirmationInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.confirmPlayerItem = inventoryFile.getItem("confirmPlayerItem");
		this.confirmIPItem = inventoryFile.getItem("confirmIPItem");

		this.cancelItem = inventoryFile.getItem("cancelItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getConfirmPlayerItem() {
		return confirmPlayerItem;
	}
	public ConfigurableItem getConfirmIPItem() {
		return confirmIPItem;
	}

	public ConfigurableItem getCancelItem() {
		return cancelItem;
	}


	// -------------------------------------------------- //
}
