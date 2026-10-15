package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().initialize(player);

		if (Main.getInstance().getEssentials().getUser(player.getName()) != null) {
			Main.getInstance().getPlayerDataManager().updatePlayerHomes(exgPlayer);
		}

		Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
			Main.getInstance().getLoadingManager().alertPlayerForUpdate(player);
		}, 20L);
	}


	// -------------------------------------------------- //
}
