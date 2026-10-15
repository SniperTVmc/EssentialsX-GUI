package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {


	// -------------------------------------------------- //


	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		Player player = event.getPlayer();
		String message = event.getMessage();

		if (Main.getInstance().getChatManager().isTyping(player.getUniqueId())) {
			Main.getInstance().getChatManager().accept(player, message);
			event.setCancelled(true);
		}
	}


	// -------------------------------------------------- //
}
