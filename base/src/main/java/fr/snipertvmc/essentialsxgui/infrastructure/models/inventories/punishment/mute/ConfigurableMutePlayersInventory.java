package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableMutePlayersInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerItem;

	private final ConfigurableItem noPlayersItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableMutePlayersInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.playerItem = inventoryFile.getItem("playerItem");

		this.noPlayersItem = inventoryFile.getItem("noPlayersItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getNoPlayersItem() {
		return noPlayersItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
