package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;

import java.util.Set;

public class ConfigurableBanReasonsInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final Set<EXGPunishment> banReasons;

	private final ConfigurableItem reasonPlayerItem;
	private final ConfigurableItem reasonIpItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableBanReasonsInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.banReasons = inventoryFile.getPunishmentReasons();

		this.reasonPlayerItem = inventoryFile.getItem("reasonPlayerItem");
		this.reasonIpItem = inventoryFile.getItem("reasonIPItem");

		this.backItem = inventoryFile.getItem("backItem");
	}


	// -------------------------------------------------- //


	public Set<EXGPunishment> getBanReasons() {
		return banReasons;
	}

	public ConfigurableItem getReasonPlayerItem() {
		return reasonPlayerItem;
	}
	public ConfigurableItem getReasonIPItem() {
		return reasonIpItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
