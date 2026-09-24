package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationFile extends BaseFile {


	// -------------------------------------------------- //


	public ConfigurationFile(YamlConfiguration yamlConfiguration) {
		super(yamlConfiguration, "configuration.yml");
	}


	// -------------------------------------------------- //


	public boolean isDetailedLoading() {
		return getYamlConfiguration().getBoolean("general.detailedLoading", true);
	}

	public boolean checkForUpdates() {
		return getYamlConfiguration().getBoolean("general.checkForUpdates", true);
	}

	public ZoneId getDateTimezone() {
		String zoneIdString = getYamlConfiguration().getString("general.timezone", ZoneId.systemDefault().toString());
		try {
			return ZoneId.of(zoneIdString);

		} catch (Exception exception) {
			ConsoleLogger.warn("The timezone '" + zoneIdString + "' is not valid. Using the system default timezone instead.");
			return ZoneId.systemDefault();
		}
	}


	// -------------------------------------------------- //


	public boolean isHomesModuleEnabled() {
		return getYamlConfiguration().getBoolean("homes.enabled", false);
	}

	public boolean isKitsModuleEnabled() {
		return getYamlConfiguration().getBoolean("kits.enabled", false);
	}

	public boolean isWarpsModuleEnabled() {
		return getYamlConfiguration().getBoolean("warps.enabled", false);
	}

	public boolean isWhoisModuleEnabled() {
		return getYamlConfiguration().getBoolean("whois.enabled", false);
	}

	public boolean isEconomyBalanceTopModuleEnabled() {
		return getYamlConfiguration().getBoolean("economy.balanceTop.enabled", false);
	}
	public boolean isEconomyWorthModuleEnabled() {
		return getYamlConfiguration().getBoolean("economy.worth.enabled", false);
	}
	public boolean isEconomyEcoModuleEnabled() {
		return getYamlConfiguration().getBoolean("economy.eco.enabled", false);
	}
	public boolean isEconomySellModuleEnabled() {
		return getYamlConfiguration().getBoolean("economy.sell.enabled", false);
	}


	// -------------------------------------------------- //


	public EXGEntryType getEntryType(String module, String entry) {
		String typeString = getYamlConfiguration().getString(module + "." + entry, "CHAT");
		try {
			return EXGEntryType.valueOf(typeString);
		} catch (IllegalArgumentException exception) {
			ConsoleLogger.warn("The entry type '" + typeString + "' is not valid for the entry '" + entry + "' in the module '" + module + "'. Using the default type 'CHAT' instead.");
			return EXGEntryType.CHAT;
		}
	}

	public List<Pair<XMaterial, Integer>> getMaterialsList(String path) {

		List<String> materialsStringList = getYamlConfiguration().getStringList(path);
		List<Pair<XMaterial, Integer>> materialsList = new ArrayList<>();

		for (String materialString : materialsStringList) {
			String[] parts = materialString.split(":");
			String materialName = parts[0];
			int dataValue = 0;
			if (parts.length > 1) {
				try {
					dataValue = Integer.parseInt(parts[1]);
				} catch (NumberFormatException exception) {
					ConsoleLogger.warn("The data value '" + parts[1] + "' is not a valid integer for the material '" + materialName + "' in the path '" + path + "'. Using the default data value '0' instead.");
				}
			}
			if (XMaterial.matchXMaterial(materialName).isPresent()) {
				XMaterial material = XMaterial.matchXMaterial(materialName).get();
				materialsList.add(new Pair<>(material, dataValue));
			} else {
				ConsoleLogger.warn("The material '" + materialName + "' is not valid in the path '" + path + "'. Using the default material 'BEDROCK' instead.");
				materialsList.add(new Pair<>(XMaterial.BEDROCK, dataValue));
			}
		}

		return materialsList;
	}


	public String getCharacterList(String path) {
		if (path == null) return null;
		return getYamlConfiguration().getString(path, "regex:^[a-zA-Z0-9_ ]+$");
	}


	// -------------------------------------------------- //


	public int getMinNameLength() {
		return getYamlConfiguration().getInt("general.minNameLength", 3);
	}

	public int getMaxNameLength() {
		return getYamlConfiguration().getInt("general.maxNameLength", 32);
	}

	public boolean mustOpenKitAdminViewByDefault() {
		return getYamlConfiguration().getBoolean("kits.openKitAdminViewByDefault", false);
	}

	public boolean mustOpenWarpAdminViewByDefault() {
		return getYamlConfiguration().getBoolean("warps.openWarpAdminViewByDefault", false);
	}


	public int getDelayForTypingInChat() {
		return getYamlConfiguration().getInt("general.delayForTypingInChat", 10);
	}

	public boolean skipDataEntryProcess() {
		return getYamlConfiguration().getBoolean("general.skipDataEntryProcess", false);
	}

	public String getInstantCreationDefaultHomeName() {
		return getYamlConfiguration().getString("general.instantCreationDefaultValues.home_name", "home_%number%");
	}
	public String getInstantCreationDefaultKitName() {
		return getYamlConfiguration().getString("general.instantCreationDefaultValues.kit_name", "kit_%number%");
	}
	public long getInstantCreationDefaultKitDelay() {
		return getYamlConfiguration().getLong("general.instantCreationDefaultValues.kit_delay", 3600L);
	}
	public String getInstantCreationDefaultWarpName() {
		return getYamlConfiguration().getString("general.instantCreationDefaultValues.warp_name", "warp_%number%");
	}


	// -------------------------------------------------- //


	public int getBalanceTopUpdateInterval() {
		return getYamlConfiguration().getInt("economy.balanceTop.updateInterval", 60);
	}


	public boolean canSeeKit(Player player, String kitName) {
		return canSeeElement(player, kitName, "kits.kitsVisibleWithoutPermission", "essentials.kits." + kitName);
	}
	public boolean canSeeWarp(Player player, String warpName) {
		return canSeeElement(player, warpName, "warps.warpsVisibleWithoutPermission", "essentials.warps." + warpName);
	}

	private boolean canSeeElement(Player player, String elementName, String configPath, String permission) {
		Object visibleWithoutPermission = getYamlConfiguration().get(configPath);
		if (visibleWithoutPermission instanceof List<?> list) return list.contains(elementName) || player.hasPermission(permission);
		if (visibleWithoutPermission instanceof String str) return str.equals("ALL") || player.hasPermission(permission);
		return true;
	}


	public boolean hasCustomKitsOrder() {
		return getYamlConfiguration().get("kits.customKitsOrder") instanceof List<?>;
	}
	public List<String> getCustomKitsOrder() {
		return getYamlConfiguration().getStringList("kits.customKitsOrder");
	}


	public boolean hasCustomWarpsOrder() {
		return getYamlConfiguration().get("warps.customWarpsOrder") instanceof List<?>;
	}
	public List<String> getCustomWarpsOrder() {
		return getYamlConfiguration().getStringList("warps.customWarpsOrder");
	}


	public Pair<XMaterial, Integer> getDefaultHomeIcon() {
		return getDefaultIcon("homes.defaultHomeIcon", XMaterial.GRASS_BLOCK);
	}

	public Pair<XMaterial, Integer> getDefaultKitIcon() {
		return getDefaultIcon("kits.defaultKitIcon", XMaterial.CHEST);
	}

	public Pair<XMaterial, Integer> getDefaultWarpIcon() {
		return getDefaultIcon("warps.defaultWarpIcon", XMaterial.END_PORTAL_FRAME);
	}

	private Pair<XMaterial, Integer> getDefaultIcon(String path, XMaterial fallback) {
		String materialString = getYamlConfiguration().getString(path + ".material", fallback.name());
		int data = getYamlConfiguration().getInt(path + ".data", 0);
		return XMaterial.matchXMaterial(materialString)
				.map(material -> Pair.of(material, data))
				.orElseGet(() -> Pair.of(fallback, 0));
	}


	// -------------------------------------------------- //


	public boolean areSoundsEnabled() {
		return getYamlConfiguration().getBoolean("sounds.enabled", true);
	}

	public String getSound(String path) {
		return getYamlConfiguration().getString("sounds." + path, null);
	}


	// -------------------------------------------------- //


	public String getStorageType() {
		return getYamlConfiguration().getString("storage.type", "SQLite");
	}

	public String getStorageHost() {
		return getYamlConfiguration().getString("storage.mysql.host", "Host not found");
	}
	public String getStoragePort() {
		return getYamlConfiguration().getString("storage.mysql.port", "Port not found");
	}
	public String getStorageDatabase() {
		return getYamlConfiguration().getString("storage.mysql.database", "Database not found");
	}
	public String getStorageUsername() {
		return getYamlConfiguration().getString("storage.mysql.username", "Username not found");
	}
	public String getStoragePassword() {
		return getYamlConfiguration().getString("storage.mysql.password", "Password not found");
	}
	public String getStorageSettings() {
		return getYamlConfiguration().getString("storage.mysql.settings", "Settings not found");
	}
	public long getStorageMaximumPoolSize() {
		return getYamlConfiguration().getLong("storage.mysql.connectionPool.maximumPoolSize", 10L);
	}
	public long getStorageMinimumIdle() {
		return getYamlConfiguration().getLong("storage.mysql.connectionPool.minimumIdle", 10L);
	}
	public long getStorageMaxLifetime() {
		return getYamlConfiguration().getLong("storage.mysql.connectionPool.maxLifetime", 1800000L);
	}
	public long getStorageKeepaliveTime() {
		return getYamlConfiguration().getLong("storage.mysql.connectionPool.keepaliveTime", 0L);
	}
	public long getStorageConnectionTimeout() {
		return getYamlConfiguration().getLong("storage.mysql.connectionPool.connectionTimeout", 5000L);
	}
	public String getStorageTablePrefix() {
		return getYamlConfiguration().getString("storage.mysql.tablePrefix", "exg_");
	}


	// -------------------------------------------------- //
}
