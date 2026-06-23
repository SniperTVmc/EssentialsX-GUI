package fr.snipertvmc.essentialsxgui.managers;

import com.earth2me.essentials.utils.VersionUtil;
import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.data.Metric;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGPermission;
import fr.snipertvmc.essentialsxgui.libraries.bstats.Metrics;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInvManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.RegisterUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.UpdateUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class LoadingManager {


	// -------------------------------------------------- //


	private boolean pluginReady = false;
	private boolean isPlaceholderAPISupported = false;
	private final long startTimestamp = System.currentTimeMillis();


	// -------------------------------------------------- //


	public boolean loadPlugin(boolean detailedLoading) {


		// CHECK IF THE PLUGIN IS READY
		if (pluginReady) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cThe plugin is already loaded.");
			return false;
		}


		// DATABASES CONNECTION
		Main.getInstance().getDatabaseManager().connectAllDatabases();


		// SERVER CONFIGURATION ANALYSIS
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Analyzing server configuration...");
		if (!isServerReady()) {
			return false;
		}
		checkForServerVersionSupport();
		if (!Main.getInstance().getDescription().getVersion().contains("-dev")) {
			startUpdateCheckerTask();
		}
		checkForPlaceholderAPISupport();
		checkForUpdates(false);
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server configuration analysis §fcompleted§7.");


		// GLOBAL DATA INITIALIZATION
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialisation of global data...");
		FastInvManager.register(Main.getInstance());
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Initialization of global data §fcompleted§7.");


		// COMMANDS REGISTRATION
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registering commands...");
		int registeredCommands = RegisterUtils.registerCommands("fr.snipertvmc.essentialsxgui.commands");
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredCommands + " commands§7.");


		// EVENTS REGISTRATION
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of events...");
		int registeredEvents = RegisterUtils.registerEvents("fr.snipertvmc.essentialsxgui.events");
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Registration of §f" + registeredEvents + " events§7.");


		// METRICS CHARTS LOADING
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loading metrics charts...");
		loadMetricsCharts();
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Metrics charts loading §fcompleted§7.");


		// PLUGIN LOADING COMPLETED
		pluginReady = true;
		return true;
	}


	// -------------------------------------------------- //


	public void unloadPlugin(boolean detailedLoading) {


		// CHECK IF THE PLUGIN IS NOT READY
		if (!pluginReady) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cThe plugin is already unloaded.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cWell... it's impossible to unload an unloaded plugin ¯\\_(ツ)_/¯");
			return;
		}


		// STOP TASKS
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Stopping tasks...");
		stopUpdateCheckerTask();
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Tasks stopping §fcompleted§7.");


		// FINAL DATA SAVING
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Data saving...");
		Main.getInstance().getPlayerManager().saveAll();
		Main.getInstance().getServerManager().save();
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Data saving §fcompleted§7.");


		// DATABASES DISCONNECTION
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Disconnecting databases...");
		Main.getInstance().getDatabaseManager().disconnectAllDatabases();
		if (detailedLoading) ConsoleLogger.console("\t§6EssentialsX-GUI: §7Databases disconnection §fcompleted§7.");
	}


	// -------------------------------------------------- //


	public void reloadPlugin(CommandSender sender) {

		Set<CommandSender> commandSenders = new HashSet<>();
		commandSenders.add(sender);
		if (sender instanceof Player) commandSenders.add(Bukkit.getConsoleSender());

		boolean detailedLoading = Main.getInstance().getConfiguration().isDetailedLoading();
		int totalErrors = 0;

		TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.PLUGIN_RELOADING, null));


		// FILES RELOADING
		totalErrors += reloadFiles(commandSenders, detailedLoading);


		// DATABASE RELOADING
		totalErrors += reloadDatabase(commandSenders, detailedLoading);


		// TASK RELOADING
		totalErrors += reloadTasks(commandSenders, detailedLoading);


		// DATA RELOADING
		totalErrors += reloadData(commandSenders, detailedLoading);


		TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.PLUGIN_RELOADED, Map.of(
						"errors", String.valueOf(totalErrors)))
		);

		if (totalErrors > 0) {
			TextUtils.sendMessageToCommandSender(commandSenders,
					MessagesUtils.getString(EXGMessage.PLUGIN_RELOADED_WITH_ERRORS, null));
		}
	}


	private int reloadFiles(Set<CommandSender> commandSenders, boolean detailedLoading) {
		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.FILES_RELOADING, null));

		int errors = Main.getInstance().getFilesManager().reloadFiles();

		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.FILES_RELOADED, Map.of(
						"errors", String.valueOf(errors)))
		);

		return errors;
	}


	private int reloadDatabase(Set<CommandSender> commandSenders, boolean detailedLoading) {
		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.DATABASE_RELOADING, null));

		Main.getInstance().getDatabaseManager().disconnectAllDatabases();
		Main.getInstance().getDatabaseManager().updateDatabaseStorage();
		Main.getInstance().getDatabaseManager().connectAllDatabases();

		int errors = Main.getInstance().getDatabaseManager().getStorage().isConnected() ? 0 : 1;

		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.DATABASE_RELOADED, Map.of(
						"errors", String.valueOf(errors)))
		);

		return errors;
	}


	private int reloadTasks(Set<CommandSender> commandSenders, boolean detailedLoading) {
		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.TASKS_RELOADING, null));

		Main.getInstance().getEXGServer().getBalanceTop().stopUpdateTask();
		Main.getInstance().getEXGServer().getBalanceTop().startUpdateTask();

		boolean balanceTopEnabled = Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled();
		int errors = (!balanceTopEnabled
				|| Main.getInstance().getEXGServer().getBalanceTop().isUpdateTaskRunning()) ? 0 : 1;

		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.TASKS_RELOADED, Map.of(
						"errors", String.valueOf(errors)))
		);

		return errors;
	}


	private int reloadData(Set<CommandSender> commandSenders, boolean detailedLoading) {
		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.DATA_RELOADING, null));

		if (Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled()) {
			Main.getInstance().getEXGServer().getBalanceTop().forceUpdate();
		}
		Main.getInstance().getEXGServer().getWorth().loadItemsWorth();

		int errors = Main.getInstance().getDatabaseManager().getStorage().isConnected() ? 0 : 1;

		if (detailedLoading) TextUtils.sendMessageToCommandSender(commandSenders,
				MessagesUtils.getString(EXGMessage.DATA_RELOADED, Map.of(
						"errors", String.valueOf(errors)))
		);

		return errors;
	}


	// -------------------------------------------------- //


	public boolean isServerReady() {

		String essentialsVersionRequired = Main.getInstance().getHookManager().getEssentialsHook().getMinimumVersionRequired();

		if (Main.getInstance().getEssentials() == null) {

			ConsoleLogger.console("\t§6EssentialsX-GUI: §cEssentialsX is not installed on the server.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cPlease install EssentialsX (" + essentialsVersionRequired + ") to use this plugin.");
			return false;
		}

		boolean essentialsVersionSupported = Main.getInstance().getHookManager().getEssentialsHook().isEssentialsVersionSupported();
		String version = Main.getInstance().getEssentials().getDescription().getVersion();
		String essentialsVersionColor = essentialsVersionSupported ? "§a" : "§c";

		ConsoleLogger.console("\t§6EssentialsX-GUI: §7EssentialsX version found: " + essentialsVersionColor + version);

		if (!essentialsVersionSupported) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cCurrent version is not supported. §4(Minimum version required: §4" + essentialsVersionRequired + ")");
			return false;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §aThis EssentialsX version is supported by EssentialsX-GUI.");

		Main.getInstance().getEssentials();
		Main.getInstance().getEssentialsManager();
		return true;
	}


	public void checkForServerVersionSupport() {

		VersionUtil.SupportStatus supportStatus = VersionUtil.getServerSupportStatus();

		boolean serverSupported = supportStatus == VersionUtil.SupportStatus.FULL || supportStatus == VersionUtil.SupportStatus.LIMITED;
		String serverVersionColor = serverSupported ? "§a" : "§6";

		String version = VersionUtil.getServerBukkitVersion().getMajor() + "." + VersionUtil.getServerBukkitVersion().getMinor() + "." + VersionUtil.getServerBukkitVersion().getPatch();
		ConsoleLogger.console("\t§6EssentialsX-GUI: §7Server version found: " + serverVersionColor + version);

		if (supportStatus == VersionUtil.SupportStatus.NMS_CLEANROOM
				|| supportStatus == VersionUtil.SupportStatus.STUPID_PLUGIN
				||  supportStatus == VersionUtil.SupportStatus.UNSTABLE
				|| supportStatus == VersionUtil.SupportStatus.DANGEROUS_FORK) {

			ConsoleLogger.console("\t§6EssentialsX-GUI: §eBe careful, the version may be not fully supported or may cause issues.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §ePlease check the documentation for more information about this error.");
			return;
		}

		if (supportStatus == VersionUtil.SupportStatus.OUTDATED) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eThis server version is outdated and may not be fully supported by EssentialsX-GUI.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §ePlease consider updating to a newer version.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eA list of fully supported versions is available on the plugin page.");
			return;
		}

		ConsoleLogger.console("\t§6EssentialsX-GUI: §aThis server version is fully supported by EssentialsX-GUI.");
	}


	// -------------------------------------------------- //


	private boolean updateAvailable = false;
	private String latestVersionAvailable = null;


	public void checkForUpdates(boolean dontFlood) {

		String currentVersion = Main.getInstance().getDescription().getVersion();
		if (currentVersion.contains("-dev") && !dontFlood) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §5You are using a development version of EssentialsX-GUI.");
			ConsoleLogger.console("\t§6EssentialsX-GUI: §dSome features may not work as expected, and bugs may be present.");
			return;
		}

		if (!Main.getInstance().getConfiguration().checkForUpdates()) {
			return;
		}

		String latestVersionAvailable = UpdateUtils.getLatestVersionTag();
		if (latestVersionAvailable == null) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §cFailed to check for updates.");
			return;
		}

		if (currentVersion.equals(latestVersionAvailable)) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7You are using the §alatest §7version of EssentialsX-GUI.");
			updateAvailable = false;
			this.latestVersionAvailable = null;

		} else {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §eA new version of EssentialsX-GUI is available: §f" + latestVersionAvailable);
			ConsoleLogger.console("\t§6EssentialsX-GUI: §6Please update to the latest version for new features and bug fixes.");

			updateAvailable = true;
			this.latestVersionAvailable = latestVersionAvailable;
		}
	}


	public void alertPlayerForUpdate(Player player) {

		if (!updateAvailable) {
			return;
		}

		if (!player.hasPermission(EXGPermission.ADMIN_UPDATE.get())) {
			return;
		}

		TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ALERT_UPDATE_AVAILABLE, Map.of(
				"currentVersion", Main.getInstance().getDescription().getVersion(),
				"latestVersion", latestVersionAvailable
		)));
	}


	// -------------------------------------------------- //


	private BukkitTask checkForUpdatesTask = null;


	public void startUpdateCheckerTask() {

		if (checkForUpdatesTask != null) {
			return;
		}

		long checkIntervalTicks = 20L * 60L * 60L;

		checkForUpdatesTask = Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(
				Main.getInstance(),
				() -> checkForUpdates(true),
				checkIntervalTicks,
				checkIntervalTicks
		);
	}


	public void stopUpdateCheckerTask() {

		if (checkForUpdatesTask == null) {
			return;
		}

		checkForUpdatesTask.cancel();
		checkForUpdatesTask = null;
	}


	// -------------------------------------------------- //


	public void checkForPlaceholderAPISupport() {

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			ConsoleLogger.console("\t§6EssentialsX-GUI: §bPlaceholderAPI found. Placeholder support enabled.");
			isPlaceholderAPISupported  = true;

		} else {
			isPlaceholderAPISupported = false;
		}
	}


	// -------------------------------------------------- //


	private void loadMetricsCharts() {


		// --- Data Metrics --- //

		Callable<String> essentialsVersionData = () -> Main.getInstance().getEssentials() != null ?
				Main.getInstance().getEssentials().getDescription().getVersion() : "Other";

		Callable<String> storageTypeData = () -> Main.getInstance().getConfiguration().getStorageType() != null ?
				Main.getInstance().getConfiguration().getStorageType() : "Other";



		// --- bStats Metrics //

		// EssentialsX Version Chart
		Main.getInstance().getbStatsMetrics().addCustomChart(new Metrics.SimplePie("essentialsx_version", essentialsVersionData));
		// Storage Type Chart
		Main.getInstance().getbStatsMetrics().addCustomChart(new Metrics.SimplePie("storage_type", storageTypeData));


		// -- FastStats Metrics //

		Main.getInstance().setFastStatsMetrics(BukkitMetrics.factory()
				.token("1c3f12060cd797a90580e386d61bd7e5")

				// EssentialsX Version Chart
				.addMetric(Metric.string("essentialsx_version", essentialsVersionData))
				// Storage Type Chart
				.addMetric(Metric.string("storage_type", storageTypeData))

				.errorTracker(Main.getInstance().getFastStatsErrorTracker())

				.create(Main.getInstance()));
	}


	// -------------------------------------------------- //


	public boolean isPluginReady() {
		return pluginReady;
	}
	public boolean isPlaceholderAPISupported() {
		return isPlaceholderAPISupported;
	}
	public long getUptimeInMilliseconds() {
		return System.currentTimeMillis() - startTimestamp;
	}
	public long getUptimeInSeconds() {
		return getUptimeInMilliseconds() / 1000L;
	}
	public long getUptimeInHours() {
		return getUptimeInSeconds() / 3600L;
	}


	// -------------------------------------------------- //
}
