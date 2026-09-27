package fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.modules;

import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationPropertyParser;
import org.bukkit.configuration.ConfigurationSection;

public class StorageConfigurationParser {


	// -------------------------------------------------- //


	public static int isStorageSectionValid(ConfigurationSection storageSection, boolean silence) {
		int errorsCount = 0;


		// Validate the storage section
		if (!ConfigurationPropertyParser.isSectionValid(storageSection, "storage", silence)) return 1;


		// Get the storage sections
		ConfigurationSection mysqlSection = storageSection.getConfigurationSection("mysql");


		// Validate the storage module properties
		if (!ConfigurationPropertyParser.isMultipleChoiceValueValid(storageSection.get("type"), "storage.type", silence)) return 1;
		if (storageSection.getString("type").equalsIgnoreCase("SQLite")) return 0;
		errorsCount += isMySQLSectionValid(mysqlSection, "storage.mysql", silence);
		return errorsCount;
	}


	// -------------------------------------------------- //


	private static int isMySQLSectionValid(ConfigurationSection mysqlSection, String configurationPath, boolean silence) {
		int errorsCount = 0;


		// Validate the MySQL section
		if (!ConfigurationPropertyParser.isSectionValid(mysqlSection, configurationPath, silence)) return 1;


		// Get the connection pool section
		ConfigurationSection connectionPoolSection = mysqlSection.getConfigurationSection("connectionPool");


		// Validate the MySQL section
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("host"), configurationPath + ".host", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveValueValid(mysqlSection.get("port"), configurationPath + ".port", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("database"), configurationPath + ".database", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("username"), configurationPath + ".username", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("password"), configurationPath + ".password", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("settings"), configurationPath + ".settings", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isStringValid(mysqlSection.get("tablePrefix"), configurationPath + ".tablePrefix", silence)) errorsCount++;
		errorsCount += isConnectionPoolSectionValid(connectionPoolSection, configurationPath + ".connectionPool", silence);
		return errorsCount;
	}


	private static int isConnectionPoolSectionValid(ConfigurationSection connectionPoolSection, String configurationPath, boolean silence) {
		int errorsCount = 0;
		if (!ConfigurationPropertyParser.isSectionValid(connectionPoolSection, configurationPath, silence)) return 1;
		if (!ConfigurationPropertyParser.isPositiveValueValid(connectionPoolSection, configurationPath + ".maximumPoolSize", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".minimumIdle", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".maxLifetime", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".keepaliveTime", silence)) errorsCount++;
		if (!ConfigurationPropertyParser.isPositiveOrZeroValueValid(connectionPoolSection, configurationPath + ".connectionTimeout", silence)) errorsCount++;
		return errorsCount;
	}


	// -------------------------------------------------- //
}
