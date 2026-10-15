package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);
		Main.getInstance().getPlayerManager().save(exgPlayer);
	}


	// -------------------------------------------------- //
}
