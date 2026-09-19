package fr.snipertvmc.essentialsxgui.utilities.config;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EXGItemConfigParser {


	// -------------------------------------------------- //


	private static final Map<String, List<String>> ignoredPaths = new HashMap<>() {{

		put("slot", List.of(

				// HOMES
				"homes.items.homeItem",
				"homes.items.noHomesItem",
				"homes.items.noSearchHomeResultsItem",

				// KITS
				"kitPreview.items.emptyKitItem",
				"kitsAdminView.items.kitItem",
				"kitsAdminView.items.noKitsItem",
				"kitsAdminView.items.noSearchKitResultsItem",
				"kitsPlayerView.items.kitItem",
				"kitsPlayerView.items.noKitsItem",
				"kitsPlayerView.items.noSearchKitResultsItem",
				"kitPlayerGive.items.playerItem",

				// WARPS
				"warpsAdminView.items.warpItem",
				"warpsAdminView.items.noWarpsItem",
				"warpsAdminView.items.noSearchWarpResultsItem",
				"warpsPlayerView.items.warpItem",
				"warpsPlayerView.items.noWarpsItem",
				"warpsPlayerView.items.noSearchWarpResultsItem",
				"warpPlayerTeleport.items.playerItem",

				// WHOIS
				"whoisPlayers.items.playerItem",

				// ECONOMY
				"rankingItems",
				"worthAll.items.worthItem",
				"worthAll.items.noWorthItem",
				"worthAll.items.noSearchWorthResultsItem",
				"worthInventory.items.emptyInventoryItem",
				"ecoAmount.items.addItem",
				"ecoAmount.items.removeItem",
				"ecoPlayers.items.playerItem",

				// OTHERS
				"dataEntryGUI.items.materialIconItem"
		));

		put("material", List.of(

				// HOMES
				"homeEditing.items.previewHomeItem",

				// KITS
				"kitEditing.items.previewKitItem",

				// WARPS
				"warpEditing.items.previewWarpItem",

				// ECONOMY
				"worthAll.items.worthItem",
				"worthInventory.items.worthItem",

				// OTHERS
				"dataEntryGUI.items.materialIconItem"
		));
	}};


	// -------------------------------------------------- //


//	public static boolean isEXGItemConfigValid(InventoryFile inventoryFile, String itemPath, boolean multipleSlots) {
//
//
//		// Get the inventory configuration
//		YamlConfiguration config = inventoryFile.getYamlConfiguration();
//		String inventoryName = Main.getInstance().getFilesManager().getInventoryName(inventoryFile);
//
//
//		// Retrieve item properties
//		Object slots = config.get(itemPath + ".slots");
//		Object slot = config.get(itemPath + ".slot");
//
//		Object material = config.get(itemPath + ".material");
//		Object data = config.get(itemPath + ".data");
//		Object amount = config.get(itemPath + ".amount");
//
//		Object displayName = config.get(itemPath + ".displayName");
//		Object lore = config.get(itemPath + ".lore");
//
//		Object itemFlags = config.get(itemPath + ".itemFlags");
//		Object enchantments = config.get(itemPath + ".enchantments");
//
//
//		// Validate item properties
//		return isSlotValid(slot, slots, itemPath, inventoryName, multipleSlots) &&
//
//				isMaterialValid(material, itemPath) &&
//				isDataValid(data, itemPath) &&
//				isAmountValid(amount, itemPath) &&
//
//				isDisplayNameValid(displayName, itemPath) &&
//				isLoreValid(lore, itemPath) &&
//
//				areEnchantmentsValid(enchantments, itemPath) &&
//				areItemFlagsValid(itemFlags, itemPath) &&
//
//				EXGItemCustomConfigParser.isEXGItemCustomConfigValid(inventoryFile, itemPath);
//	}


	// -------------------------------------------------- //


	private static boolean isSlotValid(Object slot, Object slots, String itemPath, String inventoryName, boolean multipleSlots) {

		Object slotObject = (multipleSlots ? slots : slot);
		String slotDisplayName = (multipleSlots ? "slots" : "slot");
		Class<?> slotType = (multipleSlots ? List.class : Integer.class);

		if (isIgnored(itemPath, (multipleSlots ? "slots" : "slot"))) {
			return true;
		}

		if (!isMissing(slotObject, itemPath, slotDisplayName, true) &&
				isNotTypeRequired(slotObject, itemPath, slotDisplayName, slotType)) {
			return false;
		}

		if (multipleSlots) {

			List<Integer> slotList = (List<Integer>) slots;
			if (slotList.isEmpty()) {
				ConsoleLogger.error("Invalid slots for item '" + itemPath + "': slots list cannot be empty.");
				return false;
			}

		} else {

			int slotValue = ((Number) slot).intValue();

			InventoryFile inventoryFile = Main.getInstance().getFilesManager().getInventoryFile(EXGInventory.getByName(inventoryName));
			int rows = inventoryFile.getRows();
			int maxSlots = (rows * 9) - 1;

			if (slotValue < 0 || slotValue > maxSlots) {
				ConsoleLogger.error("Invalid slot for item '" + itemPath + "': it must be between 0 and " + maxSlots + ".");
				return false;
			}
		}

		return true;
	}


	private static boolean isMaterialValid(Object material, String itemPath) {

		if (isIgnored(itemPath, "material")) {
			return true;
		}

		if (isMissing(material, itemPath, "material", false) ||
				isNotTypeRequired(material, itemPath, "material", String.class)) {
			return false;
		}

		if ((material.toString().startsWith("{") && material.toString().endsWith("}")) ||
				material.toString().startsWith("PLAYER_HEAD:")) {
			return true; // Variable or custom item, no need to validate Material
		}

		if (XMaterial.matchXMaterial(material.toString()).isEmpty()) {
			ConsoleLogger.error("Invalid material for item '" + itemPath + "': '" + material + "' is not valid.");
			return false;
		}

		return true;
	}


	private static boolean isDataValid(Object data, String itemPath) {

		if (VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT"))) {
			return true; // Data is not used in versions > 1.12.2
		}

		return isMissing(data, itemPath, "data", true) ||
				!isNotTypeRequired(data, itemPath, "data", Number.class);
	}


	private static boolean isAmountValid(Object amount, String itemPath) {

		return isMissing(amount, itemPath, "amount", true) ||
				!isNotTypeRequired(amount, itemPath, "amount", Number.class);
	}


	private static boolean isDisplayNameValid(Object displayName, String itemPath) {

		return isMissing(displayName, itemPath, "displayName", true) ||
				!isNotTypeRequired(displayName, itemPath, "displayName", String.class);
	}


	private static boolean isLoreValid(Object lore, String itemPath) {

		if (isMissing(lore, itemPath, "lore", true))  {
			return true;
		}

		if (!(lore instanceof List) || ((List<?>) lore).stream().anyMatch(line -> !(line instanceof String))) {
			ConsoleLogger.error("Invalid lore for item '" + itemPath + "': lore must be a list of strings.");
			return false;
		}

		return true;
	}


	private static boolean areEnchantmentsValid(Object enchantments, String itemPath) {

		if (isMissing(enchantments, itemPath, "enchantments", true))  {
			return true;
		}

		if (!(enchantments instanceof List) || ((List<?>) enchantments).stream().anyMatch(enchantment -> !(enchantment instanceof String))) {
			ConsoleLogger.error("Invalid enchantments list for item '" + itemPath + "': enchantments list must contain strings.");
			return false;
		}

		boolean allValid = true;
		for (Object enchantment : (List<?>) enchantments) {

			String[] parts = enchantment.toString().split(":");
			if (parts.length != 2) {
				ConsoleLogger.error("Invalid enchantment format for item '" + itemPath + "': " + enchantment + " is not in the format 'EnchantmentName:Level'.");
				allValid = false;
				continue;
			}

			try {
				Integer.parseInt(parts[1]);

				if (XEnchantment.of(parts[0]).isEmpty()) {
					ConsoleLogger.error("Invalid enchantment name for item '" + itemPath + "': '" + parts[0] + "' is not a valid enchantment.");
					allValid = false;
				}

			} catch (NumberFormatException ex) {
				ConsoleLogger.error("Invalid enchantment level for item '" + itemPath + "': '" + parts[1] + "' is not a valid integer.");
				allValid = false;
			}
		}

		return allValid;
	}


	private static boolean areItemFlagsValid(Object itemFlags, String itemPath) {

		if (isMissing(itemFlags, itemPath, "itemFlags", true))  {
			return true;
		}

		if (!(itemFlags instanceof List) || ((List<?>) itemFlags).stream().anyMatch(itemFlag -> !(itemFlag instanceof String))) {
			ConsoleLogger.error("Invalid item flags list for item '" + itemPath + "': list must contain strings.");
			return false;
		}

		boolean allValid = true;
		for (Object itemFlag : (List<?>) itemFlags) {

			if (XItemFlag.of(itemFlag.toString()).isEmpty()) {
				ConsoleLogger.error("Invalid item flag for item '" + itemPath + "': '" + itemFlag + "' is not a valid item flag.");
				allValid = false;
			}
		}

		return allValid;
	}


	// -------------------------------------------------- //


	private static boolean isIgnored(String itemPath, String propertyName) {
		if (!ignoredPaths.containsKey(propertyName)) return false;
		return ignoredPaths.get(propertyName).stream().anyMatch(ignoredPath -> itemPath.equals(ignoredPath) || itemPath.startsWith(ignoredPath + "."));
	}


	private static boolean isMissing(Object value, String itemPath, String propertyName, boolean silence) {
		if (value == null) {
			if (!silence) {
				ConsoleLogger.error("Invalid value for item '" + itemPath + "': '" + propertyName + "' is missing.");
			}
			return true;
		}
		return false;
	}


	private static boolean isNotTypeRequired(Object value, String itemPath, String propertyName, Class<?> requiredType) {
		if (!requiredType.isInstance(value)) {
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': '" + value + "' is not a " + requiredType.getSimpleName() + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //
}
