package fr.snipertvmc.essentialsxgui.events.command;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;

public class EssentialsVersionCommandListener implements Listener {


	// -------------------------------------------------- //


	private final List<String> commands = List.of(
			"essentials version", "essentials ver",
			"essentials:essentials version", "essentials:essentials ver"
	);


	// -------------------------------------------------- //


	@EventHandler(ignoreCancelled = true)
	public void onEssentialsVersionCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().replace("/", "");
		if (commands.contains(command)) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> sendInfoMessage(event.getPlayer()), 5L);
		}
	}


	@EventHandler(ignoreCancelled = true)
	public void onEssentialsVersionCommand(ServerCommandEvent event) {
		String command = event.getCommand().replace("/", "");
		if (commands.contains(command)) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> sendInfoMessage(event.getSender()), 5L);
		}
	}


	// -------------------------------------------------- //


	private void sendInfoMessage(CommandSender sender) {
		sender.sendMessage("§3[§bⓘ§3] §fEssentialsX-GUI is an unofficial addon for EssentialsX");
		sender.sendMessage("      §8- §fHowever, the compatibility with it is guaranteed.");
		sender.sendMessage("      §8- §fIf you encounter any issues, use /exg about to report them.");
	}


	// -------------------------------------------------- //
}
