package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendMessageToCommandSender(CommandSender commandSender, String formattedMessage) {
		if (commandSender == null || formattedMessage == null || formattedMessage.isEmpty()) return;
		sendMessageToCommandSender(Set.of(commandSender), formattedMessage);
	}


	public static void sendMessageToCommandSender(Set<CommandSender> commandSenders, String formattedMessage) {
		if (formattedMessage == null || formattedMessage.isEmpty() || commandSenders.isEmpty()) return;
		Component component = parseAsComponent(formattedMessage);
		for (CommandSender sender : commandSenders) {
			getAudience(sender).sendMessage(component);
		}
	}


	public static String parseAsString(String formattedMessage) {
		if (formattedMessage == null || formattedMessage.isEmpty()) return "";
		return LegacyComponentSerializer.legacySection().serialize(parseAsComponent(formattedMessage));
	}


	public static List<String> parseAsString(List<String> formattedMessages) {
		if (formattedMessages == null) return List.of();
		return formattedMessages.stream().map(TextUtils::parseAsString).toList();
	}


	private static Component parseAsComponent(String formattedMessage) {
		if (TextUtils.hasLegacyFormat(formattedMessage)) {
			String convertedDisplayName = TextUtils.replaceAmpersand(formattedMessage);
			return LegacyComponentSerializer.legacySection().deserialize(convertedDisplayName);
		} else {
			return MiniMessage.miniMessage().deserialize(formattedMessage);
		}
	}


	// -------------------------------------------------- //


	private static Audience getAudience(CommandSender sender) {
		if (Main.getInstance().getLibraryManager().hasNativeAdventureSupport()) {
			return (Audience) sender;
		} else {
			return Main.getInstance().getLoadingManager().getBukkitAudiences().sender(sender);
		}
	}


	// -------------------------------------------------- //


	private static boolean hasLegacyFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return message.matches(".*[&§][0-9a-fk-orx].*");
	}


	private static String replaceAmpersand(String message) {
		if (message == null) return "";
		return message.replaceAll("(?i)&([0-9a-fk-orx])", "§$1");
	}


	public static String firstLetterToUpperCase(String string) {
		if (string == null || string.isEmpty()) return "";
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}


	// -------------------------------------------------- //
}
