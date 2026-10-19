package fr.snipertvmc.essentialsxgui.inventories.punishment.kick;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.kick.ConfigurableKickPlayersInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KickPlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableKickPlayersInventory config = (ConfigurableKickPlayersInventory) Main.getInstance().getInventory(EXGInventory.KICK_PLAYERS);


	// -------------------------------------------------- //


	public KickPlayersInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.KICK_PLAYERS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.KICK_PLAYERS).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		List<Player> kickablePlayers = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.filter(p -> !p.hasPermission("essentials.exempt") || p.hasPermission("essentials.kick.exempt"))
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		if (!kickablePlayers.isEmpty() && config.getKickAllItem().isEnabled()) {
			addContent(config.getKickAllItem().build(player, Map.of(
					"playersCount", String.valueOf(kickablePlayers.size())
			)), e -> {

				new KickReasonsInventory(player, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}

		if (kickablePlayers.isEmpty() && config.getNoPlayersItem().isEnabled()) {
			addContent(config.getNoPlayersItem().build(player));
		}

		for (Player onlinePlayer : kickablePlayers) {

			ConfigurableItem playerItem = config.getPlayerItem().get();
			addContent(playerItem
					.build(player, Map.of(
							"targetName", onlinePlayer.getName()
					)), e -> {

				new KickReasonsInventory(player, onlinePlayer).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
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
