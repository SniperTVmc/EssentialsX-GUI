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
import com.earth2me.essentials.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.faststats.core.ErrorTracker;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.ConfigurationFile;
import fr.snipertvmc.essentialsxgui.libraries.bstats.Metrics;
import fr.snipertvmc.essentialsxgui.managers.*;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.byteflux.libby.BukkitLibraryManager;
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

	private MCServerVersion mcServerVersion;
	private Metrics bStatsMetrics;

	private BukkitAudiences bukkitAudiences;

	private dev.faststats.core.Metrics fastStatsMetrics;
	private final ErrorTracker fastStatsErrorTracker = ErrorTracker.contextAware();


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

		mcServerVersion = MCServerVersion.getMCServerVersion();
		bStatsMetrics = new Metrics(this, 26314);

		bukkitAudiences  = BukkitAudiences.create(this);


		// FILES LOADING
		filesManager.loadFiles();


		// DATABASE MANAGER INITIALIZATION
		// Must be done after loading configuration file
		databaseManager = new DatabaseManager();

		// COMMAND REGISTRATION
		LiteralCommandNode<CommandSourceStack> buildCommand = Commands.literal("essentialsxgui")
				.then(Commands.literal("debug"))
				.then(Commands.literal("reload"))
				.then(Commands.literal("about"))
				.then(Commands.literal("help"))
				.build();
			this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
				// register your commands here ...
				commands.registrar().register(buildCommand);
		});




		// LOAD PLUGIN
		boolean successfullyLoaded = loadingManager.loadPlugin(filesManager.getConfiguration().isDetailedLoading());


		// SERVER INITIALIZATION
		if (successfullyLoaded) serverManager = new ServerManager();
		fastStatsMetrics.ready();


		// PLUGIN LOADING COMPLETED
		long endTime = System.currentTimeMillis();
		long loadingTime = endTime - startTime;

		if (!successfullyLoaded) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlugin will be disabled due to loading errors.");
			ConsoleLogger.console("");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7The plugin has been §floaded §7correctly in §f" + loadingTime + "ms§7.");
		ConsoleLogger.console("");
	}

	private <O extends LifecycleEventOwner> LifecycleEventManager<O> getLifecycleManager() {
        return null;
    }


	// -------------------------------------------------- //


	@Override
	public void onDisable() {


		// PLUGIN UNLOADING
		long startTime = System.currentTimeMillis();

		ConsoleLogger.console("");
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Plugin unloading...");


		// METRICS SHUTDOWN
		fastStatsMetrics.shutdown();


		// UNLOAD PLUGIN
		loadingManager.unloadPlugin(filesManager.getConfiguration().isDetailedLoading());


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
		return filesManager.getConfiguration();
	}
	public File getPluginFile() {
		return getFile();
	}


	// CONSTANTS VARIABLES
	public MCServerVersion getMCServerVersion() {
		return mcServerVersion;
	}
	public Metrics getbStatsMetrics() {
		return bStatsMetrics;
	}

	public BukkitAudiences getBukkitAudiences() {
		return bukkitAudiences;
	}

	public dev.faststats.core.Metrics getFastStatsMetrics() {
		return fastStatsMetrics;
	}
	public void setFastStatsMetrics(dev.faststats.core.Metrics fastStatsMetrics) {
		this.fastStatsMetrics = fastStatsMetrics;
	}
	public ErrorTracker getFastStatsErrorTracker() {
		return fastStatsErrorTracker;
	}


	// -------------------------------------------------- //
}