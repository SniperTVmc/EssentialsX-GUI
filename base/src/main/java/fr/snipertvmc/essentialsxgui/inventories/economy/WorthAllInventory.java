package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.VersionUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableWorthAllInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorthAllInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWorthAllInventory config = (ConfigurableWorthAllInventory) Main.getInstance().getInventory(EXGInventory.WORTH_ALL);


	// -------------------------------------------------- //


	public WorthAllInventory(Player player, String worthSearch, Map<String, BigDecimal> itemsWorth) {
		super(
				Main.getInstance().getInventory(EXGInventory.WORTH_ALL).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WORTH_ALL).getTitle()
						.build(player, Map.of(
								"player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WorthInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}


		if (itemsWorth == null) {
			itemsWorth = Main.getInstance().getEXGServer().getWorth().getItemsWorth();
		}

		addWorthItem(player, itemsWorth, worthSearch);
		addSearchWorthItem(player, itemsWorth, worthSearch);
	}


	// -------------------------------------------------- //


	private void addWorthItem(Player player, Map<String, BigDecimal> itemsWorth, String worthSearch) {

		// Without data
		if (VersionUtil.getServerBukkitVersion().isLowerThanOrEqualTo(VersionUtil.v1_12_2_R01)) {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String[] fullMaterial = entry.getKey().split(":");

				String materialName = fullMaterial[0];
				String dataValue = fullMaterial.length > 1 ? fullMaterial[1] : "0";

				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);
				byte data = !dataValue.equals("*") ? Byte.valueOf(dataValue) : 0;

				try {
					if (!material.isItem()) continue;
				} catch (NoSuchMethodError e) {
					// Ignore the error for versions lower than 1.13
				}

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.setMaterial(material.name())
						.setData(data)
						.build(player, Map.of(
								"worthItemMaterial", material.name(),
								"worthItemData", dataValue,
								"itemWorth", itemWorth)
						));
			}

		// Without data
		} else {

			for (Map.Entry<String, BigDecimal> entry : itemsWorth.entrySet()) {

				String materialName = entry.getKey();
				Material material = Main.getInstance().getEXGServer().getWorth().getMaterialFromWorthName(materialName);

				try {
					if (!material.isItem()) continue;
				} catch (NoSuchMethodError e) {
					// Ignore the error for versions lower than 1.13
				}

				BigDecimal itemPrice = entry.getValue();
				String itemWorth = NumberUtil.displayCurrency(itemPrice, Main.getInstance().getEssentials());

				addContent(config.getWorthItem()
						.setMaterial(material.name())
						.build(player, Map.of(
								"worthItemMaterial", material.name(),
								"itemWorth", itemWorth)
						));
			}
		}


		if (itemsWorth.isEmpty()) {

			if (worthSearch == null) {
				addContent(config.getNoWorthItem().build(player));

			} else {
				addContent(config.getNoSearchWorthItemsItem()
						.build(player, Map.of
								("worthSearch", worthSearch)
						));
			}
		}
	}


	private void addSearchWorthItem(Player player, Map<String, BigDecimal> itemsWorth, String worthSearch) {

		if (worthSearch == null) {
			if (config.getSearchWorthItem().isEnabled() && !itemsWorth.isEmpty()) {
				setItem(config.getSearchWorthItem().getSlot(), config.getSearchWorthItem()
						.build(player), e -> {

					searchWorth(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {
			if (config.getCancelSearchWorthItem().isEnabled()) {
				setItem(config.getCancelSearchWorthItem().getSlot(), config.getCancelSearchWorthItem()
						.build(player), e -> {

					new WorthAllInventory(player, null, null).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}
	}


	// -------------------------------------------------- //


	private void searchWorth(Player player) {

		if (!Main.getInstance().getChatManager().canDoChat(player)) {
			return;
		}

		EXGEntryType entryType = Main.getInstance().getConfiguration().getEntryType("economy.worth", "searchWorthEntryType");
		if (entryType == EXGEntryType.CHAT) {
			player.closeInventory();
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.SEARCH_WORTH_CHAT));
		}

		EXGEntrySettings entrySettings = new EXGEntrySettings(entryType)
				.setAcceptedTypes(List.of(EXGEntryType.CHAT, EXGEntryType.ANVIL))
				.setEntryDisplayName(MessagesUtils.getString(EXGMessage.SEARCH_WORTH))
				.setMinLength(1)
				.setMaxLength(Main.getInstance().getConfiguration().getMaxNameLength());

		DataEntryUtils.processStringEntry(player, entrySettings,

				result -> {

					Map<String, BigDecimal> searchItemsWorth = Main.getInstance().getEXGServer().getWorth().getItemsWorth()
							.entrySet().stream()
							.filter(entry -> entry.getKey().toLowerCase().contains(result.getLeft().toLowerCase()))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

					if (searchItemsWorth.isEmpty()) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_WORTH_FOUND));
						new WorthAllInventory(player, result.getLeft(), searchItemsWorth).open(player);
						SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
						return;
					}

					new WorthAllInventory(player, result.getLeft(), searchItemsWorth).open(player);
					SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);

				}, entry -> new WorthAllInventory(player, null, null).open(player)
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
