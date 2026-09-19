/*
 * EssentialsX-GUI - An unofficial GUI addon for EssentialsX
 * Copyright (C) 2025  Sniper_TVmc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package fr.snipertvmc.essentialsxgui;

import com.earth2me.essentials.Essentials;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.libraries.libby.bukkit.BukkitLibraryManager;
import fr.snipertvmc.essentialsxgui.managers.*;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {


	// -------------------------------------------------- //


	private static Main instance;

	private BukkitLibraryManager bukkitLibraryManager;

	private ChatManager chatManager;
	private DatabaseManager databaseManager;
	private FilesManager filesManager;
	private HookManager hookManager;
	private InventoriesManager inventoriesManager;
	private LibraryManager libraryManager;
	private LoadingManager loadingManager;
	private PlayerDataManager playerDataManager;
	private PlayerManager playerManager;
	private ServerDataManager serverDataManager;
	private ServerManager serverManager;

	private boolean pluginLoaded = false;


	// -------------------------------------------------- //


	@Override
	public void onEnable() {


		// PLUGIN LOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin loading...");


		// INSTANCE INITIALIZATION
		instance = this;


		// LIBRARY MANAGER INITIALIZATION
		// Must be done at the beginning to load essential libraries
		bukkitLibraryManager = new BukkitLibraryManager(this);
		libraryManager = new LibraryManager();


		// MANAGERS INITIALIZATION
		chatManager = new ChatManager();
		hookManager = new HookManager();
		inventoriesManager = new InventoriesManager();
		filesManager = new FilesManager();
		loadingManager = new LoadingManager();
		playerDataManager = new PlayerDataManager();
		playerManager = new PlayerManager();
		serverDataManager = new ServerDataManager();


		// FILES LOADING
		filesManager.loadFiles();


		// DATABASE MANAGER INITIALIZATION
		// Must be done after loading configuration file
		databaseManager = new DatabaseManager();


		// LOAD PLUGIN
		pluginLoaded = loadingManager.loadPlugin(filesManager.getConfigurationFile().isDetailedLoading());
		if (!pluginLoaded) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlugin will be disabled due to loading errors.");
			ConsoleLogger.console("");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}


		// SERVER INITIALIZATION
		serverManager = new ServerManager();


		// PLUGIN LOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §floaded §7correctly in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	@Override
	public void onDisable() {


		// PLUGIN UNLOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin unloading...");


		// UNLOAD PLUGIN
		if (pluginLoaded) loadingManager.unloadPlugin(filesManager.getConfigurationFile().isDetailedLoading());


		// PLUGIN UNLOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long unloadingTime = endTime - startTime;

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §funloaded §7correctly in §f" + unloadingTime + "ms§7.");
		ConsoleLogger.console("");
	}


	// -------------------------------------------------- //


	public static Main getInstance() {
		return instance;
	}

	public BukkitLibraryManager getBukkitLibraryManager() {
		return bukkitLibraryManager;
	}

	public EXGServer getEXGServer() {
		return serverManager.getEXGServer();
	}

	public ChatManager getChatManager() {
		return chatManager;
	}
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	public EssentialsManager getEssentialsManager() {
		return hookManager.getEssentialsHook().getEssentialsManager();
	}
	public FilesManager getFilesManager() {
		return filesManager;
	}
	public HookManager getHookManager() {
		return hookManager;
	}
	public InventoriesManager getInventoriesManager() {
		return inventoriesManager;
	}
	public LibraryManager getLibraryManager() {
		return libraryManager;
	}
	public LoadingManager getLoadingManager() {
		return loadingManager;
	}
	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	public ServerDataManager getServerDataManager() {
		return serverDataManager;
	}
	public ServerManager getServerManager() {
		return serverManager;
	}


	// -------------------------------------------------- //


	// SHORTCUTS
	public Essentials getEssentials() {
		return hookManager.getEssentialsHook().getEssentials();
	}
	public ConfigurationFile getConfiguration() {
		return filesManager.getConfigurationFile();
	}
	public File getPluginFile() {
		return getFile();
	}
	public ConfigurableInventory getInventory(EXGInventory inventory) {
		return inventoriesManager.getInventory(inventory);
	}


	// -------------------------------------------------- //
}