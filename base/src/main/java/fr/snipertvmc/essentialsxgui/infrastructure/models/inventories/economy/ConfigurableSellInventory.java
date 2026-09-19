package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

public class ConfigurableSellInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem confirmSellItem;
	private final ConfigurableItem cancelSellItem;

	private final Set<Integer> borderSlots;


	// -------------------------------------------------- //


	public ConfigurableSellInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.confirmSellItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.confirmSellItem"));
		this.cancelSellItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelSellItem"));

		this.borderSlots = inventoryFile.getBorderSlots();
	}


	// -------------------------------------------------- //


	public ConfigurableItem getConfirmSellItem() {
		return confirmSellItem;
	}
	public ConfigurableItem getCancelSellItem() {
		return cancelSellItem;
	}

	public Set<Integer> getBorderSlots() {
		return borderSlots;
	}


	// -------------------------------------------------- //
}
