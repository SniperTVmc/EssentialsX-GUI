package fr.snipertvmc.essentialsxgui.utilities.parsers.inventories;

import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.items.ConfigurableExtraParser;
import fr.snipertvmc.essentialsxgui.utilities.parsers.items.ItemPropertyParser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CustomConfigurableInventoryParser {


	// -------------------------------------------------- //


	public static int isCustomConfigurableInventoryValid(InventoryFile inventoryFile, boolean silence) {
		int errorsCount = 0;
		if (!customConfigurations.contains(inventoryFile.getFileName())) return 0;


		// Get the inventory configuration
		YamlConfiguration yamlConfiguration = inventoryFile.getYamlConfiguration();
		int rows = inventoryFile.getRows();


		// Validate custom properties
		if (inventoryFile.getFileName().equals("homes")) {
			errorsCount += areBedHomeIconsValid(yamlConfiguration.getConfigurationSection("bedHomeIcons"), inventoryFile.getFileName(), silence);
		}
		if (inventoryFile.getFileName().equals("whoisView")) {
			if (!isOnlyUsePlaceholderAPIValid(yamlConfiguration.get("onlyUsePlaceholderAPI"), inventoryFile.getFileName(), silence)) errorsCount++;
		}
		if (inventoryFile.getFileName().equals("balanceTop")) {
			errorsCount += areRankingItemsValid(yamlConfiguration.getConfigurationSection("rankingItems"), inventoryFile.getFileName(), rows, true);
			if (!isRankingRangeValid(yamlConfiguration.get("rankingRange"), inventoryFile.getFileName(), silence)) errorsCount++;
		}
		return errorsCount;
	}


	// -------------------------------------------------- //


	public static int areBedHomeIconsValid(ConfigurationSection bedHomeIconSection, String inventoryName, boolean silence) {
		int errorsCount = 0;


		// Get the item configuration
		if (!InventoryPropertyParser.isSectionValid(bedHomeIconSection, inventoryName, "bedHomeIcon", silence)) return 1;


		// Get the icons section
		ConfigurationSection iconNotSetSection = bedHomeIconSection.getConfigurationSection("notSet");
		ConfigurationSection iconOverworldSection = bedHomeIconSection.getConfigurationSection("overworld");
		ConfigurationSection iconNetherSection = bedHomeIconSection.getConfigurationSection("nether");


		// Validate the icons
		errorsCount += isBedHomeIconValid(iconNotSetSection, "notSet", inventoryName, silence);
		errorsCount += isBedHomeIconValid(iconOverworldSection, "overworld", inventoryName, silence);
		if (VersionUtil.getServerBukkitVersion().isHigherThanOrEqualTo(VersionUtil.v1_16_1_R01)) {
			errorsCount += isBedHomeIconValid(iconNetherSection, "nether", inventoryName, silence);
		}
		return errorsCount;
	}


	public static int isBedHomeIconValid(ConfigurationSection bedHomeIconSection, String iconKey, String inventoryName, boolean silence) {
		int errorsCount = 0;


		// Get the item configuration
		if (!InventoryPropertyParser.isSectionValid(bedHomeIconSection, inventoryName, "bedHomeIcons." + iconKey, silence)) return 1;


		// Validate item properties
		errorsCount += ItemPropertyParser.isMaterialValid(bedHomeIconSection.get("material"), "bedHomeIcons." + iconKey, silence) ? 0 : 1;
		errorsCount += ItemPropertyParser.isDataValid(bedHomeIconSection.get("data"), "bedHomeIcons." + iconKey, silence) ? 0 : 1;
		errorsCount += InventoryPropertyParser.isStringValid(bedHomeIconSection.get("worldDisplayName"), inventoryName, "bedHomeIcons." + iconKey + ".worldDisplayName", silence) ? 0 : 1;
		return errorsCount;
	}


	// -------------------------------------------------- //


	public static boolean isOnlyUsePlaceholderAPIValid(Object onlyUsePlaceholderAPI, String inventoryName, boolean silence) {
		if (!InventoryPropertyParser.isBooleanValid(onlyUsePlaceholderAPI, inventoryName, "onlyUsePlaceholderAPI", silence)) return false;
		return true;
	}


	// -------------------------------------------------- //


	public static int areRankingItemsValid(ConfigurationSection rankingItemsSection, String inventoryName, int rows, boolean silence) {
		int errorsCount = 0;
		if (!InventoryPropertyParser.isSectionValid(rankingItemsSection, inventoryName, "rankingItems", silence)) return 1;
		for (String itemKey : rankingItemsSection.getKeys(false)) {
			ConfigurationSection rankingItemSection = rankingItemsSection.getConfigurationSection(itemKey);
			errorsCount += isRankingItemValid(rankingItemSection, itemKey, inventoryName, rows, silence);
		}
		return errorsCount;
	}


	public static int isRankingItemValid(ConfigurationSection rankingItemSection, String itemKey, String inventoryName, int rows, boolean silence) {
		int errorsCount = 0;

		// Get the item configuration
		if (!InventoryPropertyParser.isSectionValid(rankingItemSection, inventoryName, "rankingItems." + itemKey, silence)) return 1;


		// Retrieve item properties
		Object slot = itemKey;
		try {
			slot = Integer.parseInt(itemKey);
		} catch (NumberFormatException ignored) {
		}

		Object material = rankingItemSection.get("material");
		Object amount = rankingItemSection.get("amount");
		Object data = rankingItemSection.get("data");

		Object displayName = rankingItemSection.get("displayName");
		Object lore = rankingItemSection.get("lore");

		Object enchantments = rankingItemSection.get("enchantments");
		Object itemFlags = rankingItemSection.get("itemFlags");


		// Validate item properties
		String itemPath = "rankingItems." + itemKey;
		if (!ItemPropertyParser.isSlotValid(slot, inventoryName, rows, silence)) errorsCount++;
		if (!ItemPropertyParser.isMaterialValid(material, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isDataValid(data, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isAmountValid(amount, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isDisplayNameValid(displayName, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.isLoreValid(lore, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.areEnchantmentsValid(enchantments, itemPath, silence)) errorsCount++;
		if (!ItemPropertyParser.areItemFlagsValid(itemFlags, itemPath, silence)) errorsCount++;
		errorsCount += ConfigurableExtraParser.isConfigurableExtraValid(rankingItemSection, itemPath, silence);
		return errorsCount;
	}


	public static boolean isRankingRangeValid(Object range, String inventoryName, boolean silence) {
		if (!InventoryPropertyParser.isStringValid(range, inventoryName, "rankingRange", silence)) return false;
		if (!range.toString().matches("\\d+-\\d+")) {
			if (!silence) ConsoleLogger.error("Invalid rankingRange for inventory '" + inventoryName + "': '" + range + "' is not a valid range (e.g., '1-10').");
			return false;
		}
		return true;
	}


	// -------------------------------------------------- //


	private static final List<String> customConfigurations = new ArrayList<>() {{
		add("homes");
		add("whoisView");
		add("balanceTop");
	}};



	// -------------------------------------------------- //
}
