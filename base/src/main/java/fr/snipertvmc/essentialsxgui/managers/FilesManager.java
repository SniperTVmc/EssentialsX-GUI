package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.MessagesFile;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.parsers.configuration.ConfigurationParser;
import fr.snipertvmc.essentialsxgui.utilities.parsers.inventories.ConfigurableInventoryParser;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FilesManager {


	// -------------------------------------------------- //


	private ConfigurationFile configurationFile;
	private MessagesFile messagesFile;
	private Set<InventoryFile> inventoriesFiles = new HashSet<>();

	private final String configurationFileVersion = "2.0";
	private final String messagesFileVersion = "2.0";


	// -------------------------------------------------- //


	public void loadFiles() {
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading files...");

		int errorsCount = 0;
		errorsCount += loadAndCheckConfiguration(false);
		loadAndCheckMessages(false);
		errorsCount += loadAndCheckInventories(false);

		String errorsMessage = errorsCount > 0 ? "§c" + errorsCount + " error(s)" : "§a0 errors";

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files loading §fcompleted§7 with " + errorsMessage + ".");
	}


	public int reloadFiles() {
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Reloading files...");

		int errorsCount = 0;
		errorsCount += loadAndCheckConfiguration(true);
		loadAndCheckMessages(true);
		inventoriesFiles = new HashSet<>();
		errorsCount += loadAndCheckInventories(true);

		String errorsMessage = errorsCount > 0 ? "§c" + errorsCount + " error(s)" : "§a0 errors";

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Files reloading §fcompleted§7 with " + errorsMessage + ".");
		return errorsCount;
	}


	// -------------------------------------------------- //


	private boolean loadYAMLFile(String filePath) {

		try {
			File file = new File(Main.getInstance().getDataFolder(), filePath);

			if (!file.exists()) {
				file.getParentFile().mkdirs();
				Main.getInstance().saveResource(filePath, false);
			}

			YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(file);

			switch (filePath) {
				case "configuration.yml" -> configurationFile = new ConfigurationFile(yamlFile);
				case "messages.yml" -> messagesFile = new MessagesFile(yamlFile);
				default -> inventoriesFiles.add(new InventoryFile(yamlFile, filePath));
			}
			return true;

		} catch (Exception e) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cError while loading " + filePath + ": " + e.getMessage());
			return false;
		}
	}


	private void createBackupYAMLFile(String filePath) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String formattedDate = dateFormat.format(currentDate);

		File fileToBackup = new File(Main.getInstance().getDataFolder(), filePath);
		if (fileToBackup.exists()) {

			String fileName = filePath.split("/")[filePath.split("/").length - 1];
			File backupFile = new File(fileToBackup.getParent(), formattedDate + "_" + fileName);
			if (!backupFile.exists()) {

				try {
					boolean success = fileToBackup.renameTo(backupFile);

					if (success) {
						loadYAMLFile(filePath);
					}

				} catch (SecurityException e) {
					throw new RuntimeException("Error while creating backup file: " + e.getMessage());
				}
			}

		} else {
			loadYAMLFile(filePath);
		}
	}


	// -------------------------------------------------- //


	public void checkUpdateForFile(String filePath) {

		String fileVersion;
		String latestVersion;

		switch (filePath) {
			case "configuration.yml" -> {
				fileVersion = getConfigurationFile().getFileVersion();
				latestVersion = configurationFileVersion;
			}
			case "messages.yml" -> {
				fileVersion = getMessagesFile().getFileVersion();
				latestVersion = messagesFileVersion;
			}
			default -> {
				EXGInventory inventory = EXGInventory.getByPath(filePath);
				fileVersion = getInventoryFile(inventory).getFileVersion();
				latestVersion = inventory.getFileVersion();
			}
		};

		if (!fileVersion.equals(latestVersion)) {
			Main.getInstance().getFilesManager().createBackupYAMLFile(filePath);
		}
	}


	// -------------------------------------------------- //


	public int loadAndCheckConfiguration(boolean reload) {
		loadYAMLFile("configuration.yml");
		checkUpdateForFile("configuration.yml");
		int errorsCount = ConfigurationParser.isConfigurationValid(configurationFile, false);
		String label = reload ? "Reloaded" : "Loaded";
		if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fconfiguration.yml: §a" + label);
		return errorsCount;
	}


	public void loadAndCheckMessages(boolean reload) {
		loadYAMLFile("messages.yml");
		checkUpdateForFile("messages.yml");
		String label = reload ? "Reloaded" : "Loaded";
		if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §fmessages.yml: §a" + label);
	}


	public int loadAndCheckInventories(boolean reload) {
		int errorsCount = 0;

		for (EXGInventory inventory : EXGInventory.values()) {
			if (!loadYAMLFile(inventory.getFilePath())) continue;
			checkUpdateForFile(inventory.getFilePath());
			errorsCount += ConfigurableInventoryParser.isConfigurableInventoryValid(getInventoryFile(inventory), false);
			String label = reload ? "Reloaded" : "Loaded";
			if (configurationFile.isDetailedLoading()) ConsoleLogger.console("\t§6EssentialsX-GUI: §8- §f" + inventory.getFileName() + ".yml: §a" + label);
		}

		Main.getInstance().getInventoriesManager().loadInventories();
		return errorsCount;
	}


	// -------------------------------------------------- //


	public ConfigurationFile getConfigurationFile() {
		return configurationFile;
	}
	public MessagesFile getMessagesFile() {
		return messagesFile;
	}
	public InventoryFile getInventoryFile(EXGInventory inventory) {
		return inventoriesFiles.stream()
				.filter(inventoryFile -> inventoryFile.getFilePath().equals(inventory.getFilePath()))
				.findFirst()
				.orElse(null);
	}


	// -------------------------------------------------- //
}
