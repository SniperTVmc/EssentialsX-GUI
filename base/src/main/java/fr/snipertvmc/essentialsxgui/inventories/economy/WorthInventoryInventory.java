package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableWorthInventoryInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorthInventoryInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWorthInventoryInventory config = (ConfigurableWorthInventoryInventory) Main.getInstance().getInventory(EXGInventory.WORTH_INVENTORY);


	// -------------------------------------------------- //


	public WorthInventoryInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.WORTH_INVENTORY).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WORTH_INVENTORY).getTitle()
						.build(player, Map.of(
								"player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());

		for (ItemStack itemStack : player.getInventory().getContents()) {

			if (itemStack == null || itemStack.getType() == Material.AIR) continue;

			String materialName = itemStack.getType().name();

			BigDecimal itemUnitPrice = Main.getInstance().getEXGServer().getWorth().getUnitPrice(player, itemStack);
			BigDecimal itemTotalPrice = null;
			if (itemUnitPrice != null) {
				itemTotalPrice = itemUnitPrice.multiply(BigDecimal.valueOf(itemStack.getAmount()));
			}

			BigDecimal worthMultiplier = Main.getInstance().getEXGServer().getWorth().getMultiplier(player);
			boolean hasWorthMultiplier = worthMultiplier != null && worthMultiplier.compareTo(BigDecimal.ONE) != 0;

			String itemUnitWorth = itemUnitPrice == null
					? MessagesUtils.getString(EXGMessage.NO_WORTH_AVAILABLE)
					: NumberUtil.displayCurrency(itemUnitPrice, Main.getInstance().getEssentials());

			String itemTotalWorth = itemTotalPrice == null
					? MessagesUtils.getString(EXGMessage.NO_WORTH_AVAILABLE)
					: NumberUtil.displayCurrency(itemTotalPrice, Main.getInstance().getEssentials());

			if (itemUnitPrice != null) {
				if (hasWorthMultiplier) {
					itemUnitWorth = itemUnitWorth + " " + MessagesUtils.getString(EXGMessage.MULTIPLIER_FORMAT, Map.of(
							"multiplier", String.valueOf(worthMultiplier)
					));

					itemTotalWorth = itemTotalWorth + " " + MessagesUtils.getString(EXGMessage.MULTIPLIER_FORMAT, Map.of(
							"multiplier", String.valueOf(worthMultiplier)
					));
				}
			}

			Map<String, String> variables = new HashMap<>(Map.of(
					"worthItemMaterial", materialName,
					"itemUnitWorth", itemUnitWorth,
					"itemTotalWorth", itemTotalWorth,
					"itemAmount", String.valueOf(itemStack.getAmount()),
					"worthMultiplier", String.valueOf(worthMultiplier)
			));

			if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.v1_12_2_R01)) {
				variables.put("worthItemData", String.valueOf(itemStack.getDurability()));
			}

			addContent(config.getWorthItem()
					.setMaterial(materialName)
					.setAmount(itemStack.getAmount())
					.setData((int) itemStack.getDurability())
					.build(player, variables));
		}


		boolean hasEmptyInventory = Arrays.stream(player.getInventory().getContents())
				.noneMatch(itemStack -> itemStack != null && itemStack.getType() != Material.AIR);

		if (hasEmptyInventory) {
			addContent(config.getEmptyInventoryItem()
					.build(player));
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WorthInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {
		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().getFirst();
		InventoriesUtils.updateCurrentPageItem(player, config, this);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
