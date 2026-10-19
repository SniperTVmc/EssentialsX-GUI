package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableKickPlayersInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem kickAllItem;

	private final ConfigurableItem noPlayersItem;

	private final ConfigurableItem playerItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableKickPlayersInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.kickAllItem = inventoryFile.getItem("kickAllItem");

		this.noPlayersItem = inventoryFile.getItem("noPlayersItem");

		this.playerItem = inventoryFile.getItem("playerItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getKickAllItem() {
		return kickAllItem;
	}

	public ConfigurableItem getNoPlayersItem() {
		return noPlayersItem;
	}

	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
