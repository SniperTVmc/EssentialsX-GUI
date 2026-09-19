package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigurableItem {


	// -------------------------------------------------- //


	private boolean isCopied = false;

	private boolean enabled = false;
	private Integer slot = 0;

	private String material = Material.AIR.name();
	private Integer amount = 1;
	private Byte data = 0;

	private String displayName;
	private List<String> lore;

	private List<Pair<String, Integer>> enchantments = new ArrayList<>();
	private List<String> itemFlags = new ArrayList<>();

	private Map<String, String> variables = new HashMap<>();
	private ConfigurableExtra extra = new ConfigurableExtra();


	// -------------------------------------------------- //


	public ConfigurableItem(ConfigurationSection itemConfig) {
		this(itemConfig, 0);
	}


	public ConfigurableItem(ConfigurationSection itemConfig, int index) {

		if (itemConfig == null) return;

		this.enabled = itemConfig.getBoolean("enabled");

		boolean hasSlots = itemConfig.get("slot") instanceof List;
		this.slot = hasSlots ? itemConfig.getIntegerList("slot").get(index) : itemConfig.getInt("slot", -1);

		this.material = itemConfig.getString("material", Material.BEDROCK.name());
		this.amount = itemConfig.getInt("amount", 1);
		this.data = (byte) itemConfig.getInt("data", 0);

		this.displayName = itemConfig.getString("displayName", null);
		this.lore = itemConfig.getStringList("lore");

		this.enchantments = itemConfig.getStringList("enchantments").stream()
				.map(enchantment -> {
					String[] parts = enchantment.split(":");
					if (parts.length == 2) {
						String enchantmentName = parts[0];
						int level;
						try {
							level = Integer.parseInt(parts[1]);
						} catch (NumberFormatException e) {
							level = 1;
						}
						return Pair.of(enchantmentName, level);
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		this.itemFlags = itemConfig.getStringList("itemFlags");

		this.variables = new HashMap<>();
		this.extra = new ConfigurableExtra(itemConfig.getConfigurationSection("extra"), index);
	}


	public ConfigurableItem(ConfigurableItem itemConfig) {
		this.isCopied = true;

		this.enabled = itemConfig.isEnabled();
		this.slot = itemConfig.getSlot();

		this.material = itemConfig.getMaterial();
		this.amount = itemConfig.getAmount();
		this.data = itemConfig.getData();

		this.displayName = itemConfig.getDisplayName();
		this.lore = itemConfig.getLore() != null ? new ArrayList<>(itemConfig.getLore()) : new ArrayList<>();

		this.enchantments = itemConfig.getEnchantments() != null ? new ArrayList<>(itemConfig.getEnchantments()) : new ArrayList<>();
		this.itemFlags = itemConfig.getItemFlags() != null ? new ArrayList<>(itemConfig.getItemFlags()) : new ArrayList<>();

		this.variables = new HashMap<>();
		this.extra = itemConfig.getExtra() != null ? new ConfigurableExtra(itemConfig.getExtra()) : new ConfigurableExtra();
	}


	// -------------------------------------------------- //


	public boolean isEnabled() {
		return enabled;
	}
	public int getSlot() {
		return slot;
	}

	public String getMaterial() {
		return material;
	}
	public Integer getAmount() {
		return amount;
	}
	public Byte getData() {
		return data;
	}

	public String getDisplayName() {
		return displayName;
	}
	public List<String> getLore() {
		return lore;
	}

	public List<Pair<String, Integer>> getEnchantments() {
		return enchantments;
	}
	public List<String> getItemFlags() {
		return itemFlags;
	}

	public ConfigurableExtra getExtra() {
		return extra;
	}


	// -------------------------------------------------- //


	public ConfigurableItem setSlot(Integer slot) {
		this.slot = slot;
		return this;
	}

	public ConfigurableItem setMaterial(String material) {
		this.material = material;
		return this;
	}
	public ConfigurableItem setAmount(Integer amount) {
		this.amount = amount;
		return this;
	}
	public ConfigurableItem setData(Byte data) {
		this.data = data;
		return this;
	}

	public ConfigurableItem setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}
	public ConfigurableItem setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ConfigurableItem addVariable(String key, String value) {
		this.variables.put(key, value);
		return this;
	}
	public ConfigurableItem addVariables(Map<String, String> variables) {
		this.variables.putAll(variables);
		return this;
	}


	// -------------------------------------------------- //


	private void applyDisplayName(Player player, ItemBuilder itemBuilder) {
		if (displayName != null) {
			if (Main.getInstance().getHookManager().getPlaceholderAPIHook().isSupported()) {
				itemBuilder.name(PlaceholderAPI.setPlaceholders(player, displayName));
			} else {
				itemBuilder.name(displayName);
			}
		}
	}


	private void applyLore(Player player, ItemBuilder itemBuilder) {
		if (lore != null) {
			if (Main.getInstance().getHookManager().getPlaceholderAPIHook().isSupported()) {
				itemBuilder.lore(new ArrayList<>(PlaceholderAPI.setPlaceholders(player, lore)));
			} else {
				itemBuilder.lore(new ArrayList<>(lore));
			}
		}
	}


	private void applyEnchantments(ItemBuilder itemBuilder) {
		if (enchantments != null) {
			for (Pair<String, Integer> enchantment : enchantments) {
				Optional<XEnchantment> xEnchantment = XEnchantment.of(enchantment.getLeft());
				xEnchantment.ifPresent(value -> itemBuilder.enchant(value.get(), enchantment.getRight()));
			}
		}
	}


	private void applyItemFlags(ItemBuilder itemBuilder) {
		if (itemFlags != null) {
			for (String itemFlag : itemFlags) {
				Optional<XItemFlag> xItemFlag = XItemFlag.of(itemFlag);
				xItemFlag.ifPresent(value -> itemBuilder.flags(value.get()));
			}
		}
	}


	// -------------------------------------------------- //


	private @NotNull ItemBuilder getItemBuilder() {
		XMaterial material = XMaterial.matchXMaterial(this.material).orElse(XMaterial.BEDROCK);
		return new ItemBuilder(material.get());
	}


	public ItemStack build(Player player) {
		return build(player, new HashMap<>());
	}


	public ItemStack build(Player player, Map<String, String> variables) {

		if (!enabled) return null;
		if (!isCopied) return get().build(player, variables);

		ItemBuilder itemBuilder = getItemBuilder();

		if (amount < 0 || amount > 64) {
			amount = 1;
		}
		itemBuilder.amount(amount);

		if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.v1_12_2_R01)) {
			if (data < 0 || data > 15) {
				data = 0;
			}
			itemBuilder.data(data);
		}

		// Required before applying display name and lore,
		// because they can contain placeholders that need to be replaced with variables.
		applyVariables(variables);

		applyDisplayName(player, itemBuilder);
		applyLore(player, itemBuilder);

		applyEnchantments(itemBuilder);
		applyItemFlags(itemBuilder);

		extra.applyExtra(itemBuilder);

		return itemBuilder.build();
	}


	// -------------------------------------------------- //


	public void applyVariables(Map<String, String> variables) {
		this.variables.putAll(variables);
		if (this.variables.isEmpty()) return;
		this.variables.forEach((key, value) -> {
			this.displayName = displayName.replace("{" + key + "}", value);
			if (this.lore != null && !this.lore.isEmpty()) {
				this.lore = this.lore.stream()
						.map(line -> line.replace("{" + key + "}", value))
						.collect(Collectors.toList());
			}
		});
	}


	// -------------------------------------------------- //


	public ConfigurableItem get() {
		return new ConfigurableItem(this);
	}


	// -------------------------------------------------- //


	@Override
	public String toString() {
		return "ConfigurableItem{" +
				"isCopied=" + isCopied +
				", slot=" + slot +
				", material='" + material + '\'' +
				", amount=" + amount +
				", data=" + data +
				", displayName='" + displayName + '\'' +
				", lore=" + lore +
				", enchantments=" + enchantments +
				", itemFlags=" + itemFlags +
				", extra=" + extra +
				'}';
	}


	// -------------------------------------------------- //
}
