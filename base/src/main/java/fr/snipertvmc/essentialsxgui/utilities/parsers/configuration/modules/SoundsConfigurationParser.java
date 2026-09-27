package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import com.cryptomorin.xseries.XSound;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class SoundsConfigurationParser {


	// -------------------------------------------------- //


	public static int isSoundsSectionValid(ConfigurationSection soundsSection, boolean silence) {
		int errorsCount = 0;


		// Validate the sounds section
		if (!ConfigurationPropertyParser.isSectionValid(soundsSection, "sounds", silence)) return 1;


		// Validate the sounds properties
		if (!ConfigurationPropertyParser.isBooleanValid(soundsSection.get("enabled"), "sounds.enabled", silence)) return 1;
		if (!soundsSection.getBoolean("enabled")) return 0;
		if (!isSoundValid(soundsSection.get("guiOpen"), "sounds.guiOpen", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("guiClose"), "sounds.guiClose", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("guiBack"), "sounds.guiBack", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("guiClick"), "sounds.guiClick", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("guiPageChange"), "sounds.guiPageChange", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("actionSuccess"), "sounds.actionSuccess", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("actionCanceled"), "sounds.actionCanceled", silence)) errorsCount++;
		if (!isSoundValid(soundsSection.get("actionFailure"), "sounds.actionFailure", silence)) errorsCount++;
		return errorsCount;
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
