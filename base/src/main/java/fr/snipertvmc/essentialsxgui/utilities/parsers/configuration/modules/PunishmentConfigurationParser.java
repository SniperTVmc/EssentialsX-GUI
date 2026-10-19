package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class PunishmentConfigurationParser {


	// -------------------------------------------------- //


	public static int isPunishmentModuleValid(ConfigurationSection punishmentModule, boolean silence) {
		int errorsCount = 0;



		// Validate the punishment section
		if (!ConfigurationPropertyParser.isSectionValid(punishmentModule, "punishment", silence)) return 1;


		// Get the punishment sections
		ConfigurationSection kickSection = punishmentModule.getConfigurationSection("kick");


		// Validate the punishment module properties
		errorsCount += isKickModuleValid(kickSection, silence);
		return errorsCount;
	}


	// -------------------------------------------------- //


	private static int isKickModuleValid(ConfigurationSection kickSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(kickSection, "punishment.kick", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(kickSection.get("enabled"), "punishment.kick.enabled", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
