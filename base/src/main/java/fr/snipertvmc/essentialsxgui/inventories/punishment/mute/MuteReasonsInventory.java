package fr.snipertvmc.essentialsxgui.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute.ConfigurableMuteReasonsInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class MuteReasonsInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableMuteReasonsInventory config = (ConfigurableMuteReasonsInventory) Main.getInstance().getInventory(EXGInventory.MUTE_REASONS);


	// -------------------------------------------------- //


	public MuteReasonsInventory(Player player, Player target) {
		super(
				Main.getInstance().getInventory(EXGInventory.MUTE_REASONS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.MUTE_REASONS).getTitle()
						.build(player, Map.of(
								"targetName", target.getName())
		));


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		// Mute reasons items
		for (EXGPunishment muteReason : config.getMuteReasons()) {
			ConfigurableItem reasonPlayerItem = config.getReasonItem().get();

			reasonPlayerItem.setMaterial(muteReason.getMaterial());

			addContent(reasonPlayerItem.build(player, Map.of(
					"targetName", target.getName(),
					"reasonName", muteReason.getReason(),
					"reasonDescription", muteReason.getDescription()
			)), e -> {

				new MuteDurationsInventory(player, target, muteReason).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new MutePlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}