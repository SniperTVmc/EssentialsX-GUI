package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayerInventoryData;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.ConfigurableKitEditorInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.InventoryBackupUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitEditorInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitEditorInventory config = (ConfigurableKitEditorInventory) Main.getInstance().getInventory(EXGInventory.KIT_EDITOR);

	private final Set<Integer> reservedSlots = new HashSet<>(config.getBorderSlots());


	// -------------------------------------------------- //


	public KitEditorInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventory(EXGInventory.KIT_EDITOR).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KIT_EDITOR).getTitle()
						.build(player, Map.of(
								"player", player.getName(),
								"kitName", kit.getName(),
								"kitDisplayName", kit.getDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getSaveKitItem().isEnabled()) reservedSlots.add(config.getSaveKitItem().getSlot());
		if (config.getCancelChangesItem().isEnabled()) reservedSlots.add(config.getCancelChangesItem().getSlot());


		if (config.getSaveKitItem().isEnabled()) {
			setItem(config.getSaveKitItem().getSlot(), config.getSaveKitItem()
					.build(player), e -> {

				e.setCancelled(true);

				List<ItemStack> kitItems = new ArrayList<>();

				for (int i = 0; i < e.getInventory().getSize(); i++) {
					if (reservedSlots.contains(i)) continue;

					ItemStack itemStack = e.getInventory().getItem(i);
					if (itemStack != null && itemStack.getType() != Material.AIR) {
						kitItems.add(itemStack);
					}
				}

				saveKit(player, kit, kitItems);
			});
		}


		if (config.getCancelChangesItem().isEnabled()) {
			setItem(config.getCancelChangesItem().getSlot(), config.getCancelChangesItem()
					.build(player), e -> {

				e.setCancelled(true);

				List<ItemStack> kitItems = new ArrayList<>();

				for (int i = 0; i < e.getInventory().getSize(); i++) {
					if (reservedSlots.contains(i)) continue;

					ItemStack itemStack = e.getInventory().getItem(i);
					if (itemStack != null && itemStack.getType() != Material.AIR) {
						kitItems.add(itemStack);
					}
				}

				cancelChanges(player, kit, kitItems);
			});
		}


		List<ItemStack> kitItems = Main.getInstance().getEssentialsManager().getKitItems(kit.getName());

		for (ItemStack item : kitItems) {
			if (item != null && item.getType() != Material.AIR) {
				addItem(item, e -> e.setCancelled(false));
			}
		}
	}


	// -------------------------------------------------- //


	private void saveKit(Player player, EXGKit kit, List<ItemStack> kitItems) {

		long delay = ((Number) Main.getInstance().getEssentials().getKits().getKit(kit.getName()).get("delay")).longValue();

		EXGPlayerInventoryData inventoryBackup = InventoryBackupUtils.createPlayerInventoryBackup(player);
		player.getInventory().clear();

		player.getInventory().setContents(kitItems.toArray(new ItemStack[0]));
		player.performCommand("createkit " +  kit.getName() + " " + delay);

		InventoryBackupUtils.loadPlayerInventoryBackup(player, inventoryBackup);

		new KitEditingInventory(player, kit).open(player);
		SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
	}


	private void cancelChanges(Player player,  EXGKit kit, List<ItemStack> kitItems) {

		List<ItemStack> originalKitItems = Main.getInstance().getEssentialsManager().getKitItems(kit.getName());

		for (ItemStack kitItem : kitItems) {
			if (kitItem != null && kitItem.getType() != Material.AIR) {
				if (!originalKitItems.contains(kitItem)) {
					HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(kitItem);
					dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
				}
			}
		}

		new KitEditingInventory(player, kit).open(player);
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


	// -------------------------------------------------- //
}
