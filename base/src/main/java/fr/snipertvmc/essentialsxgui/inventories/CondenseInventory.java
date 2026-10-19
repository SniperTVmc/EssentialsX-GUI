package fr.snipertvmc.essentialsxgui.inventories;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.ConfigurableCondenseInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CondenseInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableCondenseInventory config = (ConfigurableCondenseInventory) Main.getInstance().getInventory(EXGInventory.CONDENSE);

	private final Set<Integer> reservedSlots = new HashSet<>(config.getBorderSlots());

	private boolean isCondensing = false;


	// -------------------------------------------------- //


	public CondenseInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.CONDENSE).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.CONDENSE).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getCondenseItem().isEnabled()) reservedSlots.add(config.getCondenseItem().getSlot());
		if (config.getCancelItem().isEnabled()) reservedSlots.add(config.getCancelItem().getSlot());


		if (config.getCondenseItem().isEnabled()) {
			setItem(config.getCondenseItem().getSlot(), config.getCondenseItem()
					.build(player), e -> {

				condenseItems(player, e.getInventory());
			});
		}


		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem()
					.build(player), e -> {

				cancelCondensing(player, e.getInventory());
			});
		}
	}


	// -------------------------------------------------- //


	private List<ItemStack> getCondenseItems(Inventory inventory) {
		List<ItemStack> condenseItems = new ArrayList<>();

		for (int i = 0; i < inventory.getSize(); i++) {
			if (reservedSlots.contains(i)) continue;

			ItemStack itemStack = inventory.getItem(i);
			if (itemStack != null && itemStack.getType() != Material.AIR) {
				condenseItems.add(itemStack);
			}
		}

		return condenseItems;
	}


	private void condenseItems(Player player, Inventory inventory) {

		List<ItemStack> items = getCondenseItems(inventory);
		boolean success = Main.getInstance().getEssentialsManager().condenseItems(player, items);

		isCondensing = true;
		player.closeInventory();

		if (success) {
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
		} else {
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
		}
	}


	private void cancelCondensing(Player player, Inventory inventory) {

		List<ItemStack> condenseItems = getCondenseItems(inventory);

		for (ItemStack item : condenseItems) {
			if (item != null && item.getType() != Material.AIR) {
				HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(item);
				dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
			}
		}

		isCondensing = true;
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
		if (isCondensing) return;
		cancelCondensing((Player) event.getPlayer(), event.getInventory());
	}


	// -------------------------------------------------- //
}
