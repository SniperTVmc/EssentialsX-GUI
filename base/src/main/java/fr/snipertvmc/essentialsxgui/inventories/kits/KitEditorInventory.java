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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitEditorInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitEditorInventory config = (ConfigurableKitEditorInventory) Main.getInstance().getInventory(EXGInventory.KIT_EDITOR);

	private final Set<Integer> reservedSlots = new HashSet<>(config.getBorderSlots());

	private boolean isEditing = false;
	private String kitName = "";


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

				saveKit(player, e.getInventory(), kit);
			});
		}


		if (config.getCancelChangesItem().isEnabled()) {
			setItem(config.getCancelChangesItem().getSlot(), config.getCancelChangesItem()
					.build(player), e -> {

				cancelChanges(player, e.getInventory());
			});
		}


		kitName = kit.getName();
		List<ItemStack> kitItems = Main.getInstance().getEssentialsManager().getKitItems(kit.getName());

		for (ItemStack item : kitItems) {
			if (item != null && item.getType() != Material.AIR) {
				addItem(item, e -> e.setCancelled(false));
			}
		}
	}


	// -------------------------------------------------- //


	private List<ItemStack> getKitItems(Inventory inventory) {
		List<ItemStack> kitItems = new ArrayList<>();

		for (int i = 0; i < inventory.getSize(); i++) {
			if (reservedSlots.contains(i)) continue;

			ItemStack itemStack = inventory.getItem(i);
			if (itemStack != null && itemStack.getType() != Material.AIR) {
				kitItems.add(itemStack);
			}
		}

		return kitItems;
	}


	private void saveKit(Player player, Inventory inventory, EXGKit kit) {

		List<ItemStack> kitItems = getKitItems(inventory);
		long delay = ((Number) Main.getInstance().getEssentials().getKits().getKit(kit.getName()).get("delay")).longValue();

		EXGPlayerInventoryData inventoryBackup = InventoryBackupUtils.createPlayerInventoryBackup(player);
		player.getInventory().clear();

		player.getInventory().setContents(kitItems.toArray(new ItemStack[0]));
		player.performCommand("createkit " +  kit.getName() + " " + delay);

		InventoryBackupUtils.loadPlayerInventoryBackup(player, inventoryBackup);

		isEditing = true;
		player.closeInventory();
		SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
	}


	private void cancelChanges(Player player, Inventory inventory) {

		List<ItemStack> kitItems = getKitItems(inventory);
		List<ItemStack> originalKitItems = Main.getInstance().getEssentialsManager().getKitItems(kitName);

		for (ItemStack kitItem : kitItems) {
			if (kitItem != null && kitItem.getType() != Material.AIR) {
				if (!originalKitItems.contains(kitItem)) {
					HashMap<Integer, ItemStack> dontFitItems = player.getInventory().addItem(kitItem);
					dontFitItems.forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));
				}
			}
		}

		isEditing = true;
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
		if (isEditing) return;
		cancelChanges((Player) event.getPlayer(), event.getInventory());
	}


	// -------------------------------------------------- //
}
