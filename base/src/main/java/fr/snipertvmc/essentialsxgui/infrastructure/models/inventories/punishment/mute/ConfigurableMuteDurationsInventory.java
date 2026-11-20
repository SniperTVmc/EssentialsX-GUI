package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

import java.util.Set;

public class ConfigurableMuteDurationsInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final Set<ConfigurableItem> durationItem;
	private final ConfigurableItem permanentItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableMuteDurationsInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.durationItem = inventoryFile.getItems("durationItem");
		this.permanentItem = inventoryFile.getItem("permanentItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getDurationPlayerItems() {
		return durationItem;
	}
	public ConfigurableItem getPermanentItem() {
		return permanentItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
