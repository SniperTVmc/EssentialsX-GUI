package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;

import java.util.Map;

public class MessagesUtils {


	// -------------------------------------------------- //


	public static String getString(EXGMessage exgMessage) {
		return getString(exgMessage, null);
	}


	public static String getString(EXGMessage exgMessage, Map<String, String> variables) {

		String prefix = Main.getInstance().getFilesManager().getMessagesFile().getPrefix();
		String message = Main.getInstance().getFilesManager().getMessagesFile().getString(exgMessage.getPath(),
				"<red>Message not found! Try to reset your messages.yml file, if the problem persists, contact plugin support."
		);
		String finalMessage = message.replace("{prefix}", prefix);

		if (variables != null) {
			for (Map.Entry<String, String> entry : variables.entrySet()) {
				finalMessage = finalMessage.replace("{" + entry.getKey() + "}", entry.getValue());
			}
		}

		return finalMessage;
	}


	// -------------------------------------------------- //
}
