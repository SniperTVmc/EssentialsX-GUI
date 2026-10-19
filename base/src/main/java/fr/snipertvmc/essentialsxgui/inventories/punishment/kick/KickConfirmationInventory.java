package fr.snipertvmc.essentialsxgui.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick.ConfigurableKickConfirmationInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGKick;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class KickConfirmationInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableKickConfirmationInventory config = (ConfigurableKickConfirmationInventory) Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION);


	// -------------------------------------------------- //


	public KickConfirmationInventory(Player player, Player target, EXGKick kickReason) {
		super(
				Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION).getTitle()
						.build(player, Map.of(
								"targetName", target != null ? target.getName() : MessagesUtils.getString(EXGMessage.ALL_PLAYERS)))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getConfirmItem().isEnabled()) {
			setItem(config.getConfirmItem().getSlot(), config.getConfirmItem().build(player, Map.of(
					"targetName", target != null ? target.getName() : MessagesUtils.getString(EXGMessage.ALL_PLAYERS),
					"reasonName", kickReason.getReason(),
					"reasonDescription", kickReason.getDescription()
			)), e -> {

				if (target != null) {
					player.performCommand("essentials:kick " + target.getName() + " " + kickReason.getReason());
				} else {
					player.performCommand("essentials:kickall " + kickReason.getReason());
				}
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
				player.closeInventory();
			});
		}

		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem().build(player), e -> {

				new KickReasonsInventory(player, target).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}