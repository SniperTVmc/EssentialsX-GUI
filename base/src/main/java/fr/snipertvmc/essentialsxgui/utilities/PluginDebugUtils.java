package fr.snipertvmc.essentialsxgui.utilities;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.*;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class PluginDebugUtils {


	// -------------------------------------------------- //


	public static void generateDebug(EXGPlayer exgPlayer) {

		File debugFile = createDebugFile();
		if (debugFile == null) return;

		try {
			String debugContent = generateDebugContent(exgPlayer);
			Files.write(debugFile.toPath(), debugContent.getBytes());

			if (exgPlayer == null) {
				ConsoleLogger.console("\t§6EssentialsX-GUI: §7A debug file has been generated: §f" + debugFile.getAbsolutePath());

			} else {
				exgPlayer.getPlayer().sendMessage("§aA debug file has been generated: §f" + debugFile.getAbsolutePath());
			}

		} catch (IOException e) {

			if (exgPlayer != null) {
				exgPlayer.getPlayer().sendMessage("§cAn error occurred while generating the debug file. Check console for more information.");
			}

			ConsoleLogger.exception(e);
			ConsoleLogger.warn("An error occurred while writing the debug file.");
			ConsoleLogger.warn("Please check the plugin's data folder permissions and try again.");
			ConsoleLogger.warn("Otherwise, please contact the plugin developer for assistance. (Use /exg about)");
		}
	}


	// -------------------------------------------------- //


	private static File createDebugFile() {

		String formattedDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		File debugFile = new File(Main.getInstance().getDataFolder(), "debug/ " + formattedDate + "_debug.txt");

		try {
			if (!debugFile.exists()) {
				debugFile.getParentFile().mkdirs();
				debugFile.createNewFile();
			}

		} catch (IOException e) {
			ConsoleLogger.exception(e);
			ConsoleLogger.warn("An error occurred while creating the debug file.");
			ConsoleLogger.warn("Please check the plugin's data folder permissions and try again.");
			ConsoleLogger.warn("Otherwise, please contact the plugin developer for assistance. (Use /exg about)");
			debugFile = null;
		}

		return debugFile;
	}


	// -------------------------------------------------- //


	private static String generateDebugContent(EXGPlayer exgPlayer) {


		// Top of the debug content
		StringBuilder content = new StringBuilder();
		content.append("""
				############################################################
				# +------------------------------------------------------+ #
				# |                        Debug                         | #
				# +------------------------------------------------------+ #
				############################################################
				
				""");

		String currentLocalDataTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
		String parisDateTime = ZonedDateTime.now(ZoneId.of("Europe/Paris")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));


		// Server information
		content.append("=== Server information ===\n");
		content.append("Name: ").append(Main.getInstance().getServer().getName()).append("\n");
		content.append("Version: ").append(Main.getInstance().getServer().getVersion()).append("\n");
		content.append("Bukkit Version: ").append(Main.getInstance().getServer().getBukkitVersion()).append("\n");
		content.append("EssentialsX: ").append(Main.getInstance().getEssentials().getDescription().getVersion()).append("\n");
		content.append("Plugin Version: ").append(Main.getInstance().getDescription().getVersion()).append("\n");
		content.append("Local date: ").append(currentLocalDataTime).append("\n");
		content.append("Paris date: ").append(parisDateTime).append("\n");


		// System information
		content.append("\n=== System information ===\n");

		// Java information
		Runtime.Version version = Runtime.version();
		content.append("Java: ").append(version).append("\n");
		content.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
		content.append("JVM: ").append(System.getProperty("java.vm.name"))
				.append(" (").append(System.getProperty("java.vm.version")).append(")\n");

		// Operating System information
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		content.append("OS: ").append(osBean.getName()).append("\n");
		content.append("OS Version: ").append(osBean.getVersion()).append("\n");
		content.append("OS Architecture: ").append(osBean.getArch()).append("\n");
		content.append("Available Processors: ").append(osBean.getAvailableProcessors()).append("\n");

		// RAM information
		long megabyte = 1024L * 1024L;
		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory() / megabyte;
		long totalMemory = runtime.totalMemory() / megabyte;
		long freeMemory = runtime.freeMemory() / megabyte;
		long usedMemory = totalMemory - freeMemory;

		content.append("JVM RAM (Used/Allocated in MB/Max): ")
				.append(usedMemory).append(" MB / ")
				.append(totalMemory).append(" MB / ")
				.append(maxMemory).append(" MB\n");

		// Disk information
		try {
			FileStore store = Files.getFileStore(Paths.get("."));
			long totalGb = store.getTotalSpace() / (1024L * 1024L * 1024L);
			long freeGb = store.getUsableSpace() / (1024L * 1024L * 1024L);
			content.append("Disk (Free/Total in GB): ")
					.append(freeGb).append(" GB / ")
					.append(totalGb).append(" GB\n");
		} catch (Exception ignored) {
			content.append("Disk: Information not available\n");
		}

		// Uptime information
		content.append("Uptime (ms/s/h): ")
				.append(Main.getInstance().getLoadingManager().getUptimeInMilliseconds()).append(" / ")
				.append(Main.getInstance().getLoadingManager().getUptimeInSeconds()).append(" / ")
				.append(Main.getInstance().getLoadingManager().getUptimeInHours()).append("\n");


		// Plugin information
		content.append("\n=== Plugin information ===\n");

		if (exgPlayer != null) {
			Set<EXGHome> homes = exgPlayer.getHomes();
			content.append(generateListDebug(homes, "Homes"));
		}

		Set<EXGKit> kits = Main.getInstance().getEXGServer().getKits();
		content.append(generateListDebug(kits, "Kits"));

		Set<EXGWarp> warps = Main.getInstance().getEXGServer().getWarps();
		content.append(generateListDebug(warps, "Warps"));

		boolean balanceTopEnabled = Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled();
		content.append("BalanceTop (Enabled: ").append(balanceTopEnabled).append("):\n");
		if (balanceTopEnabled) {
			EXGBalanceTop balanceTop = Main.getInstance().getEXGServer().getBalanceTop();
			content.append("- Update Interval: ").append(Main.getInstance().getConfiguration().getBalanceTopUpdateInterval()).append(" seconds\n");
			content.append("- Top Size: ").append(balanceTop.getBalanceTopEntries().size()).append(" player(s)\n");
			if (exgPlayer != null) {
				Pair<Integer, BigDecimal> playerRanking = balanceTop.getPlayerRanking(exgPlayer.getName());
				int rankingPosition = playerRanking.getLeft();
				BigDecimal balance = playerRanking.getRight();
				String playerBalance = NumberUtil.displayCurrency(balance, Main.getInstance().getEssentials());
				content.append("- Player ranking: ").append(rankingPosition > 0 ? rankingPosition : "Not ranked").append("\n");
				content.append("- Player balance: ").append(playerBalance).append("\n");
			}
		}


		// End of the debug content
		content.append("""
				
				############################################################
				# +------------------------------------------------------+ #
				# |                                                      | #
				# +------------------------------------------------------+ #
				############################################################
				""");


		// Return the debug content as a string
		return content.toString();
	}


	// -------------------------------------------------- //


	private static String generateListDebug(Set<?> icons, String listName) {
		StringBuilder content = new StringBuilder();
		content.append(listName).append(" (").append(icons.size()).append("):\n");
		for (Object iconObject : icons) {
			if (!(iconObject instanceof EXGIcon icon)) continue;
			content.append("- ").append(icon.getName()).append(" | ");
			content.append(icon.getMaterial()).append(" ( ").append(icon.getData()).append(") | ");
			content.append("CustomItemStack: ").append(icon.getCustomItemStack() != null ? "Yes" : "No").append(" | ");
			content.append("DisplayName: ").append(icon.getDisplayName() != null ? icon.getDisplayName() : "None").append("\n");
		}
		return content.toString();
	}


	// -------------------------------------------------- //
}
