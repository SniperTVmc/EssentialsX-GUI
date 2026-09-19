package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurableEcoActionInventory extends ConfigurableInventory {


	// -------------------------------------------------- //

	private final ConfigurableItem playerItem;

	private final ConfigurableItem addBalanceItem;
	private final ConfigurableItem takeBalanceItem;
	private final ConfigurableItem setBalanceItem;
	private final ConfigurableItem resetBalanceItem;

	private final ConfigurableItem backItem;


	// -------------------------------------------------- //


	public ConfigurableEcoActionInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.playerItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.playerItem"));

		this.addBalanceItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.addBalanceItem"));
		this.takeBalanceItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.takeBalanceItem"));
		this.setBalanceItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.setBalanceItem"));
		this.resetBalanceItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.resetBalanceItem"));

		this.backItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.backItem"));
	}


	// -------------------------------------------------- //


	public ConfigurableItem getPlayerItem() {
		return playerItem;
	}

	public ConfigurableItem getAddBalanceItem() {
		return addBalanceItem;
	}
	public ConfigurableItem getTakeBalanceItem() {
		return takeBalanceItem;
	}
	public ConfigurableItem getSetBalanceItem() {
		return setBalanceItem;
	}
	public ConfigurableItem getResetBalanceItem() {
		return resetBalanceItem;
	}

	public ConfigurableItem getBackItem() {
		return backItem;
	}


	// -------------------------------------------------- //
}
