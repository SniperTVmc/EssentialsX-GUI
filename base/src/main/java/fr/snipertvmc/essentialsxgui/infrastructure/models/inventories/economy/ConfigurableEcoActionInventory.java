package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableEcoActionInventory extends ConfigurableInventory {


	// -------------------------------------------------- //

	private final ConfigurableItem playerItem;

	private final ConfigurableItem addBalanceItem;
	private final ConfigurableItem takeBalanceItem;
	private final ConfigurableItem setBalanceItem;
	private final ConfigurableItem resetBalanceItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableEcoActionInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.playerItem = inventoryFile.getItem("playerItem");

		this.addBalanceItem = inventoryFile.getItem("addBalanceItem");
		this.takeBalanceItem = inventoryFile.getItem("takeBalanceItem");
		this.setBalanceItem = inventoryFile.getItem("setBalanceItem");
		this.resetBalanceItem = inventoryFile.getItem("resetBalanceItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getAddBalanceItem() {
		return addBalanceItem;
	}
	public ConfigurableItem getTakeBalanceItem() {
		return takeBalanceItem;
	}
	public ConfigurableItem getSetBalanceItem() {
		return setBalanceItem;
	}
	public ConfigurableItem getResetBalanceItem() {
		return resetBalanceItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
