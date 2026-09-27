package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class WhoisConfigurationParser {


	// -------------------------------------------------- //


	public static int isWhoisModuleValid(ConfigurationSection whoisSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(whoisSection, "whois", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(whoisSection.get("enabled"), "whois.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isWhoisModuleEnabled()) return 0;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
