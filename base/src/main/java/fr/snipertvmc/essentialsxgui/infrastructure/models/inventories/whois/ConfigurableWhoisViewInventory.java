package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;

public class ConfigurableWhoisViewInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerIdentificationItem;
	private final ConfigurableItem playerStatisticsItem;
	private final ConfigurableItem playerWorldItem;
	private final ConfigurableItem playerServerDataItem;
	private final ConfigurableItem playerPunishmentsItem;

	private final ConfigurableItem backItem;

	private final boolean onlyUsePlaceholderAPI;


	// -------------------------------------------------- //


	public ConfigurableWhoisViewInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Items
		this.playerIdentificationItem = inventoryFile.getItem("playerIdentificationItem");
		this.playerStatisticsItem = inventoryFile.getItem("playerStatisticsItem");
		this.playerWorldItem = inventoryFile.getItem("playerWorldItem");
		this.playerServerDataItem = inventoryFile.getItem("playerServerDataItem");
		this.playerPunishmentsItem = inventoryFile.getItem("playerPunishmentsItem");

		this.backItem = inventoryFile.getItem("backItem");

		this.onlyUsePlaceholderAPI = inventoryFile.getYamlConfiguration().getBoolean("onlyUsePlaceholderAPI");
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerIdentificationItem() {
		return playerIdentificationItem;
	}
	public ConfigurableItem getPlayerStatisticsItem() {
		return playerStatisticsItem;
	}
	public ConfigurableItem getPlayerWorldItem() {
		return playerWorldItem;
	}
	public ConfigurableItem getPlayerServerDataItem() {
		return playerServerDataItem;
	}
	public ConfigurableItem getPlayerPunishmentsItem() {
		return playerPunishmentsItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}

	public boolean isOnlyUsePlaceholderAPI() {
		return onlyUsePlaceholderAPI;
	}


	// -------------------------------------------------- //
}
