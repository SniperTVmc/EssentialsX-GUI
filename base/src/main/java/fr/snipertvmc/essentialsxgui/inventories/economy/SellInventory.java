package fr.snipertvmc.essentialsxgui.inventories.economy;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableSellInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SellInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableSellInventory config = (ConfigurableSellInventory) Main.getInstance().getInventory(EXGInventory.SELL);

	private final Set<Integer> reservedSlots = new HashSet<>(config.getBorderSlots());

	private boolean isSelling = false;


	// -------------------------------------------------- //


	public SellInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.SELL).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.SELL).getTitle()
						.build(player, Map.of(
								"player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getConfirmSellItem().isEnabled()) reservedSlots.add(config.getConfirmSellItem().getSlot());
		if (config.getCancelSellItem().isEnabled()) reservedSlots.add(config.getCancelSellItem().getSlot());


		if (config.getConfirmSellItem().isEnabled()) {
			setItem(config.getConfirmSellItem().getSlot(), config.getConfirmSellItem()
					.build(player), e -> {

				sellItems(player, e.getInventory());
			});
		}


		if (config.getCancelSellItem().isEnabled()) {
			setItem(config.getCancelSellItem().getSlot(), config.getCancelSellItem()
					.build(player), e -> {

				cancelSelling(player, e.getInventory());
			});
		}
	}


	// -------------------------------------------------- //


	private List<ItemStack> getSellItems(Inventory inventory) {
		List<ItemStack> sellItems = new ArrayList<>();

		for (int i = 0; i < inventory.getSize(); i++) {
			if (reservedSlots.contains(i)) continue;

			ItemStack itemStack = inventory.getItem(i);
			if (itemStack != null && itemStack.getType() != Material.AIR) {
				sellItems.add(itemStack);
			}
		}

		return sellItems;
	}


	private void sellItems(Player player, Inventory inventory) {

		List<ItemStack> sellItems = getSellItems(inventory);
		List<ItemStack> notSoldItems;
		try {
			notSoldItems = Main.getInstance().getEssentialsManager().sellItems(player, sellItems);

		} catch (Exception exception) {
			ConsoleLogger.error("An error occurred while selling items for player " + player.getName() + ": " + exception.getMessage());
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(sellItems.toArray(new ItemStack[0]));
			dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
			return;
		}

		HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(notSoldItems.toArray(new ItemStack[0]));
		dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));

		isSelling = true;
		player.closeInventory();
	}


	private void cancelSelling(Player player, Inventory inventory) {

		List<ItemStack> sellItems = getSellItems(inventory);

		for (ItemStack sellItem : sellItems) {
			HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(sellItem);
			dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
		}

		isSelling = true;
		player.closeInventory();
		SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
	}


	// -------------------------------------------------- //


	@Override
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(false);
		if (reservedSlots.contains(event.getRawSlot())) {
			event.setCancelled(true);
		}
	}


	@Override
	public void onClose(InventoryCloseEvent event) {
		if (isSelling) return;
		cancelSelling((Player) event.getPlayer(), event.getInventory());
	}


	// -------------------------------------------------- //
}
