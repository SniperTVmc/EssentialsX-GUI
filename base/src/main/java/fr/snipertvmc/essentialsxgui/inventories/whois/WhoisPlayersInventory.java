package fr.snipertvmc.essentialsxgui.inventories.whois;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.ConfigurableWhoisPlayersInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WhoisPlayersInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	private final ConfigurableWhoisPlayersInventory config = (ConfigurableWhoisPlayersInventory) Main.getInstance().getInventory(EXGInventory.WHOIS_PLAYERS);


	// -------------------------------------------------- //


	public WhoisPlayersInventory(Player player) {
		super(
				Main.getInstance().getInventory(EXGInventory.WHOIS_PLAYERS).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WHOIS_PLAYERS).getTitle()
						.build(player)
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.insertCloseItem(player, config.getCloseItem(), this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
				.map(p -> (Player) p)
				.sorted(Comparator.comparing(Player::getName))
				.toList();

		for (Player onlinePlayer : onlinePlayers) {

			ConfigurableItem playerItem = config.getPlayerItem().get();
			addContent(playerItem
					.build(player, Map.of(
							"targetName", onlinePlayer.getName()
					)), e -> {

				new WhoisViewInventory(player, onlinePlayer).open(player);
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
