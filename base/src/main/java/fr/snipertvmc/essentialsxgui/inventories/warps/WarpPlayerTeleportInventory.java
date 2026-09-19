package fr.snipertvmc.essentialsxgui.inventories.warps;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpPlayerTeleportInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WarpPlayerTeleportInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWarpPlayerTeleportInventory config = (ConfigurableWarpPlayerTeleportInventory) Main.getInstance().getInventory(EXGInventory.WARP_PLAYER_TELEPORT);


	// -------------------------------------------------- //


	public WarpPlayerTeleportInventory(Player player, EXGWarp warp) {
		super(
				Main.getInstance().getInventory(EXGInventory.WARP_PLAYER_TELEPORT).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WARP_PLAYER_TELEPORT).getTitle()
						.build(player, Map.of(
								"warpName", warp.getName(),
								"warpDisplayName", warp.getDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		Main.getInstance().getServerDataManager().updateServerWarps();

		List<Player> targets = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player target : targets) {

			ConfigurableItem playerItem = config.getPlayerItem().get();
			addContent(playerItem
					.build(player, Map.of(
							"targetName", target.getName(),
							"warpName", warp.getName(),
							"warpDisplayName", warp.getDisplayName()
					)), e -> {

				e.getWhoClicked().closeInventory();
				player.performCommand("essentials:warp " + warp.getName() + " " + target.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WarpsAdminViewInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
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
