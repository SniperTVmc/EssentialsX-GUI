package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items;

import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableExtra {


	// -------------------------------------------------- //


	private String skullOwner;
	private Integer customModelData;

	private Map<String, ClickType> clickActions = new HashMap<>();

	private Integer updateItemInterval = 0;
	private Double amountValue;


	// -------------------------------------------------- //


	public ConfigurableExtra() {}


	public ConfigurableExtra(ConfigurationSection configurationSection) {
		this(configurationSection, 0);
	}


	public ConfigurableExtra(ConfigurationSection configurationSection, int index) {

		if (configurationSection == null) return;

		this.skullOwner = configurationSection.getString("skullOwner", null);
		boolean hasCustomModelDatas = configurationSection.get("customModelData") instanceof List;
		this.customModelData = hasCustomModelDatas ? configurationSection.getIntegerList("customModelData").get(index) : configurationSection.getInt("customModelData", 0);

		if (configurationSection.isSet("clickActions")) {
			configurationSection.getConfigurationSection("clickActions").getKeys(false).forEach(key -> {
				String clickTypeString = configurationSection.getString("clickActions." + key);
				ClickType clickType = ClickType.valueOf(clickTypeString);
				clickActions.put(key, clickType);
			});
		}

		this.updateItemInterval = configurationSection.getInt("updateItemInterval", 0);
		boolean hasAmountValues = configurationSection.get("amountValue") instanceof List;
		this.amountValue = hasAmountValues ? configurationSection.getDoubleList("amountValue").get(index) : configurationSection.getDouble("amountValue", 0.0);
	}


	public ConfigurableExtra(ConfigurableExtra extra) {
		this.skullOwner = extra.getSkullOwner();
		this.customModelData = extra.getCustomModelData();

		this.clickActions = new HashMap<>(extra.getClickActions());

		this.updateItemInterval = extra.getUpdateItemInterval();
		this.amountValue = extra.getAmountValue();
	}


	// -------------------------------------------------- //


	public boolean hasSkullOwner() {
		return skullOwner != null && !skullOwner.isEmpty();
	}
	public String getSkullOwner() {
		return skullOwner;
	}
	public boolean hasCustomModelData() {
		return customModelData != null;
	}
	public Integer getCustomModelData() {
		return customModelData;
	}

	public boolean hasClickActions() {
		return !clickActions.isEmpty();
	}
	public Map<String, ClickType> getClickActions() {
		return clickActions;
	}

	public boolean hasUpdateItemInterval() {
		return updateItemInterval > 0;
	}
	public Integer getUpdateItemInterval() {
		return updateItemInterval;
	}
	public boolean hasAmountValue() {
		return amountValue != null;
	}
	public Double getAmountValue() {
		return amountValue;
	}


	// -------------------------------------------------- //


	public ConfigurableExtra setSkullOwner(String skullOwner) {
		this.skullOwner = skullOwner;
		return this;
	}


	// -------------------------------------------------- //


	public void applyExtra(ItemBuilder itemBuilder) {
		applySkullOwner(itemBuilder);
		applyCustomModelData(itemBuilder);
	}


	// -------------------------------------------------- //


	private void applySkullOwner(ItemBuilder itemBuilder) {
		if (skullOwner != null && !skullOwner.isEmpty()) {
			itemBuilder.meta(SkullMeta.class, skullMeta -> skullMeta.setOwner(skullOwner));
		}
	}


	private void applyCustomModelData(ItemBuilder itemBuilder) {
		itemBuilder.meta(meta -> meta.setCustomModelData(customModelData));
	}


	// -------------------------------------------------- //


	public boolean isCorrectClick(ClickType clickType, String actionName) {
		if (!clickActions.containsKey(actionName)) {
			return false;
		}
		return clickType.name().equals(clickActions.get(actionName).name());
	}


	// -------------------------------------------------- //


	@Override
	public String toString() {
		return "ConfigurableExtra{" +
				"skullOwner='" + skullOwner + '\'' +
				", customModelData=" + customModelData +
				", clickActions=" + clickActions +
				", updateItemInterval=" + updateItemInterval +
				", amountValue=" + amountValue +
				'}';
	}


	// -------------------------------------------------- //
}
