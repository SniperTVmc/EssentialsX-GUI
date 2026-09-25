package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpEditingInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WarpEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableWarpEditingInventory config = (ConfigurableWarpEditingInventory) Main.getInstance().getInventory(EXGInventory.WARP_EDITING);


	// -------------------------------------------------- //


	public WarpEditingInventory(Player player, EXGWarp warp) {
		super(
				Main.getInstance().getInventory(EXGInventory.WARP_EDITING).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WARP_EDITING).getTitle()
						.build(player, Map.of(
								"player", player.getName(),
								"warpName", warp.getName(),
								"warpDisplayName", warp.getDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getPreviewWarpItem().isEnabled()) {

			ConfigurableItem previewWarpItem = config.getPreviewWarpItem().get();
			previewWarpItem.setMaterial(warp.getMaterial().name());
			previewWarpItem.setData(warp.getData());

			ItemStack previewWarpItemStack;

			if (warp.getCustomItemStack() != null) {
				previewWarpItemStack = warp.getCustomItemStack().clone();
				ItemMeta meta = previewWarpItemStack.getItemMeta();

				meta.setDisplayName(previewWarpItem.getDisplayName()
						.replace("{warpDisplayName}", warp.getDisplayName())
						.replace("{warpName}", warp.getName()));

				meta.setLore(previewWarpItem.getLore().stream()
						.map(line -> line
								.replace("{warpDisplayName}", warp.getDisplayName())
								.replace("{warpName}", warp.getName()))
						.collect(Collectors.toList()));

				previewWarpItemStack.setItemMeta(meta);

			} else {
				previewWarpItemStack = previewWarpItem
						.build(player, Map.of(
								"warpDisplayName", warp.getDisplayName(),
								"warpName", warp.getName()
						));
			}

			setItem(config.getPreviewWarpItem().getSlot(), previewWarpItemStack);
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.build(player, Map.of(
							"warpName", warp.getName(),
							"warpDisplayName", warp.getDisplayName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeWarpDisplayName(player, warp);
			});
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.build(player, Map.of(
							"warpName", warp.getName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeWarpIcon(player, warp);
			});
		}

		if (config.getDeleteWarpItem().isEnabled()) {
			setItem(config.getDeleteWarpItem().getSlot(), config.getDeleteWarpItem()
					.build(player, Map.of(
							"warpName", warp.getName(),
							"warpDisplayName", warp.getDisplayName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				deleteWarp(player, warp);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeWarpDisplayName(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("warps", "changeWarpDisplayNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME))
				.setCharactersListPath("warps.changeWarpDisplayNameCharactersList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					warp.setDisplayName(result.getLeft());
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("newDisplayName", result.getLeft())
					));
					new WarpEditingInventory(player, warp).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	public void changeWarpIcon(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("warps", "changeWarpIconEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME_CHAT));

		} else if (entryType == EXGEntryType.ITEM_IN_HAND) {

			ItemStack itemInHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

			if (itemInHand == null || itemInHand.getType() == Material.AIR) {
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ITEM_CANT_BE_AIR));
				new WarpEditingInventory(player, warp).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				return;
			}

			warp.setCustomItemStack(itemInHand);
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", itemInHand.getType().name())));
			new WarpEditingInventory(player, warp).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL, EXGEntryType.GUI))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME))
				.setMaterialListPath("warps.changeWarpIconMaterialList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processMaterialEntry(player, entrySettings,

				result -> {

					warp.setCustomItemStack(null);
					warp.setMaterial(result.getLeft().getLeft());
					warp.setData(result.getLeft().getRight());

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", result.getLeft().getLeft().name())));
					new WarpEditingInventory(player, warp).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	public void deleteWarp(Player player, EXGWarp warp) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("warps", "deleteWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_WARP_CHAT, Map.of("warpName", warp.getName())));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_WARP))
				.setEqualsToSomething("confirm");

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					try {
						Main.getInstance().getEssentials().getWarps().removeWarp(warp.getName());
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_DELETED, Map.of("warpName", warp.getName())));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

					} catch (Exception e) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_DELETE_ERROR));
						new WarpEditingInventory(player, warp).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}

				}, entry -> new WarpEditingInventory(player, warp).open(player)
		);
	}


	// -------------------------------------------------- //
}
