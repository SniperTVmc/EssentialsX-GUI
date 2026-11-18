package fr.snipertvmc.essentialsxgui.inventories.punishment.ban;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.punishment.ban.ConfigurableBanPlayersInventory;
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

public class BanPlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableBanPlayersInventory config = (ConfigurableBanPlayersInventory) Main.getInstance().getInventory(EXGInventory.BAN_PLAYERS);


	// -------------------------------------------------- //


	public BanPlayersInventory(Player player, boolean isBanIP) {
		super(
				Main.getInstance().getInventory(EXGInventory.BAN_PLAYERS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.BAN_PLAYERS).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());

		String playerIP = player.getAddress().getAddress().getHostAddress();

		List<Player> bannablePlayers = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.filter(p -> !p.hasPermission("essentials.exempt") && !p.hasPermission("essentials.ban.exempt"))
				.filter(p -> !isBanIP || !Objects.equals(p.getAddress().getAddress().getHostAddress(), playerIP))
				.filter(p -> !Objects.equals(p.getName(), player.getName()))
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		if (bannablePlayers.isEmpty() && config.getNoPlayersItem().isEnabled()) {
			addContent(config.getNoPlayersItem().build(player));
		}

		for (Player onlinePlayer : bannablePlayers) {

			if (isBanIP) {
				ConfigurableItem ipItem = config.getIpItem().get();
				addContent(ipItem
						.build(player, Map.of(
								"targetName", onlinePlayer.getName(),
								"targetIP", onlinePlayer.getAddress().getAddress().getHostAddress()
						)), e -> {

					new BanReasonsInventory(player, onlinePlayer, true).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});

			} else {

				ConfigurableItem playerItem = config.getPlayerItem().get();
				addContent(playerItem
						.build(player, Map.of(
								"targetName", onlinePlayer.getName()
						)), e -> {

					new BanReasonsInventory(player, onlinePlayer, false).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				});
			}
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
