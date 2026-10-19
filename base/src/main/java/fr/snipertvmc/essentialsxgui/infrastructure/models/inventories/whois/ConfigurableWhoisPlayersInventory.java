package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWhoisPlayersInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem noPlayersItem;

	private final ConfigurableItem playerItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableWhoisPlayersInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.noPlayersItem = inventoryFile.getItem("noPlayersItem");

		this.playerItem = inventoryFile.getItem("playerItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


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
