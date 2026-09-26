package fr.snipertvmc.essentialsxgui.managers.database.storages;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.databases.EXGStorage;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.data.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLStorageManager implements EXGStorage {


	// -------------------------------------------------- //


	private HikariDataSource dataSource;


	// -------------------------------------------------- //


	public boolean connect() {

		if (isConnected()) {
			return true;
		}

		try {
			this.dataSource = DatabaseUtils.connectDatabase(
					Main.getInstance().getConfiguration().getStorageHost(),
					Main.getInstance().getConfiguration().getStoragePort(),
					Main.getInstance().getConfiguration().getStorageDatabase(),

					Main.getInstance().getConfiguration().getStorageUsername(),
					Main.getInstance().getConfiguration().getStoragePassword(),
					Main.getInstance().getConfiguration().getStorageSettings(),

					Main.getInstance().getConfiguration().getStorageMaximumPoolSize(),
					Main.getInstance().getConfiguration().getStorageMinimumIdle(),
					Main.getInstance().getConfiguration().getStorageMaxLifetime(),
					Main.getInstance().getConfiguration().getStorageKeepaliveTime(),
					Main.getInstance().getConfiguration().getStorageConnectionTimeout(),

					isMariaDB()
			);
			return true;

		} catch (HikariPool.PoolInitializationException e) {
			ConsoleLogger.exception(e);
			ConsoleLogger.error("------------------------- EssentialsX-GUI Report -------------------------");
			ConsoleLogger.error("Failed to connect to the database. Using SQLite as a fallback storage.");
			ConsoleLogger.error("Please check your configuration and ensure that the database server is running.");
			ConsoleLogger.error("For more information about the error, please check the exception details below.");
			ConsoleLogger.error("--------------------------------------------------");
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
		return true;
	}
	public boolean isMariaDB() {
		return Main.getInstance().getConfiguration().getStorageType().equals("MariaDB");
	}
	public boolean isSQLite() {
		return false;
	}


	// -------------------------------------------------- //
}
