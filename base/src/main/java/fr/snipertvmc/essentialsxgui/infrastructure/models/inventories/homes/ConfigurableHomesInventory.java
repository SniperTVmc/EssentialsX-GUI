package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableHomesInventory extends ConfigurablePaginatedInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem homeItem;
	private final ConfigurableItem bedHomeItem;
	private final ConfigurableItem noHomesItem;

	private final ConfigurableItem createHomeItem;
	private final ConfigurableItem searchHomeItem;
	private final ConfigurableItem cancelSearchHomeItem;
	private final ConfigurableItem noSearchHomeResultsItem;

	private final ConfigurableItem closeItem;

	private final Pair<String, String> bedHomeItemOverworld;
	private final Pair<String, String> bedHomeItemNether;
	private final Pair<String, String> bedHomeItemNotSet;


	// -------------------------------------------------- //


	public ConfigurableHomesInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.homeItem = inventoryFile.getItem("homeItem");
		this.bedHomeItem = inventoryFile.getItem("bedHomeItem");
		this.noHomesItem = inventoryFile.getItem("noHomesItem");

		this.createHomeItem = inventoryFile.getItem("createHomeItem");
		this.searchHomeItem = inventoryFile.getItem("searchHomeItem");
		this.cancelSearchHomeItem = inventoryFile.getItem("cancelSearchHomeItem");
		this.noSearchHomeResultsItem = inventoryFile.getItem("noSearchHomeResultsItem");

		this.closeItem = inventoryFile.getItem("closeItem");


		// Special settings
		this.bedHomeItemNotSet = Pair.of(
				config.getString("bedHomeItem.notSet.material", XMaterial.BARRIER.name()),
				config.getString("bedHomeItem.notSet.displayName", "&cNot set")
		);
		this.bedHomeItemOverworld = Pair.of(
				config.getString("bedHomeItem.overworld.material", XMaterial.RED_BED.name()),
				config.getString("bedHomeItem.overworld.displayName", "&aOverworld")
		);
		this.bedHomeItemNether = Pair.of(
				config.getString("bedHomeItem.nether.material", XMaterial.RESPAWN_ANCHOR.name()),
				config.getString("bedHomeItem.nether.displayName", "&5Nether")
		);
	}


	// -------------------------------------------------- //


	public ConfigurableItem getHomeItem() {
		return homeItem;
	}
	public ConfigurableItem getBedHomeItem() {
		return bedHomeItem;
	}
	public ConfigurableItem getNoHomesItem() {
		return noHomesItem;
	}

	public ConfigurableItem getCreateHomeItem() {
		return createHomeItem;
	}
	public ConfigurableItem getSearchHomeItem() {
		return searchHomeItem;
	}
	public ConfigurableItem getCancelSearchHomeItem() {
		return cancelSearchHomeItem;
	}
	public ConfigurableItem getNoSearchHomeResultsItem() {
		return noSearchHomeResultsItem;
	}

	public ConfigurableItem getCloseItem() {
		return closeItem;
	}

	public String getBedHomeItemNotSetMaterial() {
		return bedHomeItemNotSet.getLeft();
	}
	public String getBedHomeItemNotSetDisplayName() {
		return bedHomeItemNotSet.getRight();
	}

	public String getBedHomeItemOverworldMaterial() {
		return bedHomeItemOverworld.getLeft();
	}
	public String getBedHomeItemOverworldDisplayName() {
		return bedHomeItemOverworld.getRight();
	}

	public String getBedHomeItemNetherMaterial() {
		return bedHomeItemNether.getLeft();
	}
	public String getBedHomeItemNetherDisplayName() {
		return bedHomeItemNether.getRight();
	}


	// -------------------------------------------------- //
}
