package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

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

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.playerIdentificationItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerIdentificationItem"));
		this.playerStatisticsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerStatisticsItem"));
		this.playerWorldItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerWorldItem"));
		this.playerServerDataItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerServerDataItem"));
		this.playerPunishmentsItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerPunishmentsItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));

		this.onlyUsePlaceholderAPI = config.getBoolean("onlyUsePlaceholderAPI");
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
