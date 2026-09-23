package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGMessage {


	// -------------------------------------------------- //


	// ADMIN
	ALERT_UPDATE_AVAILABLE("admin.alertUpdateAvailable"),

	PLUGIN_RELOADING("admin.pluginReloading"),
	PLUGIN_RELOADED("admin.pluginReloaded"),
	PLUGIN_RELOADED_WITH_ERRORS("admin.pluginReloadedWithErrors"),

	FILES_RELOADING("admin.filesReloading"),
	FILES_RELOADED("admin.filesReloaded"),

	DATABASE_RELOADING("admin.databaseReloading"),
	DATABASE_RELOADED("admin.databaseReloaded"),

	TASKS_RELOADING("admin.tasksReloading"),
	TASKS_RELOADED("admin.tasksReloaded"),

	DATA_RELOADING("admin.dataReloading"),
	DATA_RELOADED("admin.dataReloaded"),


	// ERRORS
	INVALID_MATERIAL("errors.invalidMaterial"),
	LENGTH_LIMIT("errors.lengthLimit"),
	INVALID_MINIMESSAGE_FORMAT("errors.invalidMiniMessageFormat"),
	INVALID_LEGACY_FORMAT("errors.invalidLegacyFormat"),
	INVALID_MIXED_FORMAT("errors.invalidMixedFormat"),
	INVALID_NUMBER("errors.invalidNumber"),
	INVALID_CHARACTER("errors.invalidCharacter"),
	PLAYER_NOT_FOUND("errors.playerNotFound"),

	ARGUMENT_NOT_FOUND("errors.argumentNotFound"),
	NO_PERMISSION("errors.noPermission"),
	ONLY_FOR_PLAYERS("errors.onlyForPlayers"),
	INVALID_NAME("errors.invalidName"),
	WAIT_BEFORE_NEXT_ACTION("errors.waitBeforeNextAction"),

	HOME_DELETE_ERROR("errors.homeDeleteError"),
	HOME_NAME_ALREADY_EXISTS("errors.homeNameAlreadyExists"),
	HOME_LIMIT_REACHED("errors.homeLimitReached"),
	NO_HOME_FOUND("errors.noHomeFound"),
	ITEM_CANT_BE_AIR("errors.itemCantBeAir"),

	KIT_NAME_ALREADY_EXISTS("errors.kitNameAlreadyExists"),
	NO_KIT_FOUND("errors.noKitFound"),

	WARP_CREATION_ERROR("errors.warpCreationError"),
	WARP_DELETE_ERROR("errors.warpDeleteError"),
	WARP_NAME_ALREADY_EXISTS("errors.warpNameAlreadyExists"),
	NO_WARP_FOUND("errors.noWarpFound"),

	NO_WORTH_FOUND("errors.noWorthFound"),
	AMOUNT_MUST_BE_POSITIVE("errors.amountMustBePositive"),
	MAX_AMOUNT_LIMIT("errors.maxAmountLimit"),
	MIN_AMOUNT_LIMIT("errors.minAmountLimit"),
	CANT_HAVE_NEGATIVE_BALANCE("errors.cantHaveNegativeBalance"),


	// GENERAL
	ENTER_NEW_DISPLAY_NAME_CHAT("general.enterNewDisplayNameInChat"),
	ENTER_NEW_ICON_NAME_CHAT("general.enterNewIconNameInChat"),
	ENTER_NEW_DISPLAY_NAME("general.enterNewDisplayName"),
	ENTER_NEW_ICON_NAME("general.enterNewIconName"),

	ENTER_NEW_HOME_NAME_CHAT("general.enterNewHomeNameInChat"),
	ENTER_NEW_HOME_NAME("general.enterNewHomeName"),
	SEARCH_HOME_CHAT("general.searchHomeInChat"),
	SEARCH_HOME("general.searchHome"),
	CONFIRM_DELETE_HOME_CHAT("general.confirmDeleteHomeInChat"),
	CONFIRM_DELETE_HOME("general.confirmDeleteHome"),

	ENTER_NEW_KIT_NAME_CHAT("general.enterNewKitNameInChat"),
	ENTER_NEW_KIT_DELAY_CHAT("general.enterNewKitDelayInChat"),
	ENTER_NEW_KIT_NAME("general.enterNewKitName"),
	ENTER_NEW_KIT_DELAY("general.enterNewKitDelay"),
	SEARCH_KIT_CHAT("general.searchKitInChat"),
	SEARCH_KIT("general.searchKit"),
	CONFIRM_DELETE_KIT_CHAT("general.confirmDeleteKitInChat"),
	CONFIRM_DELETE_KIT("general.confirmDeleteKit"),

	ENTER_NEW_WARP_NAME_CHAT("general.enterNewWarpNameInChat"),
	ENTER_NEW_WARP_NAME("general.enterNewWarpName"),
	SEARCH_WARP_CHAT("general.searchWarpInChat"),
	SEARCH_WARP("general.searchWarp"),
	CONFIRM_DELETE_WARP_CHAT("general.confirmDeleteWarpInChat"),
	CONFIRM_DELETE_WARP("general.confirmDeleteWarp"),

	BALANCE_TOP_DATA_UPDATED("general.balanceTopDataUpdated"),
	SEARCH_WORTH_CHAT("general.searchWorthInChat"),
	SEARCH_WORTH("general.searchWorth"),

	ACTION_CANCELED("general.actionCanceled"),
	ACTION_EXPIRED("general.actionExpired"),
	ONGOING_ACTION("general.ongoingAction"),


	DISPLAY_NAME_CHANGED("general.displayNameChanged"),
	ICON_CHANGED("general.iconChanged"),

	HOME_DELETED("general.homeDeleted"),
	HOME_CREATED("general.homeCreated"),

	KIT_CREATED("general.kitCreated"),
	KIT_DELETED("general.kitDeleted"),

	WARP_CREATED("general.warpCreated"),
	WARP_DELETED("general.warpDeleted"),


	OPENING_HOMES_INVENTORY("general.openingHomesInventory"),

	OPENING_ADMIN_KITS_INVENTORY("general.openingAdminKitsInventory"),
	OPENING_PLAYER_KITS_INVENTORY("general.openingPlayerKitsInventory"),

	OPENING_ADMIN_WARPS_INVENTORY("general.openingAdminWarpsInventory"),
	OPENING_PLAYER_WARPS_INVENTORY("general.openingPlayerWarpsInventory"),

	OPENING_WHOIS_INVENTORY("general.openingWhoisInventory"),

	OPENING_BALANCE_TOP_INVENTORY("general.openingBalanceTopInventory"),
	OPENING_WORTH_INVENTORY("general.openingWorthInventory"),
	OPENING_ECO_INVENTORY("general.openingEcoInventory"),
	OPENING_SELL_INVENTORY("general.openingSellInventory"),


	YES("general.yes_value"),
	NO("general.no_value"),
	FLYING("general.flying"),
	NOT_FLYING("general.notFlying"),
	GAMEMODE_SURVIVAL("general.gamemodeSurvival"),
	GAMEMODE_CREATIVE("general.gamemodeCreative"),
	GAMEMODE_ADVENTURE("general.gamemodeAdventure"),
	GAMEMODE_SPECTATOR("general.gamemodeSpectator"),
	HIDDEN("general.hidden"),
	DISABLED("general.disabled"),
	NO_NICKNAME("general.noNickname"),
	NOT_AFK("general.notAfk"),
	NOT_JAILED("general.notJailed"),
	NOT_MUTED("general.notMuted"),
	NO_MUTE_REASON("general.noMuteReason"),
	NOT_BANNED("general.notBanned"),
	NO_BAN_REASON("general.noBanReason"),
	PERMANENT("general.permanent"),
	LOCATION_FORMAT("general.locationFormat"),
	UNKNOWN("general.unknown"),
	TYPE_HERE("general.typeHere"),
	NOBODY("general.nobody"),
	NOT_RANKED("general.notRanked"),
	RANK_FORMAT("general.rankFormat"),
	NO_WORTH_AVAILABLE("general.noWorthAvailable"),
	NO_ITEM_IN_HAND("general.noItemInHand"),
	GIVE("general.give"),
	TAKE("general.take"),
	SET("general.set"),
	RESET("general.reset"),
	AVAILABLE("general.available"),
	WORTH_FORMAT("general.worthFormat"),
	MULTIPLIER_FORMAT("general.multiplierFormat"),


	DAYS("general.days"),
	HOURS("general.hours"),
	MINUTES("general.minutes"),
	SECONDS("general.seconds"),
	AGO_TIME_FORMAT("general.agoTimeFormat"),
	IN_TIME_FORMAT("general.inTimeFormat"),
	SINCE_TIME_FORMAT("general.sinceTimeFormat");


	// -------------------------------------------------- //


	private final String path;


	// -------------------------------------------------- //


	EXGMessage(String path) {
		this.path = path;
	}


	// -------------------------------------------------- //


	public String getPath() {
		return path;
	}


	// -------------------------------------------------- //
}
