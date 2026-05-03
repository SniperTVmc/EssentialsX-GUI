package fr.snipertvmc.essentialsxgui.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGPermission;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.PluginDebugUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BrigadierCmdEssentialsXGUI {
    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("essentialsxgui")
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    sendHelpMessage(sender);
                    return 0;
                })
                .then(Commands.literal("debug")
                        .executes(ctx ->{
                            CommandSender sender = ctx.getSource().getSender();
                            sendDebugLinkMessage(sender);
                            return 0;
                        }))
                .then(Commands.literal("reload")
                        .executes(ctx ->{
                            CommandSender sender = ctx.getSource().getSender();
                            reloadPlugin(sender);
                            return 0;
                        }))
                .then(Commands.literal("about")
                        .executes(ctx ->{
                            CommandSender sender = ctx.getSource().getSender();
                            sendAboutMessage(sender);
                            return 0;
                        }))
                .then(Commands.literal("help")
                        .executes(ctx ->{
                            CommandSender sender = ctx.getSource().getSender();
                            sendHelpMessage(sender);
                            return 0;
                        })
                );

    }

    // -------------------------------------------------- //


    public void sendHelpMessage(CommandSender commandSender) {

        commandSender.sendMessage("");
        commandSender.sendMessage("  §6EssentialsX-GUI §7- §fHelp");
        commandSender.sendMessage("");
        commandSender.sendMessage("    §8■ §7/exg help §7- §fDisplay this help message.");
        commandSender.sendMessage("    §8■ §7/exg about §7- §fDisplay information about the plugin.");
        commandSender.sendMessage("    §8■ §7/exg reload §7- §fReload the plugin files.");
        commandSender.sendMessage("    §8■ §7/exg debug §7- §fGet a debug link to help you or developers.");
        commandSender.sendMessage("");
    }


    public void sendAboutMessage(CommandSender commandSender) {

        commandSender.sendMessage("");
        commandSender.sendMessage("  §6EssentialsX-GUI §7- §fPlugin by §eSniper_TVmc");
        commandSender.sendMessage("    §8■ §7§oEssentialsX-GUI is an addon for EssentialsX that adds some GUIs to the plugin.");
        commandSender.sendMessage("");
        commandSender.sendMessage("    §8■ §7Version: §b" + Main.getInstance().getDescription().getVersion());
        commandSender.sendMessage("    §8■ §7Discord: §3discord.gg/fSzK79TAYf");
        commandSender.sendMessage("    §8■ §7Spigot: §6spigotmc.org/resources/127805");
        commandSender.sendMessage("    §8■ §7GitHub: §fgithub.com/SniperTVmc/EssentialsX-GUI");
        commandSender.sendMessage("");
    }


    public void sendDebugLinkMessage(CommandSender commandSender) {

        if (!commandSender.hasPermission(EXGPermission.CMD_EXG_DEBUG.get())) {
            TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.NO_PERMISSION));
            return;
        }

        if (!(commandSender instanceof Player player)) {
            TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.ONLY_FOR_PLAYERS, null));
            return;
        }

        EXGPlayer exgPlayer = Main.getInstance().getPlayerManager().getPlayer(player);
        PluginDebugUtils.sendDebugToPrivateBin(exgPlayer);
    }


    // -------------------------------------------------- //


    public void reloadPlugin(CommandSender commandSender) {

        if (!commandSender.hasPermission(EXGPermission.CMD_EXG_RELOAD.get())) {
            TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.NO_PERMISSION));
            return;
        }


        // FILES RELOADING
        TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.FILES_RELOADING, null));
        Main.getInstance().getFilesManager().reloadFiles();
        TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.FILES_RELOADED, null));


        // DATABASE RELOADING
        TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.DATABASE_RELOADING, null));

        Main.getInstance().getDatabaseManager().disconnectAllDatabases();
        Main.getInstance().getDatabaseManager().updateDatabaseStorage();
        Main.getInstance().getDatabaseManager().connectAllDatabases();

        String newStorageType = Main.getInstance().getConfiguration().getStorageType();
        TextUtils.sendMessageToCommandSender(commandSender, MessagesUtils.getString(EXGMessage.DATABASE_RELOADED, Map.of("newStorageType", newStorageType)));
    }


    // -------------------------------------------------- //
}


