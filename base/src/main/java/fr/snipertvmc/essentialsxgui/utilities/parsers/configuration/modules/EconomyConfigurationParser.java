package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class EconomyConfigurationParser {


	// -------------------------------------------------- //


	public static int isEconomyModuleValid(ConfigurationSection economySection, boolean silence) {
		int errorsCount = 0;


		// Validate the economy section
		if (!ConfigurationPropertyParser.isSectionValid(economySection, "economy", silence)) return 1;


		// Get the economy sections
		ConfigurationSection balanceTopSection = economySection.getConfigurationSection("balanceTop");
		ConfigurationSection worthSection = economySection.getConfigurationSection("worth");
		ConfigurationSection ecoSection = economySection.getConfigurationSection("eco");
		ConfigurationSection sellSection = economySection.getConfigurationSection("sell");


		// Validate the economy module properties
		errorsCount += isBalanceTopModuleValid(balanceTopSection, silence);
		errorsCount += isWorthModuleValid(worthSection, silence);
		errorsCount += isEcoModuleValid(ecoSection, silence);
		errorsCount += isSellModuleValid(sellSection, silence);
		return errorsCount;
	}


	// -------------------------------------------------- //


	private static int isBalanceTopModuleValid(ConfigurationSection balanceTopSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(balanceTopSection, "economy.balanceTop", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(balanceTopSection.get("enabled"), "economy.balanceTop.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled()) return 0;
		if (!ConfigurationPropertyParser.isPositiveValueValid(balanceTopSection.get("updateInterval"), "economy.balanceTop.updateInterval", silence)) errorsCount++;
		return errorsCount;
	}


	private static int isWorthModuleValid(ConfigurationSection worthSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(worthSection, "economy.worth", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(worthSection.get("enabled"), "economy.worth.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isEconomyWorthModuleEnabled()) return 0;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(worthSection.get("searchWorthEntryType"), "economy.worth.searchWorthEntryType", silence)) errorsCount++;
		return errorsCount;
	}


	private static int isEcoModuleValid(ConfigurationSection ecoSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(ecoSection, "economy.eco", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(ecoSection.get("enabled"), "economy.eco.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isEconomyEcoModuleEnabled()) return 0;
		return errorsCount;
	}


	private static int isSellModuleValid(ConfigurationSection sellSection, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(sellSection, "economy.sell", silence)) return 1;
		if (!ConfigurationPropertyParser.isBooleanValid(sellSection.get("enabled"), "economy.sell.enabled", silence)) errorsCount++;
		if (!Main.getInstance().getConfiguration().isEconomySellModuleEnabled()) return 0;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
