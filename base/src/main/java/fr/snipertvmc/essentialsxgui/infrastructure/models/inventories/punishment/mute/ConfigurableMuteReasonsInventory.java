package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;

import java.util.Set;

public class ConfigurableMuteReasonsInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final Set<EXGPunishment> muteReasons;

	private final ConfigurableItem reasonItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableMuteReasonsInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.muteReasons = inventoryFile.getPunishmentReasons();

		this.reasonItem = inventoryFile.getItem("reasonItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public Set<EXGPunishment> getMuteReasons() {
		return muteReasons;
	}

	public ConfigurableItem getReasonItem() {
		return reasonItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
