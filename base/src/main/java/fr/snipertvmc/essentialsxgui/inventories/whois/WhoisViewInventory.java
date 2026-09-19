package fr.snipertvmc.essentialsxgui.inventories.whois;

import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.EnumUtil;
import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.ConfigurableWhoisViewInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WhoisViewInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableWhoisViewInventory config = (ConfigurableWhoisViewInventory) Main.getInstance().getInventory(EXGInventory.WHOIS_VIEW);


	// -------------------------------------------------- //


	public WhoisViewInventory(Player player, Player target) {
		super(
				Main.getInstance().getInventory(EXGInventory.WHOIS_VIEW).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.WHOIS_VIEW).getTitle()
						.build(player, Map.of(
								"targetName", target.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		// Fetching user data
		boolean useOnlyPlaceholderAPI = config.isOnlyUsePlaceholderAPI();

		Map<String, String> playerIdentificationPlaceholders = new HashMap<>();
		Map<String, String> playerStatisticsPlaceholders = new HashMap<>();
		Map<String, String> playerWorldPlaceholders = new HashMap<>();
		Map<String, String> playerServerDataPlaceholders = new HashMap<>();
		Map<String, String> playerPunishmentsPlaceholders = new HashMap<>();

		if (!useOnlyPlaceholderAPI) {
			Map<String, String> playerData = getPlayerData(player);
			Location worldLocation = target.getLocation();

			playerIdentificationPlaceholders = new HashMap<>() {{
				put("targetName", target.getName());
				put("targetUUID", target.getUniqueId().toString());
				put("targetIP", playerData.get("ipAddress"));
				put("targetPlaytime", playerData.get("playtime"));
			}};

			playerStatisticsPlaceholders = new HashMap<>() {{
				put("targetName", target.getName());
				put("targetHealth", playerData.get("health"));
				put("targetMaxHealth", playerData.get("maxHealth"));
				put("targetFoodLevel", playerData.get("foodLevel"));
				put("targetSaturation", playerData.get("saturation"));
				put("targetExperience", playerData.get("experience"));
				put("targetLevel", playerData.get("level"));
			}};

			playerWorldPlaceholders = new HashMap<>() {{
				put("targetName", target.getName());
				put("targetWorld", worldLocation.getWorld().getName());
				put("targetX", String.valueOf((int) worldLocation.getX()));
				put("targetY", String.valueOf((int) worldLocation.getY()));
				put("targetZ", String.valueOf((int) worldLocation.getZ()));
				put("targetYaw", String.valueOf((int) worldLocation.getYaw()));
				put("targetPitch", String.valueOf((int) worldLocation.getPitch()));
				put("targetLocation", playerData.get("location"));
			}};

			playerServerDataPlaceholders = new HashMap<>() {{
				put("targetName", target.getName());
				put("targetGamemode", playerData.get("gamemodeName"));
				put("targetMoney", playerData.get("money"));
				put("targetIsInGodMode", playerData.get("isGodMode"));
				put("targetCanFly", playerData.get("canFly"));
				put("targetIsFlying", playerData.get("isFlying"));
				put("targetWalkSpeed", playerData.get("walkSpeed"));
				put("targetFlySpeed", playerData.get("flySpeed"));
				put("targetIsOperator", playerData.get("isOperator"));
				put("targetIsWhitelisted", playerData.get("isWhitelisted"));
				put("targetIsVanished", playerData.get("isVanished"));
				put("targetIsNicked", playerData.get("isNicked"));
				put("targetNickname", playerData.get("nickname"));
				put("targetIsAfk", playerData.get("isAfk"));
				put("targetAfkSince", playerData.get("afkSince"));
			}};

			playerPunishmentsPlaceholders = new HashMap<>() {{
				put("targetName", target.getName());
				put("targetIsJailed", playerData.get("isJailed"));
				put("targetJailName", playerData.get("jailName"));
				put("targetJailExpiry", playerData.get("jailExpiry"));
				put("targetIsMuted", playerData.get("isMuted"));
				put("targetMuteReason", playerData.get("muteReason"));
				put("targetMuteExpiry", playerData.get("muteExpiry"));
				put("targetIsBanned", playerData.get("isBanned"));
				put("targetBanReason", playerData.get("banReason"));
				put("targetBanExpiry", playerData.get("banExpiry"));
			}};
		}


		// Setting items
		if (config.getPlayerIdentificationItem().isEnabled()) {
			setItem(config.getPlayerIdentificationItem().getSlot(), config.getPlayerIdentificationItem()
					.build(player, playerIdentificationPlaceholders)
			);
		}

		if (config.getPlayerStatisticsItem().isEnabled()) {
			setItem(config.getPlayerStatisticsItem().getSlot(), config.getPlayerStatisticsItem()
					.build(player, playerStatisticsPlaceholders)
			);
		}

		if (config.getPlayerWorldItem().isEnabled()) {
			setItem(config.getPlayerWorldItem().getSlot(), config.getPlayerWorldItem()
					.build(player, playerWorldPlaceholders)
			);
		}

		if (config.getPlayerServerDataItem().isEnabled()) {
			setItem(config.getPlayerServerDataItem().getSlot(), config.getPlayerServerDataItem()
					.build(player, playerServerDataPlaceholders)
			);
		}

		if (config.getPlayerPunishmentsItem().isEnabled()) {
			setItem(config.getPlayerPunishmentsItem().getSlot(), config.getPlayerPunishmentsItem()
					.build(player, playerPunishmentsPlaceholders)
			);
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new WhoisPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public Map<String, String> getPlayerData(Player player) {
		User user = Main.getInstance().getEssentials().getUser(player.getUniqueId());

		Map<String, String> data = new HashMap<>();

		data.putAll(getPlayerIdentification(user));
		data.putAll(getPlayerStatistics(user));
		data.putAll(getPlayerWorld(user));
		data.putAll(getPlayerServerData(user, player));
		data.putAll(getPlayerPunishments(user, player));

		return data;
	}


	private Map<String, String> getPlayerIdentification(User user) {
		Map<String, String> map = new HashMap<>();

		boolean canSeeIPAddress = user.isAuthorized("essentials.whois.ip");
		String ipAddress = canSeeIPAddress
				? user.getBase().getAddress().getAddress().toString()
				: "§c" + MessagesUtils.getString(EXGMessage.HIDDEN);

		Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");
		long playtimeSeconds = user.getBase().getStatistic(PLAY_ONE_TICK) / 20L;
		String playtime = TimeUtils.formatDuration((int) (playtimeSeconds));

		map.put("ipAddress", ipAddress);
		map.put("playtime", playtime);

		return map;
	}


	private Map<String, String> getPlayerStatistics(User user) {
		Map<String, String> map = new HashMap<>();

		map.put("health", String.valueOf(user.getBase().getHealth()));
		map.put("maxHealth", String.valueOf(user.getBase().getMaxHealth()));
		map.put("foodLevel", String.valueOf(user.getBase().getFoodLevel()));
		map.put("saturation", String.valueOf(user.getBase().getSaturation()));
		map.put("experience", String.valueOf(user.getBase().getTotalExperience()));
		map.put("level", String.valueOf(user.getBase().getLevel()));

		return map;
	}


	private Map<String, String> getPlayerWorld(User user) {
		Map<String, String> map = new HashMap<>();

		Location loc = user.getBase().getLocation();
		String location = MessagesUtils.getString(EXGMessage.LOCATION_FORMAT, Map.of(
				"world", loc.getWorld().getName(),
				"x", String.valueOf((int) loc.getX()),
				"y", String.valueOf((int) loc.getY()),
				"z", String.valueOf((int) loc.getZ()),
				"yaw", String.valueOf((int) loc.getYaw()),
				"pitch", String.valueOf((int) loc.getPitch())
		));

		map.put("location", location);

		return map;
	}


	private Map<String, String> getPlayerServerData(User user, Player player) {
		Map<String, String> map = new HashMap<>();

		GameMode gamemode = user.getBase().getGameMode();
		String gamemodeName = switch (gamemode) {
			case SURVIVAL -> MessagesUtils.getString(EXGMessage.GAMEMODE_SURVIVAL);
			case CREATIVE -> MessagesUtils.getString(EXGMessage.GAMEMODE_CREATIVE);
			case ADVENTURE -> MessagesUtils.getString(EXGMessage.GAMEMODE_ADVENTURE);
			case SPECTATOR -> MessagesUtils.getString(EXGMessage.GAMEMODE_SPECTATOR);
		};

		boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String money = ecoEnabled
				? NumberUtil.displayCurrency(user.getMoney(), Main.getInstance().getEssentials())
				: MessagesUtils.getString(EXGMessage.DISABLED);

		String nick = user.getNickname();
		boolean isNicked = nick != null && !nick.equals(player.getName());
		boolean isAfk = user.isAfk();

		map.put("money", money);
		map.put("gamemodeName", gamemodeName);
		map.put("isGodMode", yesNo(user.isGodModeEnabled()));
		map.put("canFly", yesNo(user.getBase().getAllowFlight()));
		map.put("isFlying", yesNo(user.getBase().isFlying()));
		map.put("walkSpeed", String.valueOf(user.getBase().getWalkSpeed()));
		map.put("flySpeed", String.valueOf(user.getBase().getFlySpeed()));
		map.put("isOperator", yesNo(user.getBase().isOp()));
		map.put("isWhitelisted", yesNo(user.getBase().isWhitelisted()));
		map.put("isVanished", yesNo(user.isVanished()));
		map.put("nickname", isNicked ? nick : MessagesUtils.getString(EXGMessage.NO_NICKNAME));
		map.put("isNicked", yesNo(isNicked));
		map.put("isAfk", yesNo(isAfk));
		map.put("afkSince", isAfk
				? TimeUtils.formatSinceTime((int) user.getAfkSince() / 1000)
				: MessagesUtils.getString(EXGMessage.NOT_AFK));

		return map;
	}


	private Map<String, String> getPlayerPunishments(User user, Player player) {
		Map<String, String> map = new HashMap<>();

		boolean isJailed = user.isJailed();
		map.put("isJailed", yesNo(isJailed));
		map.put("jailName", user.getName());
		map.put("jailExpiry", isJailed
				? TimeUtils.formatInTime((int) user.getJailTimeout() / 1000)
				: MessagesUtils.getString(EXGMessage.NOT_JAILED));

		boolean isMuted = user.isMuted();
		map.put("isMuted", yesNo(isMuted));
		map.put("muteReason", isMuted
				? (user.getMuteReason() != null
				? user.getMuteReason()
				: MessagesUtils.getString(EXGMessage.NO_MUTE_REASON))
				: MessagesUtils.getString(EXGMessage.NOT_MUTED));
		map.put("muteExpiry", isMuted
				? TimeUtils.formatInTime((int) user.getMuteTimeout() / 1000)
				: MessagesUtils.getString(EXGMessage.NOT_MUTED));

		BanEntry banEntry = Bukkit.getServer()
				.getBanList(BanList.Type.NAME)
				.getBanEntry(player.getName());

		boolean isBanned = banEntry != null;
		map.put("isBanned", yesNo(isBanned));
		map.put("banReason", isBanned
				? (banEntry.getReason() != null && !banEntry.getReason().isEmpty()
				? banEntry.getReason()
				: MessagesUtils.getString(EXGMessage.NO_BAN_REASON))
				: MessagesUtils.getString(EXGMessage.NOT_BANNED));
		map.put("banExpiry", isBanned
				? (banEntry.getExpiration() != null
				? TimeUtils.formatInTime((int) banEntry.getExpiration().getTime() / 1000)
				: MessagesUtils.getString(EXGMessage.PERMANENT))
				: MessagesUtils.getString(EXGMessage.NOT_BANNED));

		return map;
	}


	// -------------------------------------------------- //


	private String yesNo(boolean value) {
		return value
				? MessagesUtils.getString(EXGMessage.YES)
				: MessagesUtils.getString(EXGMessage.NO);
	}


	// -------------------------------------------------- //
}