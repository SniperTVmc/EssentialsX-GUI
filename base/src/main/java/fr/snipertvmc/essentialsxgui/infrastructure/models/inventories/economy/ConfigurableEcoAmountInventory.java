package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

public class ConfigurableEcoAmountInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final Set<ConfigurableItem> addItems;
	private final Set<ConfigurableItem> removeItems;

	private final ConfigurableItem confirmActionItem;
	private final ConfigurableItem cancelActionItem;


	// -------------------------------------------------- //


	public ConfigurableEcoAmountInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.addItems = inventoryFile.getItems(config.getConfigurationSection(name + ".items.addItems"));
		this.removeItems = inventoryFile.getItems(config.getConfigurationSection(name + ".items.removeItems"));

		this.confirmActionItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.confirmActionItem"));
		this.cancelActionItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelActionItem"));
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getAddItems() {
		return addItems;
	}
	public Set<ConfigurableItem> getRemoveItems() {
		return removeItems;
	}

	public ConfigurableItem getConfirmActionItem() {
		return confirmActionItem;
	}
	public ConfigurableItem getCancelActionItem() {
		return cancelActionItem;
	}


	// -------------------------------------------------- //
}
