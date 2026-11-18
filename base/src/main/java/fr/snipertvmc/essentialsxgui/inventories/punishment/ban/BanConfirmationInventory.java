package fr.snipertvmc.essentialsxgui.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick.ConfigurableKickConfirmationInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class BanConfirmationInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableKickConfirmationInventory config = (ConfigurableKickConfirmationInventory) Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION);


	// -------------------------------------------------- //


	public BanConfirmationInventory(Player player, Player target, EXGPunishment banReason, long durationSeconds, boolean isBanIP) {
		super(
				Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KICK_CONFIRMATION).getTitle()
						.build(player, Map.of(
								"targetName", target.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (isBanIP) {
			String targetIP = target.getAddress().getAddress().getHostAddress();
			if (config.getConfirmItem().isEnabled()) {
				setItem(config.getConfirmItem().getSlot(), config.getConfirmItem().build(player, Map.of(
						"targetIP", targetIP,
						"reasonName", banReason.getReason(),
						"reasonDescription", banReason.getDescription()
				)), e -> {

					if (durationSeconds > 0) {
						player.performCommand("tempbanip " + targetIP + " " + banReason.getReason() + " " + durationSeconds);
					} else {
						player.performCommand("banip " + targetIP + " " + banReason.getReason());
					}

					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					player.closeInventory();
				});
			}

		} else {
			if (config.getConfirmItem().isEnabled()) {
				setItem(config.getConfirmItem().getSlot(), config.getConfirmItem().build(player, Map.of(
						"targetName", target.getName(),
						"reasonName", banReason.getReason(),
						"reasonDescription", banReason.getDescription()
				)), e -> {

					if (durationSeconds > 0) {
						player.performCommand("tempban " + target.getName() + " " + durationSeconds  + " " + banReason.getReason());
					} else {
						player.performCommand("ban " + target.getName() + " " + banReason.getReason());
					}

					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
					player.closeInventory();
				});
			}
		}

		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem().build(player), e -> {

				new BanDurationsInventory(player, target, banReason, isBanIP).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}