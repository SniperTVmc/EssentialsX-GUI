package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

public class ConfigurableBalanceTopInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem playerRankingItem;
	private final ConfigurableItem forceUpdateItem;

	private final ConfigurableItem closeItem;

	private final Set<ConfigurableItem> rankingItems;

	private final Pair<Integer, Integer> rankingRange;


	// -------------------------------------------------- //


	public ConfigurableBalanceTopInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.playerRankingItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerRankingItem"));
		this.forceUpdateItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.forceUpdateItem"));

		this.closeItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.closeItem"));

		this.rankingItems = inventoryFile.getSlotsItems(config.getConfigurationSection("rankingItems"));

		this.rankingRange = getRankingRange(config);
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerRankingItem() {
		return playerRankingItem;
	}
	public ConfigurableItem getForceUpdateItem() {
		return forceUpdateItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}

	public Set<ConfigurableItem> getRankingItems() {
		return rankingItems;
	}

	public Pair<Integer, Integer> getRankingRange() {
		return rankingRange;
	}


	// -------------------------------------------------- //


	private Pair<Integer, Integer> getRankingRange(YamlConfiguration config) {
		String range = config.getString("rankingRange", "1-10");
		String[] rangeSplit = range.split("-");
		return Pair.of(Integer.parseInt(rangeSplit[0]), Integer.parseInt(rangeSplit[1]));
	}


	// -------------------------------------------------- //
}
