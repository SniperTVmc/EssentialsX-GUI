package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EXGWorth {


	// -------------------------------------------------- //


	private final Map<String, BigDecimal> itemsWorth = new HashMap<>();
	private final Map<String, Material> materials = new HashMap<>();


	// -------------------------------------------------- //


	public EXGWorth() {
		for (Material material : Material.values()) {
			materials.put(material.name().toLowerCase().replace("_", ""), material);
		}

		loadItemsWorth();
	}


	// -------------------------------------------------- //


	public void loadItemsWorth() {
		itemsWorth.clear();

		File file = Main.getInstance().getEssentials().getWorth().getFile();
		YamlConfiguration worthConfigurationFile = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection worthConfig =  worthConfigurationFile.getConfigurationSection("worth");
		if (worthConfig == null) return;

		for (String key : worthConfig.getKeys(false)) {
			String materialName = key.toLowerCase().replace("_", "");
			if (!materials.containsKey(materialName)) continue;

			// With data
			boolean dataSupport = VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.v1_12_2_R01);
			if (dataSupport && worthConfig.getConfigurationSection(key) != null) {
				ConfigurationSection materialSection = worthConfig.getConfigurationSection(key);
				if (materialSection == null) continue;

				for (String dataKey : materialSection.getKeys(false)) {
					BigDecimal itemWorth = BigDecimal.valueOf(materialSection.getDouble(dataKey));
					itemsWorth.put(materialName + ":" + dataKey, itemWorth);
				}
				continue;
			}

			// Without data
			BigDecimal itemWorth = BigDecimal.valueOf(worthConfig.getDouble(key));
			itemsWorth.put(materialName, itemWorth);
		}
	}


	public Map<String, BigDecimal> getItemsWorth() {
		return itemsWorth;
	}


	public Material getMaterialFromWorthName(String materialName) {
		return materials.get(materialName);
	}


	// -------------------------------------------------- //


	public BigDecimal getUnitPrice(Player player, ItemStack itemStack) {

		String itemName = itemStack.getType().name().toLowerCase().replace("_", "");

		// Without data
		if (VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.v1_12_2_R01)) {
			BigDecimal unitPrice = itemsWorth.getOrDefault(itemName, null);
			return unitPrice != null ? unitPrice.multiply(getMultiplier(player)) : null;
		}

		// With data
		short itemData = itemStack.getDurability();

		if (itemsWorth.containsKey(itemName)) {
			return itemsWorth.get(itemName).multiply(getMultiplier(player));
		} else if (itemsWorth.containsKey(itemName + ":" + itemData)) {
			return itemsWorth.get(itemName + ":" + itemData).multiply(getMultiplier(player));
		} else {
			BigDecimal unitPrice = itemsWorth.getOrDefault(itemName + ":*", null);
			return unitPrice != null ? unitPrice.multiply(getMultiplier(player)) : null;
		}
	}


	public BigDecimal getInventoryPrice(Player player) {
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR) continue;

			BigDecimal itemPrice = getUnitPrice(player, itemStack);
			if (itemPrice != null) {
				totalPrice = totalPrice.add(itemPrice.multiply(BigDecimal.valueOf(itemStack.getAmount())));
			}
		}

		return totalPrice;
	}


	public BigDecimal getMultiplier(Player player) {
		return Main.getInstance().getEssentials().getSettings().getMultiplier(Main.getInstance().getEssentials().getUser(player));
	}


	// -------------------------------------------------- //
}
