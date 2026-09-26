package fr.snipertvmc.essentialsxgui.managers.database.storages;

import com.zaxxer.hikari.HikariDataSource;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.databases.EXGStorage;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.data.DatabaseUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteStorageManager implements EXGStorage {


	// -------------------------------------------------- //


	private HikariDataSource dataSource;
	private File sqliteFile;


	// -------------------------------------------------- //


	public boolean connect() {

		if (isConnected()) {
			return true;
		}

		try {
			sqliteFile = new File(Main.getInstance().getDataFolder(), "database.sqlite");
			if (!sqliteFile.exists()) {
				sqliteFile.createNewFile();
			}

			dataSource = DatabaseUtils.connectDatabase(sqliteFile.getAbsolutePath());
			return true;

		} catch (IOException e) {
			ConsoleLogger.error("Failed to create SQLite database file: " + e.getMessage());
			return false;
		}
	}


	public void disconnect() {

		if (!isConnected()) {
			return;
		}

		DatabaseUtils.disconnectDatabase(this.dataSource);
		this.dataSource = null;
	}


	public boolean isConnected() {
		return DatabaseUtils.isConnected(this.dataSource);
	}


	// -------------------------------------------------- //


	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}


	public boolean isMySQL() {
		return false;
	}
	public boolean isMariaDB() {
		return false;
	}
	public boolean isSQLite() {
		return true;
	}


	// -------------------------------------------------- //
}
