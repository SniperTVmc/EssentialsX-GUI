package fr.snipertvmc.essentialsxgui.utilities.parsers.items;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPropertyParser {


	// -------------------------------------------------- //


	protected static boolean isEnabled(Object enabled, String itemPath) {
		if (isIgnored(itemPath, "enabled")) return true;
		if (isMissing(enabled, itemPath, "enabled", false)) return false;
		if (isNotTypeRequired(enabled, itemPath, "enabled", Boolean.class)) return false;
		return true;
	}


	protected static boolean isSlotValid(Object slot, String itemPath, int rows) {
		if (isIgnored(itemPath, "slot")) return true;
		if (isMissing(slot, itemPath, "slot", false)) return false;
		if (isNotTypeRequired(slot, itemPath, "slot", Number.class, List.class)) return false;
		if (slot instanceof List<?>) {
			if (((List<?>) slot).stream().anyMatch(s -> !(s instanceof Number))) {
				ConsoleLogger.error("Invalid slots for item '" + itemPath + "': slots list must contain integers.");
				return false;
			}
			List<Integer> slotList = (List<Integer>) slot;
			if (slotList.isEmpty()) {
				ConsoleLogger.error("Invalid slots for item '" + itemPath + "': slots list cannot be empty.");
				return false;
			}
			return isSlotInValidRange(itemPath, rows, slotList.stream().mapToInt(Integer::intValue).toArray());
		}
		int slotValue = ((Number) slot).intValue();
		return isSlotInValidRange(itemPath, rows, slotValue);
	}


	private static boolean isSlotInValidRange(String itemPath, int rows, int... slotValue) {
		int maxSlots = (rows * 9) - 1;
		for (int slot : slotValue) {
			if (slot < 0 || slot > maxSlots) {
				ConsoleLogger.error("Invalid slot for item '" + itemPath + "': it must be between 0 and " + maxSlots + ".");
				return false;
			}
		}
		return true;
	}


	protected static boolean isMaterialValid(Object material, String itemPath) {
		boolean isRequired = isRequired(itemPath, "material");
		if (isMissing(material, itemPath, "material", !isRequired)) return !isRequired;
		if (isNotTypeRequired(material, itemPath, "material", String.class)) return false;
		if (material.toString().startsWith("{") && material.toString().endsWith("}")) return true;
		if (XMaterial.matchXMaterial(material.toString()).isEmpty()) {
			ConsoleLogger.error("Invalid material for item '" + itemPath + "': '" + material + "' is not valid.");
			return false;
		}
		return true;
	}


	protected static boolean isDataValid(Object data, String itemPath) {
		// Data is not used in versions > 1.12.2
		if (VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.v1_12_2_R01)) return true;
		boolean isRequired = isRequired(itemPath, "data");
		if (isMissing(data, itemPath, "data", !isRequired)) return !isRequired;
		if (isNotTypeRequired(data, itemPath, "data", Number.class)) return false;
		if (data instanceof Number dataValue) {
			if (dataValue.intValue() < 0 || dataValue.intValue() > 15) {
				ConsoleLogger.error("Invalid data for item '" + itemPath + "': it must be between 0 and 15.");
				return false;
			}
		}
		return true;
	}


	protected static boolean isAmountValid(Object amount, String itemPath) {
		boolean isRequired = isRequired(itemPath, "amount");
		if (isMissing(amount, itemPath, "amount", !isRequired)) return !isRequired;
		if (isNotTypeRequired(amount, itemPath, "amount", Number.class)) return false;
		int amountValue = ((Number) amount).intValue();
		if (amountValue < 1 || amountValue > 64) {
			ConsoleLogger.error("Invalid amount for item '" + itemPath + "': it must be between 1 and 64.");
			return false;
		}
		return true;
	}


	protected static boolean isDisplayNameValid(Object displayName, String itemPath) {
		boolean isRequired = isRequired(itemPath, "displayName");
		if (isMissing(displayName, itemPath, "displayName", !isRequired)) return !isRequired;
		if (isNotTypeRequired(displayName, itemPath, "displayName", String.class)) return false;
		return true;
	}


	protected static boolean isLoreValid(Object lore, String itemPath) {
		boolean isRequired = isRequired(itemPath, "lore");
		if (isMissing(lore, itemPath, "lore", !isRequired)) return !isRequired;
		if (isNotTypeRequired(lore, itemPath, "lore", List.class)) return false;
		if (((List<?>) lore).stream().anyMatch(line -> !(line instanceof String))) {
			ConsoleLogger.error("Invalid lore for item '" + itemPath + "': lore must be a list of strings.");
			return false;
		}
		return true;
	}


	protected static boolean areEnchantmentsValid(Object enchantments, String itemPath) {
		boolean isRequired = isRequired(itemPath, "enchantments");
		if (isMissing(enchantments, itemPath, "enchantments", !isRequired)) return !isRequired;
		if (isNotTypeRequired(enchantments, itemPath, "enchantments", List.class)) return false;
		if (((List<?>) enchantments).stream().anyMatch(enchantment -> !(enchantment instanceof String))) {
			ConsoleLogger.error("Invalid enchantments for item '" + itemPath + "': enchantments must be a list of strings.");
			return false;
		}
		return areEnchantmentsFormatsValid(itemPath, ((List<?>) enchantments).toArray());
	}


	private static boolean areEnchantmentsFormatsValid(String itemPath, Object... enchantments) {
		boolean valid = true;
		for (Object enchantment : enchantments) {
			String[] parts = enchantment.toString().split(":");
			String enchantmentName = parts[0];
			String enchantmentLevel = parts.length > 1 ? parts[1] : "1";
			try {
				Integer.parseInt(enchantmentLevel);
				if (XEnchantment.of(enchantmentName).isEmpty()) {
					ConsoleLogger.error("Invalid enchantment name for item '" + itemPath + "': '" + enchantmentName + "' is not a valid enchantment.");
					valid = false;
				}
			} catch (NumberFormatException ex) {
				ConsoleLogger.error("Invalid enchantment level for item '" + itemPath + "': '" + enchantmentLevel + "' is not a valid integer.");
				valid = false;
			}
		}
		return valid;
	}


	protected static boolean areItemFlagsValid(Object itemFlags, String itemPath) {
		boolean isRequired = isRequired(itemPath, "itemFlags");
		if (isMissing(itemFlags, itemPath, "itemFlags", !isRequired)) return !isRequired;
		if (isNotTypeRequired(itemFlags, itemPath, "itemFlags", List.class)) return false;
		if (((List<?>) itemFlags).stream().anyMatch(itemFlag -> !(itemFlag instanceof String))) {
			ConsoleLogger.error("Invalid item flags list for item '" + itemPath + "': list must contain strings.");
			return false;
		}
		boolean valid = true;
		for (Object itemFlag : (List<?>) itemFlags) {
			if (XItemFlag.of(itemFlag.toString()).isEmpty()) {
				ConsoleLogger.error("Invalid item flag for item '" + itemPath + "': '" + itemFlag + "' is not a valid item flag.");
				valid = false;
			}
		}
		return valid;
	}


	// -------------------------------------------------- //


	protected static boolean isExtraRequired(String itemPath) {
		if (requiredPaths.containsKey("extra") && requiredPaths.get("extra").contains(itemPath)) {
			ConsoleLogger.error("Invalid extra section for item '" + itemPath + "': 'extra' section is required.");
			return true;
		}
		return false;
	}


	protected static boolean isSkullOwnerValid(Object skullOwner, String itemPath) {
		boolean isRequired = isRequired(itemPath, "skullOwner");
		if (isMissing(skullOwner, itemPath, "skullOwner", !isRequired)) return !isRequired;
		if (isNotTypeRequired(skullOwner, itemPath, "skullOwner", String.class)) return false;
		if (skullOwner.toString().matches("^[a-zA-Z0-9_]{1,16}$")) {
			ConsoleLogger.error("Invalid skull owner for item '" + itemPath + "': '" + skullOwner + "' is not a valid player name.");
			return false;
		}
		return true;
	}


	protected static boolean isCustomModelDataValid(Object customModelData, String itemPath) {
		return isNonNegativeNumber(customModelData, itemPath, "customModelData");
	}


	protected static boolean areClickActionsValid(Object clickActions, String itemPath) {
		boolean isRequired = isRequired(itemPath, "clickActions");
		if (isMissing(clickActions, itemPath, "clickActions", !isRequired)) return !isRequired;
		ConfigurationSection clickActionsSection = (ConfigurationSection) clickActions;
		if (clickActionsSection.getKeys(false).isEmpty()) {
			ConsoleLogger.error("Invalid click actions for item '" + itemPath + "': 'clickActions' section is empty.");
			return false;
		}
		for (String action : clickActionsSection.getKeys(false)) {
			Object clickTypeObject = clickActionsSection.get(action);
			if (isNotTypeRequired(clickTypeObject, itemPath, action, String.class)) return false;
			assert clickTypeObject != null;
			try {
				ClickType.valueOf(clickTypeObject.toString().toUpperCase());
			} catch (IllegalArgumentException ex) {
				ConsoleLogger.error("Invalid click action for item '" + itemPath + "': '" + clickTypeObject + "' is not a valid click type.");
				return false;
			}
		}
		return true;
	}


	protected static boolean isUpdateItemIntervalValid(Object updateItemInterval, String itemPath) {
		return isNonNegativeNumber(updateItemInterval, itemPath, "updateItemInterval");
	}


	protected static boolean isAmountValueValid(Object amountValue, String itemPath) {
		return isNonNegativeNumber(amountValue, itemPath, "amountValue");
	}


	// -------------------------------------------------- //



	private static boolean isNonNegativeNumber(Object value, String itemPath, String propertyName) {
		boolean isRequired = isRequired(itemPath, propertyName);
		if (isMissing(value, itemPath, propertyName, !isRequired)) return !isRequired;
		if (isNotTypeRequired(value, itemPath, propertyName, Number.class)) return false;
		double amountValueDouble = ((Number) value).doubleValue();
		if (amountValueDouble < 0) {
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': it must be a non-negative number.");
			return false;
		}
		return true;
	}


	// -------------------------------------------------- //


	protected static boolean isIgnored(String itemPath, String propertyName) {
		if (!ignoredPaths.containsKey(propertyName)) return false;
		return ignoredPaths.get(propertyName).stream().anyMatch(ignoredPath -> itemPath.equals(ignoredPath) || itemPath.startsWith(ignoredPath + "."));
	}


	protected static boolean isRequired(String itemPath, String propertyName) {
		if (!requiredPaths.containsKey(propertyName)) return false;
		return requiredPaths.get(propertyName).stream().anyMatch(requiredPath -> itemPath.equals(requiredPath) || itemPath.startsWith(requiredPath + "."));
	}


	protected static boolean isMissing(Object value, String itemPath, String propertyName, boolean silence) {
		if (value == null) {
			if (silence) return true;
			ConsoleLogger.error("Invalid value for item '" + itemPath + "': '" + propertyName + "' is missing.");
			return true;
		}
		return false;
	}


	protected static boolean isNotTypeRequired(Object value, String itemPath, String propertyName, Class<?>... requiredTypes) {
		boolean isInstance = false;
		for (Class<?> requiredType : requiredTypes) {
			if (requiredType.isInstance(value)) {
				isInstance = true;
				break;
			}
		}
		if (!isInstance) {
			String requiredTypeNames = Arrays.stream(requiredTypes).map(Class::getSimpleName).reduce((s1, s2) -> s1 + " or " + s2).orElse("");
			ConsoleLogger.error("Invalid " + propertyName + " for item '" + itemPath + "': '" + value + "' is not a " + requiredTypeNames + ".");
			return true;
		}
		return false;
	}


	// -------------------------------------------------- //


	private static final Map<String, List<String>> ignoredPaths = new HashMap<>() {{

		put("enabled", List.of(
				"rankingItems"
		));

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


	private static final Map<String, List<String>> requiredPaths = new HashMap<>() {{

		put("extra", List.of(
				"homes.items.homeItem",
				"kitsPlayerView.items.kitItem",
				"kitsAdminView.items.kitItem",
				"warpsAdminView.items.warpItem"
		));
	}};


	// -------------------------------------------------- //
}
