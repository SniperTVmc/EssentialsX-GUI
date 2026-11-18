package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

import java.util.Set;

public class ConfigurableBanDurationsInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final Set<ConfigurableItem> durationPlayerItem;
	private final ConfigurableItem permanentPlayerItem;

	private final Set<ConfigurableItem> durationIPItem;
	private final ConfigurableItem permanentIPItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableBanDurationsInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.durationPlayerItem = inventoryFile.getItems("durationPlayerItem");
		this.permanentPlayerItem = inventoryFile.getItem("permanentPlayerItem");

		this.durationIPItem = inventoryFile.getItems("durationIPItem");
		this.permanentIPItem = inventoryFile.getItem("permanentIPItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getDurationPlayerItems() {
		return durationPlayerItem;
	}
	public ConfigurableItem getPermanentPlayerItem() {
		return permanentPlayerItem;
	}

	public Set<ConfigurableItem> getDurationIPItems() {
		return durationIPItem;
	}
	public ConfigurableItem getPermanentIPItem() {
		return permanentIPItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
