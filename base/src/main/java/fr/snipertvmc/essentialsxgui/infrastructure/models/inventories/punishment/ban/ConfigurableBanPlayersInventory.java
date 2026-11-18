package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableBanPlayersInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerItem;
	private final ConfigurableItem ipItem;

	private final ConfigurableItem noPlayersItem;

	private final ConfigurableItem closeItem;


	// -------------------------------------------------- //


	public ConfigurableBanPlayersInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.playerItem = inventoryFile.getItem("playerItem");
		this.ipItem = inventoryFile.getItem("ipItem");

		this.noPlayersItem = inventoryFile.getItem("noPlayersItem");

		this.closeItem = inventoryFile.getItem("closeItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}
	public ConfigurableItem getIpItem() {
		return ipItem;
	}

	public ConfigurableItem getNoPlayersItem() {
		return noPlayersItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}


	// -------------------------------------------------- //
}
