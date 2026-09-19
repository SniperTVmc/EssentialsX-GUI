package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WarpsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWarpsAdminViewInventory config = (ConfigurableWarpsAdminViewInventory) Main.getInstance().getInventory(EXGInventory.WARPS_ADMIN_VIEW);


	// -------------------------------------------------- //


	public WarpsAdminViewInventory(Player player, String warpSearch, Set<EXGWarp> definedWarps) {
		super(
				Main.getInstance().getInventory(EXGInventory.WARPS_ADMIN_VIEW).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WARPS_ADMIN_VIEW).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Main.getInstance().getServerDataManager().updateServerWarps();

		Set<EXGWarp> warps = warpSearch != null ? definedWarps :

				Main.getInstance().getEXGServer().getWarps()
						.stream()
						.sorted(Comparator.comparing(EXGWarp::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		defineWarpsItems(player, warps, warpSearch);
		defineSwitchToPlayerModeItem(player);
		defineCreateWarpItem(player);
		defineSearchWarpItem(player, warps, warpSearch);
	}


	// -------------------------------------------------- //


	private void defineWarpsItems(Player player, Set<EXGWarp> warps, String warpSearch) {

		for (EXGWarp warp : warps) {

			ConfigurableItem warpItem = config.getWarpItem().get();
			ItemStack warpItemStack = InventoriesUtils.getCustomItemStack(warpItem, warp, "warp", player);

			addContent(warpItemStack, e -> {

				if (warpItem.getExtra().isCorrectClick(e.getClick(), "teleportWarp")) {
					new WarpPlayerTeleportInventory(player, warp).open(player);

				} else if (warpItem.getExtra().isCorrectClick(e.getClick(), "editWarp")) {
					new WarpEditingInventory(player, warp).open(player);

				} else if (warpItem.getExtra().isCorrectClick(e.getClick(), "deleteWarp")) {
					new WarpEditingInventory(player, warp).deleteWarp(player, warp);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (warps.isEmpty()) {

			if (warpSearch == null) {
				addContent(config.getNoWarpsItem().build(player));

			} else {
				addContent(config.getNoSearchWarpResultsItem()
						.build(player, Map.of(
								"warpSearch", warpSearch
						)));
			}
		}
	}


	private void defineSwitchToPlayerModeItem(Player player) {

		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(player), e -> {

				new WarpsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineCreateWarpItem(Player player) {

		if (config.getCreateWarpItem().isEnabled()) {
			setItem(config.getCreateWarpItem().getSlot(), config.getCreateWarpItem()
					.build(player), e -> {

				if (Main.getInstance().getPlayerManager().getPlayer(player).canDo("setwarp", "essentials.setwarp")) {
					createNewWarp(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void defineSearchWarpItem(Player player, Set<EXGWarp> warps, String warpSearch) {

		if (warpSearch == null) {
			if (config.getSearchWarpItem().isEnabled() && !warps.isEmpty()) {
				setItem(config.getSearchWarpItem().getSlot(), config.getSearchWarpItem()
						.build(player), e -> {

					searchWarp(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchWarpItem().isEnabled()) {
				setItem(config.getCancelSearchWarpItem().getSlot(), config.getCancelSearchWarpItem()
						.build(player), e -> {

					new WarpsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void createNewWarp(Player player) {

		if (Main.getInstance().getConfiguration().skipDataEntryProcess()) {
			String instantCreationDefaultWarpName = Main.getInstance().getConfiguration().getInstantCreationDefaultWarpName();
			int warpNumber = 1;

			if (instantCreationDefaultWarpName.contains("%number%")) {

				List<String> warpsName = Main.getInstance().getServerManager().getEXGServer().getWarps().stream()
						.map(EXGWarp::getName)
						.toList();

				while (warpsName.contains(instantCreationDefaultWarpName.replace("%number%", String.valueOf(warpNumber)))) {
					warpNumber++;
				}
			}

			String finalWarpName = instantCreationDefaultWarpName
					.replace("%number%", String.valueOf(warpNumber))
					.replace(" ", "_");

			try {
				Main.getInstance().getEssentialsManager().createWarpWithPlayer(player, finalWarpName);
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_CREATED, Map.of("warpName", finalWarpName)));
				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

			} catch (Exception e) {
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_CREATION_ERROR));
				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			}
			return;
		}

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("warps", "createNewWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_WARP_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_WARP_NAME))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Pattern pattern = Pattern.compile("^[a-zA-Z0-9 _-]+$");
					if (!pattern.matcher(result.getLeft()).matches()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.INVALID_NAME));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					Set<EXGWarp> warps = Main.getInstance().getEXGServer().getWarps();
					String warpName = result.getLeft().toLowerCase().replace(" ", "_").replace("-", "_");

					if (warps.stream().anyMatch(warp -> warp.getName().equalsIgnoreCase(warpName))) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_NAME_ALREADY_EXISTS, null));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					try {
						Main.getInstance().getEssentialsManager().createWarpWithPlayer(player, warpName.replace(" ", "_"));
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_CREATED, Map.of("warpName", warpName)));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

					} catch (Exception e) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WARP_CREATION_ERROR));
						new WarpsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
					}

				}, entry -> new WarpsAdminViewInventory(player, null, null).open(player)
		);
	}


	private void searchWarp(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("warps", "searchWarpEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_WARP_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_WARP))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGWarp> searchWarps = Main.getInstance().getEXGServer().getWarps()
							.stream()
							.filter(warp -> warp.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									warp.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGWarp::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchWarps.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_WARP_FOUND));
						new WarpsAdminViewInventory(player, result.getLeft(), searchWarps).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WarpsAdminViewInventory(player, result.getLeft(), searchWarps).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpsAdminViewInventory(player, null, null).open(player)
		);
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
