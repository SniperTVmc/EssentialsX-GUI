package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGBalanceTop;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableBalanceTopInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceTopInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableBalanceTopInventory config = (ConfigurableBalanceTopInventory) Main.getInstance().getInventory(EXGInventory.BALANCE_TOP);


	// -------------------------------------------------- //


	public BalanceTopInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.BALANCE_TOP).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.BALANCE_TOP).getTitle()
						.build(player, Map.of("player", player.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);


		if (config.getForceUpdateItem().isEnabled()) {

			if (config.getForceUpdateItem().getExtra().hasUpdateItemInterval()) {
				long updateInterval = config.getForceUpdateItem().getExtra().getUpdateItemInterval() * 20L;
				setDynamicItem(config.getForceUpdateItem().getSlot(), () -> getForceUpdateItem(player),
						updateInterval, e -> forceUpdate(player)
				);

			} else {
				setItem(config.getForceUpdateItem().getSlot(), getForceUpdateItem(player), e -> forceUpdate(player));
			}
		}

		addPlayerRankingItem(player);
		addRankingItems(player);
	}


	// -------------------------------------------------- //


	private void addPlayerRankingItem(Player player) {
		if (config.getPlayerRankingItem().isEnabled()) {

			EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();

			Pair<Integer, BigDecimal> playerRanking = balanceTop.getPlayerRanking(player.getName());
			int playerRankValue = playerRanking.getLeft();
			BigDecimal playerMoney = playerRanking.getRight();

			boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
			String playerBalance = ecoEnabled
					? NumberUtil.displayCurrency(playerMoney, Main.getInstance().getEssentials())
					: MessagesUtils.getString(EXGMessage.DISABLED);

			String playerRank = playerRankValue > 0
					? MessagesUtils.getString(EXGMessage.RANK_FORMAT, Map.of("rank", String.valueOf(playerRankValue)))
					: MessagesUtils.getString(EXGMessage.NOT_RANKED);

			setItem(config.getPlayerRankingItem().getSlot(), config.getPlayerRankingItem()
					.build(player, Map.of(
							"playerName", player.getName(),
							"playerRank", playerRank,
							"playerBalance", playerBalance
					)));
		}
	}


	private void addRankingItems(Player player) {

		Map<String, String> placeholders = getBalanceTopPlaceholders();

		for (ConfigurableItem rankingItem : config.getRankingItems()) {
			rankingItem = rankingItem.get();

			String displayName = rankingItem.getDisplayName();
			List<String> lore = rankingItem.getLore();

			for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
				if (displayName.contains(placeholder.getKey())) {
					displayName = displayName.replace(placeholder.getKey(), placeholder.getValue());
				}
			}

			for (int i = 0; i < lore.size(); i++) {
				String line = lore.get(i);
				for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
					if (line.contains(placeholder.getKey())) {
						line = line.replace(placeholder.getKey(), placeholder.getValue());
					}
				}
				lore.set(i, line);
			}

			rankingItem.setDisplayName(displayName);
			rankingItem.setLore(lore);
			setItem(rankingItem.getSlot(), rankingItem.build(player));
		}
	}


	private Map<String, String> getBalanceTopPlaceholders() {

		Map<String, String> placeholders = new HashMap<>();
		EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();

		int start = config.getRankingRange().getLeft();
		int end = config.getRankingRange().getRight();

		boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String disabledMsg = MessagesUtils.getString(EXGMessage.DISABLED);
		String nobodyMsg = MessagesUtils.getString(EXGMessage.NOBODY);

		for (int i = start; i <= end; i++) {
			String name = nobodyMsg;
			String balance = NumberUtil.displayCurrency(BigDecimal.valueOf(0), Main.getInstance().getEssentials());

			if (i <= balanceTop.getBalanceTopEntries().size()) {
				Pair<String, Double> entry = balanceTop.getBalanceTopEntries().get(i - 1);

				if (entry != null) {
					name = entry.getLeft();

					if (ecoEnabled) {
						BigDecimal amount = BigDecimal.valueOf(entry.getRight());
						balance = NumberUtil.displayCurrency(amount, Main.getInstance().getEssentials());
					} else {
						balance = disabledMsg;
					}
				}
			}

			placeholders.put("{playerName_" + i + "}", name);
			placeholders.put("{playerBalance_" + i + "}", balance);
		}
		return placeholders;
	}


	private ItemStack getForceUpdateItem(Player player) {

		EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();
		long lastUpdateTimestamp = balanceTop.getLastUpdate();
		int secondsSinceLastUpdate = (int) ((System.currentTimeMillis() - lastUpdateTimestamp) / 1000);
		String lastUpdate = TimeUtils.formatAgoTime(secondsSinceLastUpdate);

		int balanceTopUpdateInterval = Main.getInstance().getConfiguration().getBalanceTopUpdateInterval();
		int secondesBeforeNextUpdate = balanceTopUpdateInterval - secondsSinceLastUpdate;
		String nextUpdate = TimeUtils.formatInTime(secondesBeforeNextUpdate);

		return config.getForceUpdateItem()
				.build(player, Map.of(
						"lastUpdate", lastUpdate,
						"nextUpdate", nextUpdate
				));
	}


	private void forceUpdate(Player player) {

		if (player.hasPermission("essentials.balancetop.force")) {

			long lastUpdate = Main.getInstance().getEXGServer().getBalanceTop().getLastUpdate();
			long lastUpdateSeconds = (System.currentTimeMillis() - lastUpdate) / 1000;
			if (lastUpdateSeconds < 5) {
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.WAIT_BEFORE_NEXT_ACTION));
				SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
				return;
			}

			Main.getInstance().getEXGServer().getBalanceTop().forceUpdate().thenRun(() -> {
				addPlayerRankingItem(player);
				addRankingItems(player);
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.BALANCE_TOP_DATA_UPDATED));
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});

		} else {
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.NO_PERMISSION));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
		}
	}


	// -------------------------------------------------- //
}
