package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class EconomyConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isEconomyModuleValid(ConfigurationSection economySection, boolean silence) {


		// Validate the economy section
		if (!ConfigurationPropertyParser.isSectionValid(economySection, "economy", silence)) return false;


		// Get the economy sections
		ConfigurationSection balanceTopSection = economySection.getConfigurationSection("balanceTop");
		ConfigurationSection worthSection = economySection.getConfigurationSection("worth");
		ConfigurationSection ecoSection = economySection.getConfigurationSection("eco");
		ConfigurationSection sellSection = economySection.getConfigurationSection("sell");


		// Validate the economy module properties
		boolean isValid = true;
		if (!isBalanceTopModuleValid(balanceTopSection, silence)) isValid = false;
		if (!isWorthModuleValid(worthSection, silence)) isValid = false;
		if (!isEcoModuleValid(ecoSection, silence)) isValid = false;
		if (!isSellModuleValid(sellSection, silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //


	private static boolean isBalanceTopModuleValid(ConfigurationSection balanceTopSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(balanceTopSection, "economy.balanceTop", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(balanceTopSection.get("enabled"), "economy.balanceTop.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled()) return true;
		if (!ConfigurationPropertyParser.isPositiveValueValid(balanceTopSection.get("updateInterval"), "economy.balanceTop.updateInterval", silence)) isValid = false;
		return isValid;
	}


	private static boolean isWorthModuleValid(ConfigurationSection worthSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(worthSection, "economy.worth", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(worthSection.get("enabled"), "economy.worth.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isEconomyWorthModuleEnabled()) return true;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(worthSection.get("searchWorthEntryType"), "economy.worth.searchWorthEntryType", silence)) isValid = false;
		return isValid;
	}


	private static boolean isEcoModuleValid(ConfigurationSection ecoSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(ecoSection, "economy.eco", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(ecoSection.get("enabled"), "economy.eco.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isEconomyEcoModuleEnabled()) return true;
		return isValid;
	}


	private static boolean isSellModuleValid(ConfigurationSection sellSection, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isSectionValid(sellSection, "economy.sell", silence)) return false;
		if (!ConfigurationPropertyParser.isBooleanValid(sellSection.get("enabled"), "economy.sell.enabled", silence)) isValid = false;
		if (!Main.getInstance().getConfiguration().isEconomySellModuleEnabled()) return true;
		return isValid;
	}


	// -------------------------------------------------- //
}
