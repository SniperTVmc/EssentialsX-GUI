package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableWorthInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Map;

public class WorthInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableWorthInventory config = (ConfigurableWorthInventory) Main.getInstance().getInventory(EXGInventory.WORTH);


	// -------------------------------------------------- //


	public WorthInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.WORTH).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WORTH).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);


		if (config.getAllItem().isEnabled()) {
			setItem(config.getAllItem().getSlot(), config.getAllItem()
					.build(player), e -> {

				new WorthAllInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		ItemStack handItemStack = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
		boolean hasItemInHand = handItemStack != null && handItemStack.getType() != Material.AIR;

		String handItemMaterialName = hasItemInHand
				? handItemStack.getType().name()
				: "BARRIER";

		String handItemUnitWorth = MessagesUtils.getString(hasItemInHand
				? EXGMessage.NO_WORTH_AVAILABLE
				: EXGMessage.NO_ITEM_IN_HAND);

		String handItemTotalWorth = MessagesUtils.getString(hasItemInHand
				? EXGMessage.NO_WORTH_AVAILABLE
				: EXGMessage.NO_ITEM_IN_HAND);

		int handItemAmount = hasItemInHand ? handItemStack.getAmount() : 1;
		byte handItemData = hasItemInHand ? (byte) handItemStack.getDurability() : 0;

		BigDecimal worthMultiplier = Main.getInstance().getEXGServer().getWorth().getMultiplier(player);
		boolean hasWorthMultiplier = worthMultiplier != null && worthMultiplier.compareTo(BigDecimal.ONE) != 0;

		if (hasItemInHand) {

			BigDecimal handItemUnitPrice = Main.getInstance().getEXGServer().getWorth().getUnitPrice(player, handItemStack);
			if (handItemUnitPrice != null) {
				handItemUnitWorth = MessagesUtils.getString(EXGMessage.WORTH_FORMAT, Map.of(
						"worth", NumberUtil.displayCurrency(handItemUnitPrice, Main.getInstance().getEssentials())));
				BigDecimal handItemTotalPrice = handItemUnitPrice.multiply(BigDecimal.valueOf(handItemStack.getAmount()));
				handItemTotalWorth = MessagesUtils.getString(EXGMessage.WORTH_FORMAT, Map.of(
						"worth", NumberUtil.displayCurrency(handItemTotalPrice, Main.getInstance().getEssentials())));

				if (hasWorthMultiplier) {
					handItemUnitWorth = handItemUnitWorth + " " + MessagesUtils.getString(EXGMessage.MULTIPLIER_FORMAT, Map.of(
							"multiplier", String.valueOf(worthMultiplier)
					));

					handItemTotalWorth = handItemTotalWorth + " " + MessagesUtils.getString(EXGMessage.MULTIPLIER_FORMAT, Map.of(
							"multiplier", String.valueOf(worthMultiplier)
					));
				}
			}
		}

		if (config.getHandItem().isEnabled()) {
			setItem(config.getHandItem().getSlot(), config.getHandItem()
					.setMaterial(handItemMaterialName)
					.setData(handItemData)
					.setAmount(handItemAmount)
					.build(player, Map.of(
							"handItemMaterial", handItemMaterialName,
							"handItemUnitWorth", handItemUnitWorth,
							"handItemTotalWorth", handItemTotalWorth,
							"handItemAmount", String.valueOf(handItemAmount),
							"worthMultiplier", String.valueOf(worthMultiplier)
					)));
		}


		BigDecimal inventoryPrice = Main.getInstance().getEXGServer().getWorth().getInventoryPrice(player);
		String inventoryWorth = NumberUtil.displayCurrency(inventoryPrice, Main.getInstance().getEssentials());

		if (hasWorthMultiplier) {
			inventoryWorth = inventoryWorth + " " + MessagesUtils.getString(EXGMessage.MULTIPLIER_FORMAT, Map.of(
					"multiplier", String.valueOf(worthMultiplier)
			));
		}

		if (config.getInventoryItem().isEnabled()) {
			setItem(config.getInventoryItem().getSlot(), config.getInventoryItem()
					.build(player, Map.of(
							"inventoryWorth", inventoryWorth,
							"worthMultiplier", String.valueOf(worthMultiplier)
					)), e -> {

				new WorthInventoryInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	// -------------------------------------------------- //
}
