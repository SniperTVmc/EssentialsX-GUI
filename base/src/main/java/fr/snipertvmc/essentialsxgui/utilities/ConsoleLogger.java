package fr.snipertvmc.essentialsxgui.utilities;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.logging.Level;

public class ConsoleLogger {


	// -------------------------------------------------- //


	public static void console(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}


	public static void info(String message) {
		Bukkit.getLogger().info(message);
	}


	public static void warn(String message) {
		Bukkit.getLogger().warning(message);
	}


	public static void error(String message) {
		Bukkit.getLogger().severe(message);
	}


	public static void exception(Exception e) {
		Bukkit.getLogger().log(Level.SEVERE, "", e);
	}


	// -------------------------------------------------- //
}
