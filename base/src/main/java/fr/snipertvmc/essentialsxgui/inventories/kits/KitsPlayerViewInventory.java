package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.ConfigurableKitsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KitsPlayerViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitsPlayerViewInventory config = (ConfigurableKitsPlayerViewInventory) Main.getInstance().getInventory(EXGInventory.KITS_PLAYER_VIEW);


	// -------------------------------------------------- //


	public KitsPlayerViewInventory(Player player, String kitSearch, Set<EXGKit> definedKits) {
		super(
				Main.getInstance().getInventory(EXGInventory.KITS_PLAYER_VIEW).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KITS_PLAYER_VIEW).getTitle()
						.build(player, Map.of(
								"player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Main.getInstance().getServerDataManager().updateServerKits();

		Set<EXGKit> kits = kitSearch != null ? definedKits :

				Main.getInstance().getEXGServer().getKits()
						.stream()
						.filter(kit -> player.hasPermission("essentials.kits." + kit.getName()))
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		defineKitsItems(player, kitSearch, kits);
		defineSwitchToAdminModeItem(player);
		defineSearchKitItem(player, kitSearch, kits);

		if (config.getKitItem().getExtra().hasUpdateItemInterval()) {
			startRefreshTask(config.getKitItem().getExtra().getUpdateItemInterval() * 20L);
		}
	}


	// -------------------------------------------------- //


	private void defineKitsItems(Player player, String kitSearch, Set<EXGKit> kits) {

		for (EXGKit kit : kits) {

			AtomicReference<ConfigurableItem> atomicKitItemConfig = new AtomicReference<>(config.getKitItem());

			Supplier<ItemStack> supplierKitItem = () -> {

				String kitDelay = MessagesUtils.getString(EXGMessage.AVAILABLE);
				if (!player.hasPermission("essentials.kit.exemptdelay")) {
					long delay = ((Number) Main.getInstance().getEssentials().getKits().getKit(kit.getName()).getOrDefault("delay", 0)).longValue();
					long kitTimestamp = Main.getInstance().getEssentials().getUser(player.getUniqueId()).getKitTimestamp(kit.getName()) / 1000;
					long now = System.currentTimeMillis() / 1000;
					long secondsRemaining = delay - (now - kitTimestamp);
					if (secondsRemaining > 0) {
						kitDelay = TimeUtils.formatInTime(secondsRemaining);
					}
				}

				atomicKitItemConfig.set(config.getKitItem().get().addVariable("kitDelay", kitDelay));
				return InventoriesUtils.getCustomItemStack(atomicKitItemConfig.get(), kit, "kit", player);
			};

			ConfigurableItem kitItemConfig = atomicKitItemConfig.get();

			addDynamicContent(supplierKitItem, e -> {

				if (kitItemConfig.getExtra().isCorrectClick(e.getClick(), "receiveKit")) {
					player.performCommand("essentials:kit " + kit.getName());
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);

				} else if (kitItemConfig.getExtra().isCorrectClick(e.getClick(), "previewKit")) {
					new KitPreviewInventory(player, kit).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				}

			});
		}

		if (kits.isEmpty()) {

			if (kitSearch == null) {
				addContent(config.getNoKitsItem().build(player));

			} else {
				addContent(config.getNoSearchKitResultsItem()
						.build(player, Map.of(
								"kitSearch", kitSearch)));
			}
		}
	}


	private void defineSwitchToAdminModeItem(Player player) {

		if (config.getSwitchToAdminModeItem().isEnabled() && player.hasPermission(EXGPermission.KITS_ADMIN.get())) {
			setItem(config.getSwitchToAdminModeItem().getSlot(), config.getSwitchToAdminModeItem().build(player), e -> {

				new KitsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineSearchKitItem(Player player, String kitSearch, Set<EXGKit> kits) {

		if (kitSearch == null) {
			if (config.getSearchKitItem().isEnabled() && !kits.isEmpty()) {
				setItem(config.getSearchKitItem().getSlot(), config.getSearchKitItem()
						.build(player), e -> {

					searchKit(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchKitItem().isEnabled()) {
				setItem(config.getCancelSearchKitItem().getSlot(), config.getCancelSearchKitItem()
						.build(player), e -> {

					new KitsPlayerViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void searchKit(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "searchKitEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_KIT_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_KIT))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Set<EXGKit> searchKits = Main.getInstance().getEXGServer().getKits()
							.stream()
							.filter(kit -> player.hasPermission("essentials.kits." + kit.getName()))
							.filter(kit -> kit.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									kit.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGKit::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchKits.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_KIT_FOUND));
						new KitsPlayerViewInventory(player, result.getLeft(), searchKits).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new KitsPlayerViewInventory(player, result.getLeft(), searchKits).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsPlayerViewInventory(player, null, null).open(player)
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
