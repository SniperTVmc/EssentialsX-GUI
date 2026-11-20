package fr.snipertvmc.essentialsxgui.inventories.punishment.mute;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.mute.ConfigurableMutePlayersInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MutePlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableMutePlayersInventory config = (ConfigurableMutePlayersInventory) Main.getInstance().getInventory(EXGInventory.MUTE_PLAYERS);


	// -------------------------------------------------- //


	public MutePlayersInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.MUTE_PLAYERS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.MUTE_PLAYERS).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());

		List<Player> mutablePlayers = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.filter(p -> !p.hasPermission("essentials.exempt") && !p.hasPermission("essentials.mute.exempt"))
				.filter(p -> !Objects.equals(p.getName(), player.getName()))
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		if (mutablePlayers.isEmpty() && config.getNoPlayersItem().isEnabled()) {
			addContent(config.getNoPlayersItem().build(player));
		}

		for (Player onlinePlayer : mutablePlayers) {

			ConfigurableItem playerItem = config.getPlayerItem().get();
			addContent(playerItem
					.build(player, Map.of(
							"targetName", onlinePlayer.getName()
					)), e -> {

				new MuteReasonsInventory(player, onlinePlayer).open(player);
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
