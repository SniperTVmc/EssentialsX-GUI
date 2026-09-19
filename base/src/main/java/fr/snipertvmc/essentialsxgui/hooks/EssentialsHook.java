package fr.snipertvmc.essentialsxgui.hooks;

import com.earth2me.essentials.Essentials;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.managers.EssentialsManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.plugin.Plugin;

public class EssentialsHook {


	// -------------------------------------------------- //


	private Essentials essentials;
	private EssentialsManager essentialsManager;


	// -------------------------------------------------- //


	public Essentials getEssentials() {

		if (essentials != null) return essentials;

		Plugin essentialsPlugin = Main.getInstance().getServer().getPluginManager().getPlugin("Essentials");

		if (essentialsPlugin instanceof Essentials) return essentials = (Essentials) essentialsPlugin;
		else return null;
	}


	public EssentialsManager getEssentialsManager() {

		if (essentialsManager != null) return essentialsManager;

		String version = essentials.getDescription().getVersion();
		String className;

		if (version.startsWith("2.21.2")) className = "fr.snipertvmc.essentialsxgui.v2_21_2.Essentials_v2_21_2";
		else className = "fr.snipertvmc.essentialsxgui.v2_22_0.Essentials_v2_22_0";

		String packageVersionName = className.split("\\.")[3];

		try {
			Class<?> clazz = Class.forName(className);
			essentialsManager = (EssentialsManager) clazz.getDeclaredConstructor().newInstance();
			ConsoleLogger.console("\t§6EssentialsX-GUI: §7Loaded EssentialsManager implementation: §a" + packageVersionName);
			return essentialsManager;

		} catch (Exception e) {
			ConsoleLogger.error("Failed to load EssentialsManager implementation for version " + version + ": " + e.getMessage());
			return null;
		}
	}


	// -------------------------------------------------- //


	private final String minimumVersionRequired = "2.22.0";


	public String getMinimumVersionRequired() {
		return minimumVersionRequired;
	}


	public boolean isEssentialsVersionSupported() {

		String version = essentials.getDescription().getVersion();
		String cleanVersion = version.split("-")[0];

		return isVersionGreaterOrEqual(cleanVersion);
	}


	public boolean isVersionGreaterOrEqual(String current) {
		String[] currentParts = current.split("\\.");
		String[] minimumParts = minimumVersionRequired.split("\\.");

		int length = Math.max(currentParts.length, minimumParts.length);

		for (int i = 0; i < length; i++) {
			int cur = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
			int min = (i < minimumParts.length) ? Integer.parseInt(minimumParts[i]) : 0;

			if (cur > min) return true;
			if (cur < min) return false;
		}
		return true;
	}


	// -------------------------------------------------- //
}
