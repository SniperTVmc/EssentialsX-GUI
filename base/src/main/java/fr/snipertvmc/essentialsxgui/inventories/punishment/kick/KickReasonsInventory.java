package fr.snipertvmc.essentialsxgui.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick.ConfigurableKickReasonsInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGKick;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class KickReasonsInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableKickReasonsInventory config = (ConfigurableKickReasonsInventory) Main.getInstance().getInventory(EXGInventory.KICK_REASONS);


	// -------------------------------------------------- //


	public KickReasonsInventory(Player player, Player target) {
		super(
				Main.getInstance().getInventory(EXGInventory.KICK_REASONS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KICK_REASONS).getTitle()
						.build(player, Map.of(
								"targetName", target != null ? target.getName() : MessagesUtils.getString(EXGMessage.ALL_PLAYERS)))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		// Kick reasons items
		for (EXGKick kickReason : config.getKickReasons()) {

			ConfigurableItem reasonItem = config.getReasonItem().get();

			reasonItem.setMaterial(kickReason.getMaterial());

			addContent(reasonItem.build(player, Map.of(
					"targetName", target != null ? target.getName() : MessagesUtils.getString(EXGMessage.ALL_PLAYERS),
					"reasonName", kickReason.getReason(),
					"reasonDescription", kickReason.getDescription()
			)), e -> {

				new KickConfirmationInventory(player, target, kickReason).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new KickPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}