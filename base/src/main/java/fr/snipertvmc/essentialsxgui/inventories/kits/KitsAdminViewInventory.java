package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.ConfigurableKitsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
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

public class KitsAdminViewInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitsAdminViewInventory config = (ConfigurableKitsAdminViewInventory) Main.getInstance().getInventory(EXGInventory.KITS_ADMIN_VIEW);


	// -------------------------------------------------- //


	public KitsAdminViewInventory(Player player, String kitSearch, Set<EXGKit> definedKits) {
		super(
				Main.getInstance().getInventory(EXGInventory.KITS_ADMIN_VIEW).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KITS_ADMIN_VIEW).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Main.getInstance().getServerDataManager().updateServerKits();

		Set<EXGKit> kits = kitSearch != null ? definedKits :

				Main.getInstance().getEXGServer().getKits()
						.stream()
						.sorted(Comparator.comparing(EXGKit::getName))
						.collect(Collectors.toCollection(LinkedHashSet::new));


		defineKitsItems(player, kits, kitSearch);
		defineSwitchToPlayerModeItem(player);
		defineCreateKitItem(player);
		defineSearchKitItem(player, kits, kitSearch);
	}


	// -------------------------------------------------- //


	private void defineKitsItems(Player player, Set<EXGKit> kits, String kitSearch) {

		for (EXGKit kit : kits) {

			ConfigurableItem kitItem = config.getKitItem().get();
			ItemStack kitItemStack = InventoriesUtils.getCustomItemStack(kitItem, kit, "kit", player);

			addContent(kitItemStack, e -> {

				if (kitItem.getExtra().isCorrectClick(e.getClick(), "giveKit")) {
					new KitPlayerGiveInventory(player, kit).open(player);

				} else if (kitItem.getExtra().isCorrectClick(e.getClick(), "editKit")) {
					new KitEditingInventory(player, kit).open(player);

				} else if (kitItem.getExtra().isCorrectClick(e.getClick(), "deleteKit")) {
					new KitEditingInventory(player, kit).deleteKit(player, kit);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
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


	private void defineSwitchToPlayerModeItem(Player player) {

		if (config.getSwitchToPlayerModeItem().isEnabled()) {
			setItem(config.getSwitchToPlayerModeItem().getSlot(), config.getSwitchToPlayerModeItem().build(player), e -> {

				new KitsPlayerViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}
	}


	private void defineCreateKitItem(Player player) {

		if (config.getCreateKitItem().isEnabled()) {
			setItem(config.getCreateKitItem().getSlot(), config.getCreateKitItem()
					.build(player), e -> {

				if (player.hasPermission("essentials.createkit")) {
					createNewKitName(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION, null));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			});
		}
	}


	private void defineSearchKitItem(Player player, Set<EXGKit> kits, String kitSearch) {

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

					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void createNewKitName(Player player) {

		if (Main.getInstance().getConfiguration().skipDataEntryProcess()) {
			String instantCreationDefaultKitName = Main.getInstance().getConfiguration().getInstantCreationDefaultKitName();
			int kitNumber = 1;

			if (instantCreationDefaultKitName.contains("%number%")) {

				List<String> kitsName = Main.getInstance().getServerManager().getEXGServer().getKits().stream()
						.map(EXGKit::getName)
						.toList();

				while (kitsName.contains(instantCreationDefaultKitName.replace("%number%", String.valueOf(kitNumber)))) {
					kitNumber++;
				}
			}

			String finalKitName = instantCreationDefaultKitName
					.replace("%number%", String.valueOf(kitNumber))
					.replace(" ", "_");

			long delay = Main.getInstance().getConfiguration().getInstantCreationDefaultKitDelay();

			player.performCommand("createkit " +  finalKitName + " " + delay);
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_CREATED, Map.of("kitName", finalKitName, "kitDelay", String.valueOf(delay))));
			new KitsAdminViewInventory(player, null, null).open(player);
			SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			return;
		}

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "createNewKitNameEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_NAME_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_NAME))
				.setMinLength(Main.getInstance().getConfiguration().getMinNameLength())
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Pattern pattern = Pattern.compile("^[a-zA-Z0-9 _-]+$");
					if (!pattern.matcher(result.getLeft()).matches()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.INVALID_NAME));
						new KitsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();
					String kitName = result.getLeft().toLowerCase().replace(" ", "_").replace("-", "_");

					if (kits.stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(kitName))) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_NAME_ALREADY_EXISTS, null));
						new KitsAdminViewInventory(player, null, null).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					createNewKitDelay(player, result.getLeft());

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
		);
	}


	private void createNewKitDelay(Player player, String kitName) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("kits", "createNewKitDelayEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_DELAY_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.ENTER_NEW_KIT_DELAY))
				.setMustBeNumber(true);

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					long delay = Long.parseLong(result.getLeft());

					player.performCommand("createkit " +  kitName.replace(" ", "_") + " " + delay);
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.KIT_CREATED, Map.of("kitName", kitName, "kitDelay", String.valueOf(delay))));
					new KitsAdminViewInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
		);
	}


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
							.filter(kit -> kit.getDisplayName().toLowerCase().contains(result.getLeft().toLowerCase()) ||
									kit.getName().toLowerCase().contains(result.getLeft().toLowerCase()))
							.sorted(Comparator.comparing(EXGKit::getName))
							.collect(Collectors.toCollection(LinkedHashSet::new));

					if (searchKits.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_KIT_FOUND, null));
						new KitsAdminViewInventory(player, result.getLeft(), searchKits).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new KitsAdminViewInventory(player, result.getLeft(), searchKits).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new KitsAdminViewInventory(player, null, null).open(player)
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
