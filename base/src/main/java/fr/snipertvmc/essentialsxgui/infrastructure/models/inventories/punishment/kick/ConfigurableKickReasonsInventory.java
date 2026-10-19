package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGKick;

import java.util.Set;

public class ConfigurableKickReasonsInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final Set<EXGKick> kickReasons;

	private final ConfigurableItem reasonItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableKickReasonsInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.kickReasons = inventoryFile.getKickReasons();

		this.reasonItem = inventoryFile.getItem("reasonItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public Set<EXGKick> getKickReasons() {
		return kickReasons;
	}

	public ConfigurableItem getReasonItem() {
		return reasonItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
