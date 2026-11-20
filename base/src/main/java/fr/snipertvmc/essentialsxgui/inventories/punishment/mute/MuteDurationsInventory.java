package fr.snipertvmc.essentialsxgui.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute.ConfigurableMuteDurationsInventory;
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

public class MuteDurationsInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableMuteDurationsInventory config = (ConfigurableMuteDurationsInventory) Main.getInstance().getInventory(EXGInventory.MUTE_DURATIONS);


	// -------------------------------------------------- //


	public MuteDurationsInventory(Player player, Player target, EXGPunishment muteReason) {
		super(
				Main.getInstance().getInventory(EXGInventory.MUTE_DURATIONS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.MUTE_DURATIONS).getTitle()
						.build(player, Map.of(
								"targetName", target.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		// Mute durations items
		if (config.getPermanentItem().isEnabled()) {
			setItem(config.getPermanentItem().getSlot(), config.getPermanentItem().build(player, Map.of(
					"muteDuration", MessagesUtils.getString(EXGMessage.PERMANENT),
					"targetName", target.getName())
			), e -> {

				new MuteConfirmationInventory(player, target, muteReason, -1).open(player);
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
							"muteDuration", duration,
							"targetName", target.getName())
					), e -> {

				new MuteConfirmationInventory(player, target, muteReason, (long) amountValue).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new MuteReasonsInventory(player, target).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}