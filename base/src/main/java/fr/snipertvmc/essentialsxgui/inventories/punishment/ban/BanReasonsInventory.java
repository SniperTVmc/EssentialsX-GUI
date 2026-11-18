package fr.snipertvmc.essentialsxgui.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban.ConfigurableBanReasonsInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.punishment.EXGPunishment;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class BanReasonsInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableBanReasonsInventory config = (ConfigurableBanReasonsInventory) Main.getInstance().getInventory(EXGInventory.BAN_REASONS);


	// -------------------------------------------------- //


	public BanReasonsInventory(Player player, Player target, boolean isBanIP) {
		super(
				Main.getInstance().getInventory(EXGInventory.BAN_REASONS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.BAN_REASONS).getTitle()
						.build(player, Map.of(
								"targetName", target.getName(),
								"targetIP", isBanIP ? target.getAddress().getAddress().getHostAddress() : "{targetIP}")
		));


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		// Ban reasons items
		for (EXGPunishment banReason : config.getBanReasons()) {

			if (isBanIP) {
				ConfigurableItem reasonIPItem = config.getReasonIPItem().get();

				reasonIPItem.setMaterial(banReason.getMaterial());

				addContent(reasonIPItem.build(player, Map.of(
						"targetName", target.getName(),
						"targetIP", target.getAddress().getAddress().getHostAddress(),
						"reasonName", banReason.getReason(),
						"reasonDescription", banReason.getDescription()
				)), e -> {

					new BanDurationsInventory(player, target, banReason, true).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});

			} else {
				ConfigurableItem reasonPlayerItem = config.getReasonPlayerItem().get();

				reasonPlayerItem.setMaterial(banReason.getMaterial());

				addContent(reasonPlayerItem.build(player, Map.of(
						"targetName", target.getName(),
						"reasonName", banReason.getReason(),
						"reasonDescription", banReason.getDescription()
				)), e -> {

					new BanDurationsInventory(player, target, banReason, false).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new BanPlayersInventory(player, isBanIP).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}