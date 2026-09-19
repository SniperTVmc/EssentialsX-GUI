package fr.snipertvmc.essentialsxgui.inventories.homes;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.ConfigurableHomeEditingInventory;
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

public class HomeEditingInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableHomeEditingInventory config = (ConfigurableHomeEditingInventory) Main.getInstance().getInventory(EXGInventory.HOME_EDITING);


	// -------------------------------------------------- //


	public HomeEditingInventory(Player player, EXGHome home) {
		super(
				Main.getInstance().getInventory(EXGInventory.HOME_EDITING).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.HOME_EDITING).getTitle()
						.build(player, Map.of(
								"player", player.getName(),
								"homeName", home.getName(),
								"homeDisplayName", home.getDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getPreviewHomeItem().isEnabled()) {

			ConfigurableItem previewHomeItem = config.getPreviewHomeItem().get();
			previewHomeItem.setMaterial(home.getMaterial().name());
			previewHomeItem.setData(home.getData());

			ItemStack previewHomeItemStack;

			if (home.getCustomItemStack() != null) {
				previewHomeItemStack = home.getCustomItemStack().clone();
				ItemMeta meta = previewHomeItemStack.getItemMeta();

				meta.setDisplayName(previewHomeItem.getDisplayName()
						.replace("{homeDisplayName}", home.getDisplayName())
						.replace("{homeName}", home.getName()));

				meta.setLore(previewHomeItem.getLore().stream()
						.map(line -> line
								.replace("{homeDisplayName}", home.getDisplayName())
								.replace("{homeName}", home.getName()))
						.collect(Collectors.toList()));

				previewHomeItemStack.setItemMeta(meta);

			} else {
				previewHomeItemStack = previewHomeItem.build(player, Map.of(
						"homeDisplayName", home.getDisplayName(),
						"homeName", home.getName()));
			}

			setItem(config.getPreviewHomeItem().getSlot(), previewHomeItemStack);
		}


		if (config.getChangeDisplayNameItem().isEnabled()) {
			setItem(config.getChangeDisplayNameItem().getSlot(), config.getChangeDisplayNameItem()
					.build(player, Map.of(
							"homeName", home.getName(),
							"homeDisplayName", home.getDisplayName())
					), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeHomeDisplayName(player, home);
			});
		}

		if (config.getChangeIconItem().isEnabled()) {
			setItem(config.getChangeIconItem().getSlot(), config.getChangeIconItem()
					.build(player, Map.of(
							"homeName", home.getName())
					), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				changeHomeIcon(player, home);
			});
		}

		if (config.getDeleteHomeItem().isEnabled()) {
			setItem(config.getDeleteHomeItem().getSlot(), config.getDeleteHomeItem()
					.build(player, Map.of(
							"homeName", home.getName(),
							"homeDisplayName", home.getDisplayName())
					), e -> {

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				deleteHome(player, home);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new HomesInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public void changeHomeDisplayName(Player player, EXGHome home) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("homes", "changeHomeDisplayNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_DISPLAY_NAME))
				.setCharactersListPath("homes.changeHomeDisplayNameCharacterList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					home.setDisplayName(result.getLeft());
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.DISPLAY_NAME_CHANGED,
							Map.of("newDisplayName", result.getLeft())
					));
					new HomeEditingInventory(player, home).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new HomeEditingInventory(player, home).open(player)
		);
	}


	public void changeHomeIcon(Player player, EXGHome home) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("homes", "changeHomeIconEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME_CHAT));

		} else if (entryType == EXGEntryType.ITEM_IN_HAND) {

			ItemStack itemInHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

			if (itemInHand == null || itemInHand.getType() == Material.AIR) {
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ITEM_CANT_BE_AIR));
				new HomeEditingInventory(player, home).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				return;
			}

			home.setCustomItemStack(itemInHand);
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", itemInHand.getType().name())));
			new HomeEditingInventory(player, home).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL, EXGEntryType.GUI))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_ICON_NAME))
				.setMaterialListPath("homes.changeHomeIconMaterialList")
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processMaterialEntry(player, entrySettings,

				result -> {

					home.setCustomItemStack(null);
					home.setMaterial(result.getLeft().getLeft());
					home.setData(result.getLeft().getRight());

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ICON_CHANGED, Map.of("newIcon", result.getLeft().getLeft().name())));
					new HomeEditingInventory(player, home).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new HomeEditingInventory(player, home).open(player)
		);
	}


	public void deleteHome(Player player, EXGHome home) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("homes", "deleteHomeEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_HOME_CHAT, Map.of("homeName", home.getName())));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.CONFIRM_DELETE_HOME))
				.setEqualsToSomething("confirm");

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {
					try {
						Main.getInstance().getEssentials().getUser(player).delHome(home.getName());

					} catch (Exception ex) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_DELETE_ERROR));
						new HomesInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.HOME_DELETED,
							Map.of("homeName", home.getName()))
					);
					new HomesInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new HomeEditingInventory(player, home).open(player)
		);
	}

	// -------------------------------------------------- //
}
