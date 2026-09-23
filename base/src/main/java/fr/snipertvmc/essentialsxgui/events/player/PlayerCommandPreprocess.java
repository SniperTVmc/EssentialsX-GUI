package fr.snipertvmc.essentialsxgui.events.player;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEcoAction;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGPermission;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.inventories.economy.*;
import fr.snipertvmc.essentialsxgui.inventories.homes.HomesInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.kits.KitsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.warps.WarpsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.warps.WarpsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.inventories.whois.WhoisPlayersInventory;
import fr.snipertvmc.essentialsxgui.inventories.whois.WhoisViewInventory;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class PlayerCommandPreprocess implements Listener {


	// -------------------------------------------------- //



	private final List<String> commands = List.of(

			// HOMES
			"home", "homes", "ehome", "ehomes",

			// KITS
			"kit", "kits", "ekit", "ekits",

			// WARPS
			"warp", "warps", "ewarp", "ewarps",

			// WHOIS
			"whois", "ewhois",

			// BALANCE TOP
			"baltop", "balancetop", "ebaltop", "ebalancetop",

			// WORTH
			"worth", "price", "eprice", "eworth",

			// ECONOMY
			"eco", "economy", "eeco", "eeconomy",

			// SELL
			"sell", "esell"
	);


	// -------------------------------------------------- //


	@EventHandler(ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();
		String command = event.getMessage().split(" ")[0].replaceFirst("/", "").replace("essentials:", "").toLowerCase();
		String[] args = event.getMessage().split(" ");

		if (!commands.contains(command)) return;

		switch (command) {


			//
			// HOMES
			//

			case "home", "homes", "ehome", "ehomes" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.home")) return;
				if (!Main.getInstance().getConfiguration().isHomesModuleEnabled()) return;
				event.setCancelled(true);

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_HOMES_INVENTORY, null));
				new HomesInventory(player, null, null).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// KITS
			//

			case "kit", "kits", "ekit", "ekits" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.kit")) return;
				if (!Main.getInstance().getConfiguration().isKitsModuleEnabled()) return;

				event.setCancelled(true);

				if (player.hasPermission(EXGPermission.KITS_ADMIN.get())
						&& Main.getInstance().getConfiguration().mustOpenKitAdminViewByDefault()) {

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ADMIN_KITS_INVENTORY, null));
					new KitsAdminViewInventory(player, null, null).open(player);

				} else {
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_PLAYER_KITS_INVENTORY, null));
					new KitsPlayerViewInventory(player, null, null).open(player);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// WARPS
			//

			case "warp", "warps", "ewarp", "ewarps" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.warp")) return;
				if (!Main.getInstance().getConfiguration().isWarpsModuleEnabled()) return;

				event.setCancelled(true);

				if (player.hasPermission(EXGPermission.WARPS_ADMIN.get())
						&& Main.getInstance().getConfiguration().mustOpenWarpAdminViewByDefault()) {

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ADMIN_WARPS_INVENTORY, null));
					new WarpsAdminViewInventory(player, null, null).open(player);

				} else {
					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_PLAYER_WARPS_INVENTORY, null));
					new WarpsPlayerViewInventory(player, null, null).open(player);
				}

				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
 			// WHOIS
			//

			case "whois", "ewhois" -> {

				if (!player.hasPermission("essentials.whois")) return;
				if (!Main.getInstance().getConfiguration().isWhoisModuleEnabled()) return;

				event.setCancelled(true);

				if (args.length > 1) {
					String targetPlayerName = args[1];

					Player targetPlayer = Main.getInstance().getServer().getPlayerExact(targetPlayerName);
					if (targetPlayer == null) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.PLAYER_NOT_FOUND, Map.of("player", targetPlayerName)));
						return;
					}

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_WHOIS_INVENTORY));
					new WhoisViewInventory(player, targetPlayer).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_WHOIS_INVENTORY));
				new WhoisPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// BALANCE TOP
			//

			case "balancetop", "baltop", "ebalancetop", "ebaltop" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.balancetop")) return;
				if (!Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled()) return;

				event.setCancelled(true);

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_BALANCE_TOP_INVENTORY, null));
				new BalanceTopInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// WORTH
			//

			case "worth", "price", "eworth", "eprice" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.worth")) return;
				if (!Main.getInstance().getConfiguration().isEconomyWorthModuleEnabled()) return;

				event.setCancelled(true);

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_WORTH_INVENTORY, null));
				new WorthInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// ECONOMY
			//

			case "eco", "economy", "eeco", "eeconomy" -> {

				if (args.length > 3) return;
				if (!player.hasPermission("essentials.eco")) return;
				if (!Main.getInstance().getConfiguration().isEconomyEcoModuleEnabled()) return;

				event.setCancelled(true);

				if (args.length > 2) {


					// Check the action argument
					String ecoActionArgument = args[1];
					EXGEcoAction ecoAction = switch (ecoActionArgument.toLowerCase()) {
						case "give", "add" -> EXGEcoAction.GIVE;
						case "take", "remove" -> EXGEcoAction.TAKE;
						case "set" -> EXGEcoAction.SET;
						case "reset" -> EXGEcoAction.RESET;
						default -> null;
					};

					if (ecoAction == null) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ARGUMENT_NOT_FOUND, Map.of(
								"argument", ecoActionArgument))
						);
						return;
					}


					// Check the target player argument
					String targetPlayerName = args[2];
					Player targetPlayer = Main.getInstance().getServer().getPlayerExact(targetPlayerName);
					if (targetPlayer == null) {
						TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.PLAYER_NOT_FOUND, Map.of("player", targetPlayerName)));
						return;
					}

					TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ECO_INVENTORY, null));
					new EcoAmountInventory(player, targetPlayer, ecoAction).open(player);
					SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
					return;
				}

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_ECO_INVENTORY, null));
				new EcoPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}


			//
			// SELL
			//

			case "sell", "esell" -> {

				if (args.length > 1) return;
				if (!player.hasPermission("essentials.sell") || !player.hasPermission("essentials.sell.bulk")) return;
				if (!Main.getInstance().getConfiguration().isEconomySellModuleEnabled()) return;

				event.setCancelled(true);

				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.OPENING_SELL_INVENTORY, null));
				new SellInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_OPEN);
			}
		}

	}


	// -------------------------------------------------- //
}
