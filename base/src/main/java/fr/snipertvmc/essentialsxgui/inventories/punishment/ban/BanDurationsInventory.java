package fr.snipertvmc.essentialsxgui.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban.ConfigurableBanDurationsInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public class BanDurationsInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableBanDurationsInventory config = (ConfigurableBanDurationsInventory) Main.getInstance().getInventory(EXGInventory.BAN_DURATIONS);


	// -------------------------------------------------- //


	public BanDurationsInventory(Player player, Player target, EXGPunishment banReason, boolean isBanIP) {
		super(
				Main.getInstance().getInventory(EXGInventory.BAN_DURATIONS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.BAN_DURATIONS).getTitle()
						.build(player, Map.of(
								"targetName", target.getName(),
								"targetIP", isBanIP ? target.getAddress().getAddress().getHostAddress() : "{targetIP}"))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		// Ban durations items
		if (isBanIP) {

			if (config.getPermanentIPItem().isEnabled()) {
				setItem(config.getPermanentIPItem().getSlot(), config.getPermanentIPItem().build(player, Map.of(
						"banDuration", MessagesUtils.getString(EXGMessage.PERMANENT),
						"targetName", target.getName(),
						"targetIP", target.getAddress().getAddress().getHostAddress())
				), e -> {

					new BanConfirmationInventory(player, target, banReason, -1, true).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

			Set<ConfigurableItem> durationIPItems = config.getDurationIPItems();
			for (ConfigurableItem durationIPItem : durationIPItems) {
				ConfigurableItem durationIPItemCopy = durationIPItem.get();

				double amountValue = durationIPItemCopy.getExtra().getAmountValue();
				String duration = TimeUtils.formatDuration((long) amountValue);

				setItem(durationIPItemCopy.getSlot(), durationIPItemCopy
						.build(player, Map.of(
								"banDuration", duration,
								"targetName", target.getName(),
								"targetIP", target.getAddress().getAddress().getHostAddress())
						), e -> {

					 new BanConfirmationInventory(player, target, banReason, (long) amountValue, true).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

		} else {

			if (config.getPermanentPlayerItem().isEnabled()) {
				setItem(config.getPermanentPlayerItem().getSlot(), config.getPermanentPlayerItem().build(player, Map.of(
						"banDuration", MessagesUtils.getString(EXGMessage.PERMANENT),
						"targetName", target.getName())
				), e -> {

					new BanConfirmationInventory(player, target, banReason, -1, false).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}

			Set<ConfigurableItem> durationPlayerItems = config.getDurationPlayerItems();
			for (ConfigurableItem durationPlayerItem : durationPlayerItems) {
				ConfigurableItem durationPlayerItemCopy = durationPlayerItem.get();

				double amountValue = durationPlayerItemCopy.getExtra().getAmountValue();
				String duration = TimeUtils.formatDuration((long) amountValue);

				setItem(durationPlayerItemCopy.getSlot(), durationPlayerItemCopy
						.build(player, Map.of(
								"banDuration", duration,
								"targetName", target.getName())
						), e -> {

					 new BanConfirmationInventory(player, target, banReason, (long) amountValue, false).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new BanReasonsInventory(player, target, isBanIP).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}