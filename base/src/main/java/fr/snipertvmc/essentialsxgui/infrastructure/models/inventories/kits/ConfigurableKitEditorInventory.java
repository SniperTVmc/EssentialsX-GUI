package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits;

import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

public class ConfigurableKitEditorInventory extends ConfigurableInventory {


	// -------------------------------------------------- //


	private final ConfigurableItem saveKitItem;
	private final ConfigurableItem cancelChangesItem;

	private final Set<Integer> borderSlots;


	// -------------------------------------------------- //


	public ConfigurableKitEditorInventory(InventoryFile inventoryFile) {
		super(inventoryFile);

		String name = inventoryFile.getFileName();
		YamlConfiguration config = inventoryFile.getYamlConfiguration();


		// Items
		this.saveKitItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.saveKitItem"));
		this.cancelChangesItem = new ConfigurableItem(config.getConfigurationSection(name + ".items.cancelChangesItem"));

		this.borderSlots = inventoryFile.getBorderSlots();
	}


	// -------------------------------------------------- //


	public ConfigurableItem getSaveKitItem() {
		return saveKitItem;
	}
	public ConfigurableItem getCancelChangesItem() {
		return cancelChangesItem;
	}

	public Set<Integer> getBorderSlots() {
		return borderSlots;
	}


	// -------------------------------------------------- //
}
