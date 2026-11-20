package fr.snipertvmc.essentialsxgui.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute.ConfigurableMuteConfirmationInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class MuteConfirmationInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableMuteConfirmationInventory config = (ConfigurableMuteConfirmationInventory) Main.getInstance().getInventory(EXGInventory.MUTE_CONFIRMATION);


	// -------------------------------------------------- //


	public MuteConfirmationInventory(Player player, Player target, EXGPunishment muteReason, long durationSeconds) {
		super(
				Main.getInstance().getInventory(EXGInventory.MUTE_CONFIRMATION).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.MUTE_CONFIRMATION).getTitle()
						.build(player, Map.of(
								"targetName", target.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getConfirmItem().isEnabled()) {
			setItem(config.getConfirmItem().getSlot(), config.getConfirmItem().build(player, Map.of(
					"targetName", target.getName(),
					"reasonName", muteReason.getReason(),
					"reasonDescription", muteReason.getDescription()
			)), e -> {

				if (durationSeconds > 0) {
					player.performCommand("mute " + target.getName() + " " + durationSeconds + " " + muteReason.getReason());
				} else {
					player.performCommand("mute " + target.getName() + " " + muteReason.getReason());
				}

				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				player.closeInventory();
			});
		}

		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem().build(player), e -> {

				new MuteDurationsInventory(player, target, muteReason).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}