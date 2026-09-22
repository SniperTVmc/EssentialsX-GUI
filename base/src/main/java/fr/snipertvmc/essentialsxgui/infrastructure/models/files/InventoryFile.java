package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InventoryFile extends BaseFile {


	// -------------------------------------------------- //


	public InventoryFile(YamlConfiguration yamlConfiguration, String filePath) {
		super(yamlConfiguration, filePath);
	}


	// -------------------------------------------------- //


	public String getTitle() {
		return getYamlConfiguration().getString(getFileName() + ".title", "");
	}


	public int getRows() {
		return getYamlConfiguration().getInt(getFileName() + ".rows", 1);
	}


	public InventoryScheme getInventoryScheme() {
		List<String> scheme = getYamlConfiguration().getStringList(getFileName() + ".inventoryScheme");
		InventoryScheme inventoryScheme = new InventoryScheme();
		for (String line : scheme) {
			inventoryScheme.mask(line);
		}
		return inventoryScheme.bindPagination('1');
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getBorderItems() {

		Set<ConfigurableItem> borderItems = new HashSet<>();

		String itemPath = getFileName() + ".borderItem";
		ConfigurationSection borderSection = getYamlConfiguration().getConfigurationSection(itemPath);
		if (borderSection == null) return borderItems;

		ConfigurableItem borderItem = new ConfigurableItem(borderSection, itemPath);
		if (!borderItem.isEnabled()) return borderItems;

		List<Integer> borderSlots = getYamlConfiguration().getIntegerList(itemPath + ".slot");
		borderSlots.forEach(slot -> {
			ConfigurableItem borderItemCopy = new ConfigurableItem(borderItem);
			borderItemCopy.setSlot(slot);
			borderItems.add(borderItemCopy);
		});

		return borderItems;
	}


	public Set<Integer> getBorderSlots() {
		return getYamlConfiguration().getIntegerList(getFileName() + ".borderItem.slot").stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
	}


	// -------------------------------------------------- //


	public ConfigurableItem getItem(String itemName) {
		return getItem(itemName, true);
	}
	public ConfigurableItem getItem(String itemPath, boolean includeItemPath) {
		if (includeItemPath) itemPath = getFileName() + ".items." + itemPath;
		ConfigurationSection itemSection = getYamlConfiguration().getConfigurationSection(itemPath);
		return new ConfigurableItem(itemSection, itemPath);
	}


	public Set<ConfigurableItem> getItems(String itemName) {
		return getItems(itemName, true);
	}
	public Set<ConfigurableItem> getItems(String itemPath, boolean includeItemPath) {
		if (includeItemPath) itemPath = getFileName() + ".items." + itemPath;
		ConfigurationSection itemSection = getYamlConfiguration().getConfigurationSection(itemPath);
		if (itemSection == null) return new HashSet<>();
		int count = itemSection.getIntegerList("slot").size();
		Set<ConfigurableItem> items = new HashSet<>();

		for (int i = 0; i < count; i++) {
			ConfigurableItem itemConfig = new ConfigurableItem(itemSection, itemPath, i);
			items.add(itemConfig);
		}

		return items;
	}


	// -------------------------------------------------- //


	public ConfigurableItem getNextPageItem() {
		return getItem("nextPageItem");
	}
	public ConfigurableItem getPreviousPageItem() {
		return getItem("previousPageItem");
	}
	public ConfigurableItem getCurrentPageItem() {
		return getItem("currentPageItem");
	}


	// -------------------------------------------------- //


	public Set<ConfigurableItem> getSlotsItems(String slotsItemsPath) {

		ConfigurationSection itemSection = getYamlConfiguration().getConfigurationSection(slotsItemsPath);
		if (itemSection == null) return new HashSet<>();
		Set<ConfigurableItem> slotsItems = new HashSet<>();

		for (String key : itemSection.getKeys(false)) {
			ConfigurableItem itemConfig = getItem(slotsItemsPath + "." + key, false);

			// Range slots
			if (key.contains("-")) {

				int startRange;
				int endRange;

				try {
					startRange = Integer.parseInt(key.split("-")[0]);
					endRange = Integer.parseInt(key.split("-")[1]);

				} catch (NumberFormatException e) {
					ConsoleLogger.error(
							"Invalid slot range format for item '" + key + "' in inventory '" + getFileName() + "'." +
							"Expected format: 'start-end' (e.g., '0-8'). Skipping this item.");
					continue;
				}

				for (int i = startRange; i <= endRange; i++) {
					itemConfig.setSlot(i);
					slotsItems.add(itemConfig);
				}
				continue;
			}


			// Specific slot
			int slot;

			try {
				slot = Integer.parseInt(key.split("-")[0]);

			} catch (NumberFormatException e) {
				ConsoleLogger.error(
						"Invalid slot format for item '" + key + "' in inventory '" + getFileName() + "'." +
						"Expected a number or a range (e.g., '0' or '0-8'). Skipping this item.");
				continue;
			}

			itemConfig.setSlot(slot);
			slotsItems.add(itemConfig);
		}

		return slotsItems;
	}


	// -------------------------------------------------- //
}
