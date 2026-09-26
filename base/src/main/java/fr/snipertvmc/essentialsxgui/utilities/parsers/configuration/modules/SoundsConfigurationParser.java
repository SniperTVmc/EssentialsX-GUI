package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import com.cryptomorin.xseries.XSound;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class SoundsConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isSoundsSectionValid(ConfigurationSection soundsSection, boolean silence) {


		// Validate the sounds section
		if (!ConfigurationPropertyParser.isSectionValid(soundsSection, "sounds", silence)) return false;


		// Validate the sounds properties
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isBooleanValid(soundsSection.get("enabled"), "sounds.enabled", silence)) return false;
		if (!soundsSection.getBoolean("enabled")) return true;
		if (!isSoundValid(soundsSection.get("guiOpen"), "sounds.guiOpen", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("guiClose"), "sounds.guiClose", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("guiBack"), "sounds.guiBack", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("guiClick"), "sounds.guiClick", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("guiPageChange"), "sounds.guiPageChange", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("actionSuccess"), "sounds.actionSuccess", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("actionCanceled"), "sounds.actionCanceled", silence)) isValid = false;
		if (!isSoundValid(soundsSection.get("actionFailure"), "sounds.actionFailure", silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //


	private static boolean isSoundValid(Object value, String configurationPath, boolean silence) {
		if (ConfigurationPropertyParser.isMissing(value, configurationPath, silence)) return false;
		if (ConfigurationPropertyParser.isNotTypeRequired(value, configurationPath, silence, String.class)) return false;
		if (XSound.matchXSound((String) value).isEmpty()) {
			if (!silence) {
				ConsoleLogger.error("Invalid configuration '" + configurationPath + "': the property contains an invalid sound.");
			}
			return false;
		}
		return true;
	}


	// -------------------------------------------------- //
}
