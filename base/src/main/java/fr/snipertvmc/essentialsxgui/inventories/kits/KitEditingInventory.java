package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.ConfigurableKitEditingInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
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

public class KitEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitEditingInventory config = (ConfigurableKitEditingInventory) Main.getInstance().getInventory(EXGInventory.KIT_EDITING);


	// -------------------------------------------------- //


	public KitEditingInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventory(EXGInventory.KIT_EDITING).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KIT_EDITING).getTitle()
						.build(player, Map.of(
								"player", player.getName(),
								"kitName", kit.getName(),
								"kitDisplayName", kit.getDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getPreviewKitItem().isEnabled()) {

			ConfigurableItem previewKitItem = config.getPreviewKitItem().get();
			previewKitItem.setMaterial(kit.getMaterial().name());
			previewKitItem.setData(kit.getData());

			ItemStack previewKitItemStack;

			if (kit.getCustomItemStack() != null) {
				previewKitItemStack = kit.getCustomItemStack().clone();
				ItemMeta meta = previewKitItemStack.getItemMeta();

				meta.setDisplayName(previewKitItem.getDisplayName()
						.replace("{kitDisplayName}", kit.getDisplayName())
						.replace("{kitName}", kit.getName()));

				meta.setLore(previewKitItem.getLore().stream()
						.map(line -> line
								.replace("{kitDisplayName}", kit.getDisplayName())
								.replace("{kitName}", kit.getName()))
						.collect(Collectors.toList()));

				previewKitItemStack.setItemMeta(meta);

			} else {
				previewKitItemStack = previewKitItem.build(player, Map.of(
								"kitDisplayName", kit.getDisplayName(),
								"kitName", kit.getName()));
			}

			setItem(config.getPreviewKitItem().getSlot(), previewKitItemStack);
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.build(player, Map.of(
							"kitName", kit.getName(),
							"kitDisplayName", kit.getDisplayName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeKitDisplayName(player, kit);
			});
		}


		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.build(player, Map.of(
							"kitName", kit.getName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeKitIcon(player, kit);
			});
		}

		if (config.getDeleteKitItem().isEnabled()) {
			setItem(config.getDeleteKitItem().getSlot(), config.getDeleteKitItem()
					.build(player, Map.of(
							"kitName", kit.getName(),
							"kitDisplayName", kit.getDisplayName()
					)), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				deleteKit(player, kit);
			});
		}


		if (config.getEditKitContentsItem().isEnabled()) {
			setItem(config.getEditKitContentsItem().getSlot(), config.getEditKitContentsItem()
					.build(player), e -> {

				new KitEditorInventory(player, kit).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new KitsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeKitDisplayName(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "changeKitDisplayNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME))
				.setCharactersListPath("kits.changeKitDisplayNameCharacterList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					kit.setDisplayName(result.getLeft());
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("newDisplayName", result.getLeft())
					));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitEditingInventory(player, kit).open(player)
		);
	}


	public void changeKitIcon(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "changeKitIconEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME_CHAT));

		} else if (entryType == EXGEntryType.ITEM_IN_HAND) {

			ItemStack itemInHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

			if (itemInHand == null || itemInHand.getType() == Material.AIR) {
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ITEM_CANT_BE_AIR));
				new KitEditingInventory(player, kit).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				return;
			}

			kit.setCustomItemStack(itemInHand);
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", itemInHand.getType().name())));
			new KitEditingInventory(player, kit).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL, EXGEntryType.GUI))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME))
				.setMaterialListPath("kits.changeKitIconMaterialList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processMaterialEntry(player, entrySettings,

				result -> {

					kit.setCustomItemStack(null);
					kit.setMaterial(result.getLeft().getLeft());
					kit.setData(result.getLeft().getRight());

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", result.getLeft().getLeft().name())));
					new KitEditingInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitEditingInventory(player, kit).open(player)
		);
	}


	public void deleteKit(Player player, EXGKit kit) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "deleteKitEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_KIT_CHAT, Map.of("kitName", kit.getName())));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_KIT))
				.setEqualsToSomething("confirm");

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Main.getInstance().getEssentials().getKits().removeKit(kit.getName());
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_DELETED, Map.of("kitName", kit.getName())));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitEditingInventory(player, kit).open(player)
		);
	}


	// -------------------------------------------------- //
}
