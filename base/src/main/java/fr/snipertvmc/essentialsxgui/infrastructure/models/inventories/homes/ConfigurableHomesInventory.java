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

	private final Pair<String, String> bedHomeIconOverworld;
	private final Pair<String, String> bedHomeIconNether;
	private final Pair<String, String> bedHomeIconNotSet;


	// -------------------------------------------------- //


	public ConfigurableHomesInventory(InventoryFile inventoryFile) {
		super(inventoryFile);


		// Get the configuration
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
		this.bedHomeIconNotSet = Pair.of(
				config.getString("bedHomeIcons.notSet.material", XMaterial.BARRIER.name()),
				config.getString("bedHomeIcons.notSet.displayName", "&cNot set")
		);
		this.bedHomeIconOverworld = Pair.of(
				config.getString("bedHomeIcons.overworld.material", XMaterial.RED_BED.name()),
				config.getString("bedHomeIcons.overworld.displayName", "&aOverworld")
		);
		this.bedHomeIconNether = Pair.of(
				config.getString("bedHomeIcons.nether.material", XMaterial.RESPAWN_ANCHOR.name()),
				config.getString("bedHomeIcons.nether.displayName", "&5Nether")
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

	public String getBedHomeIconNotSetMaterial() {
		return bedHomeIconNotSet.getLeft();
	}
	public String getBedHomeIconNotSetDisplayName() {
		return bedHomeIconNotSet.getRight();
	}

	public String getBedHomeIconOverworldMaterial() {
		return bedHomeIconOverworld.getLeft();
	}
	public String getBedHomeIconOverworldDisplayName() {
		return bedHomeIconOverworld.getRight();
	}

	public String getBedHomeIconNetherMaterial() {
		return bedHomeIconNether.getLeft();
	}
	public String getBedHomeIconNetherDisplayName() {
		return bedHomeIconNether.getRight();
	}


	// -------------------------------------------------- //
}
