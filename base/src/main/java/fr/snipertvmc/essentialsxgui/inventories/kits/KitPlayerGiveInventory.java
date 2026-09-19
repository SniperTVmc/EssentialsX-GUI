package fr.snipertvmc.essentialsxgui.inventories.kits;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.ConfigurableKitPlayerGiveInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KitPlayerGiveInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableKitPlayerGiveInventory config = (ConfigurableKitPlayerGiveInventory) Main.getInstance().getInventory(EXGInventory.KIT_PLAYER_GIVE);


	// -------------------------------------------------- //


	public KitPlayerGiveInventory(Player player, EXGKit kit) {
		super(
				Main.getInstance().getInventory(EXGInventory.KIT_PLAYER_GIVE).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KIT_PLAYER_GIVE).getTitle()
						.build(player, Map.of(
								"kitName", kit.getName(),
								"kitDisplayName", kit.getDisplayName()))
		);


		Main.getInstance().getServerDataManager().updateServerKits();


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		List<Player> targets = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player target : targets) {

			ConfigurableItem playerItem = config.getPlayerItem().get();
			addContent(playerItem
					.build(player, Map.of(
							"targetName", target.getName(),
							"kitName", kit.getName(),
							"kitDisplayName", kit.getDisplayName())
					), e -> {

				player.performCommand("essentials:kit " + kit.getName() + " " + target.getName());
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new KitsAdminViewInventory(player, null, null).open(player);
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
