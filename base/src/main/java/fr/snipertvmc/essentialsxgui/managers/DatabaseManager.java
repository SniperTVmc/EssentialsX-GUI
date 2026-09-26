package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.databases.EXGStorage;
import fr.snipertvmc.essentialsxgui.managers.database.storages.MySQLStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.storages.SQLiteStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.KitsTableManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.PlayerHomesTableManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.WarpsTableManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

public class DatabaseManager {


	// -------------------------------------------------- //


	private EXGStorage storage;

	private final KitsTableManager kitsTableManager = new KitsTableManager();
	private final PlayerHomesTableManager playerHomesTableManager = new PlayerHomesTableManager();
	private final WarpsTableManager warpsTableManager = new WarpsTableManager();


	// -------------------------------------------------- //


	public DatabaseManager() {
		updateDatabaseStorage(false);
	}


	public void updateDatabaseStorage(boolean forceUseSQLite) {


		String storageType = Main.getInstance().getConfiguration().getStorageType();
		if (forceUseSQLite || storageType.equals("SQLite")) {
			Main.getInstance().getLibraryManager().loadLibraries("SQLite");
			storage = getSQLite();
			return;
		}

		if (storageType.equalsIgnoreCase("MariaDB")) {
			Main.getInstance().getLibraryManager().loadLibraries("MariaDB");
		} else {
			Main.getInstance().getLibraryManager().loadLibraries("MySQL");
		}

		storage = getMySQL();
	}


	// -------------------------------------------------- //


	public void connectAllDatabases() {

		// Attempt to connect to the database
		boolean success = getStorage().connect();

		// If the connection fails and the storage type is MySQL or MariaDB, try to switch to SQLite
		if (!success && (getStorage().isMySQL() || getStorage().isMariaDB())) {
			ConsoleLogger.warn("Failed to connect to the MySQL/MariaDB database. Switching to SQLite...");
			updateDatabaseStorage(true);
			success = getStorage().connect();
		}

		// If the connection still fails, disable the plugin
		if (!success) {
			ConsoleLogger.error("Failed to connect to the database. Disabling the plugin...");
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			return;
		}

		// Initialize the table managers
		playerHomesTableManager.initialize(storage.isSQLite());
		kitsTableManager.initialize(storage.isSQLite());
		warpsTableManager.initialize(storage.isSQLite());
	}


	public void disconnectAllDatabases() {
		getStorage().disconnect();
	}


	// -------------------------------------------------- //


	public EXGStorage getStorage() {
		return storage;
	}

	public KitsTableManager getKitsTableManager() {
		return kitsTableManager;
	}
	public PlayerHomesTableManager getPlayerHomesTableManager() {
		return playerHomesTableManager;
	}
	public WarpsTableManager getWarpsTableManager() {
		return warpsTableManager;
	}


	// -------------------------------------------------- //


	private final MySQLStorageManager mySQLStorageManager = new MySQLStorageManager();
	private final SQLiteStorageManager sqLiteStorageManager = new SQLiteStorageManager();

	private MySQLStorageManager getMySQL() {
		return mySQLStorageManager;
	}
	private SQLiteStorageManager getSQLite() {
		return sqLiteStorageManager;
	}


	// -------------------------------------------------- //
}
