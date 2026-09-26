package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class WhoisConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isWhoisModuleValid(ConfigurationSection whoisSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(whoisSection, "whois", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(whoisSection.get("enabled"), "whois.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isWhoisModuleEnabled()) return true;
		return isValid;
	}


	// -------------------------------------------------- //
}
