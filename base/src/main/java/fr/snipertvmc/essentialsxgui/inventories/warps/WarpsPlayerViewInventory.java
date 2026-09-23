package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class WarpsPlayerViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWarpsPlayerViewInventory config = (ConfigurableWarpsPlayerViewInventory) Main.getInstance().getInventory(EXGInventory.WARPS_PLAYER_VIEW);


	// -------------------------------------------------- //


	public WarpsPlayerViewInventory(Player player, String warpSearch, Set<EXGWarp> definedWarps) {
		super(
				Main.getInstance().getInventory(EXGInventory.WARPS_PLAYER_VIEW).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WARPS_PLAYER_VIEW).getTitle()
						.build(player, Map.of(
								"player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Main.getInstance().getServerDataManager().updateServerWarps();

		Set<EXGWarp> warps = warpSearch != null ? definedWarps :

				Main.getInstance().getEXGServer().getWarps()
						.stream()
						.filter(warp -> Main.getInstance().getConfiguration().canSeeWarp(player, warp.getName()))
						.sorted(Comparator.comparing(EXGWarp::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		defineWarpsItems(player, warpSearch, warps);
		defineSwitchToAdminModeItem(player);
		defineSearchWarpItem(player, warpSearch, warps);
	}


	// -------------------------------------------------- //


	private void defineWarpsItems(Player player, String warpSearch, Set<EXGWarp> warps) {

		List<EXGWarp> sortedWarps = new ArrayList<>(warps);
		if (Main.getInstance().getConfiguration().hasCustomWarpsOrder()) {
			List<String> customWarpsOrder = Main.getInstance().getConfiguration().getCustomWarpsOrder();
			sortedWarps.sort(Comparator.comparingInt(warp -> {
				int index = customWarpsOrder.indexOf(warp.getName());
				return index != -1 ? index : Integer.MAX_VALUE;
			}));
		}

		for (EXGWarp warp : sortedWarps) {

			ConfigurableItem warpItem = config.getWarpItem().get();
			if (!player.hasPermission("essentials.warps." + warp.getName())) {
				warpItem.setLore(List.of(MessagesUtils.getString(EXGMessage.NO_WARP_ACCESS)));
			}
			ItemStack warpItemStack = InventoriesUtils.getCustomItemStack(warpItem, warp, "warp", player);

			addContent(warpItemStack, e -> {
				e.getWhoClicked().closeInventory();
				player.performCommand("essentials:warp " + warp.getName());
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


	private void defineSwitchToAdminModeItem(Player player) {

		if (config.getSwitchToAdminModeItem().isEnabled() && player.hasPermission(EXGPermission.WARPS_ADMIN.get())) {
			setItem(config.getSwitchToAdminModeItem().getSlot(), config.getSwitchToAdminModeItem().build(player), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineSearchWarpItem(Player player, String warpSearch, Set<EXGWarp> warps) {

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

					new WarpsPlayerViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


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
							.filter(warp -> Main.getInstance().getConfiguration().canSeeWarp(player, warp.getName()))
							.filter(warp -> warp.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									warp.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGWarp::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchWarps.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_WARP_FOUND));
						new WarpsPlayerViewInventory(player, result.getLeft(), searchWarps).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WarpsPlayerViewInventory(player, result.getLeft(), searchWarps).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WarpsPlayerViewInventory(player, null, null).open(player)
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
