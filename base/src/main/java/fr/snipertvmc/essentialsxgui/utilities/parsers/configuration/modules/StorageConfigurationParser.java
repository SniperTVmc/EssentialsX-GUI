package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class StorageConfigurationParser {


	// -------------------------------------------------- //


	public static boolean isStorageSectionValid(ConfigurationSection storageSection, boolean silence) {


		// Validate the storage section
		if (!ConfigurationPropertyParser.isSectionValid(storageSection, "storage", silence)) return false;


		// Get the storage sections
		ConfigurationSection mysqlSection = storageSection.getConfigurationSection("mysql");


		// Validate the storage module properties
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(storageSection.get("type"), "storage.type", silence)) return false;
		if (storageSection.getString("type").equalsIgnoreCase("SQLite")) return true;
		if (!isMySQLSectionValid(mysqlSection, "storage.mysql", silence)) isValid = false;
		return isValid;
	}


	// -------------------------------------------------- //


	private static boolean isMySQLSectionValid(ConfigurationSection mysqlSection, String configurationPath, boolean silence) {


		// Validate the MySQL section
		if (!ConfigurationPropertyParser.isSectionValid(mysqlSection, configurationPath, silence)) return false;


		// Get the connection pool section
		ConfigurationSection connectionPoolSection = mysqlSection.getConfigurationSection("connectionPool");


		// Validate the MySQL section
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("host"), configurationPath + ".host", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isPositiveValueValid(mysqlSection.get("port"), configurationPath + ".port", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("database"), configurationPath + ".database", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("username"), configurationPath + ".username", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("password"), configurationPath + ".password", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("settings"), configurationPath + ".settings", silence)) isValid = false;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("tablePrefix"), configurationPath + ".tablePrefix", silence)) isValid = false;
		if (!isConnectionPoolSectionValid(connectionPoolSection, configurationPath + ".connectionPool", silence)) isValid = false;
		return isValid;
	}


	private static boolean isConnectionPoolSectionValid(ConfigurationSection connectionPoolSection, String configurationPath, boolean silence) {
		boolean isValid = true;
		if (!ConfigurationPropertyParser.isPositiveValueValid(connectionPoolSection, configurationPath + ".maximumPoolSize", silence)) return false;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".minimumIdle", silence)) return false;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".maxLifetime", silence)) return false;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".keepaliveTime", silence)) return false;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".connectionTimeout", silence)) return false;
		return isValid;
	}


	// -------------------------------------------------- //
}
