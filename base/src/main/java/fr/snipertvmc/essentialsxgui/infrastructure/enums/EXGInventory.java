package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum EXGInventory {


	// -------------------------------------------------- //


	BALANCE_TOP("economy/balanceTop.yml", "2.0"),
	ECO_ACTION("economy/ecoAction.yml", "2.0"),
	ECO_AMOUNT("economy/ecoAmount.yml", "2.0"),
	ECO_PLAYERS("economy/ecoPlayers.yml", "2.0"),
	SELL("economy/sell.yml", "2.0"),
	WORTH("economy/worth.yml", "2.0"),
	WORTH_ALL("economy/worthAll.yml", "2.0"),
	WORTH_INVENTORY("economy/worthInventory.yml", "2.0"),

	HOME_EDITING("homes/homeEditing.yml", "2.0"),
	HOMES("homes/homes.yml", "2.0"),

	KIT_EDITING("kits/kitEditing.yml", "2.0"),
	KIT_EDITOR("kits/kitEditor.yml", "2.0"),
	KIT_PLAYER_GIVE("kits/kitPlayerGive.yml", "2.0"),
	KIT_PREVIEW("kits/kitPreview.yml", "2.0"),
	KITS_ADMIN_VIEW("kits/kitsAdminView.yml", "2.0"),
	KITS_PLAYER_VIEW("kits/kitsPlayerView.yml", "2.0"),

	DATA_ENTRY_GUI("others/dataEntryGUI.yml", "2.0"),

	WARP_EDITING("warps/warpEditing.yml", "2.0"),
	WARP_PLAYER_TELEPORT("warps/warpPlayerTeleport.yml", "2.0"),
	WARPS_ADMIN_VIEW("warps/warpsAdminView.yml", "2.0"),
	WARPS_PLAYER_VIEW("warps/warpsPlayerView.yml", "2.0"),

	WHOIS_PLAYERS("whois/whoisPlayers.yml", "2.0"),
	WHOIS_VIEW("whois/whoisView.yml", "2.0");


	// -------------------------------------------------- //


	private final String filePath;
	private final String fileName;

	private final String fileVersion;


	// -------------------------------------------------- //



	EXGInventory(String filePath, String fileVersion) {
		this.filePath = "inventories/" + filePath;
		this.fileName = filePath.split("/")[filePath.split("/").length - 1].replace(".yml", "");;

		this.fileVersion = fileVersion;
	}


	// -------------------------------------------------- //


	public String getFilePath() {
		return filePath;
	}
	public String getFileName() {
		return fileName;
	}

	public String getFileVersion() {
		return fileVersion;
	}


	public static Set<String> getAllPaths() {
		return Arrays.stream(EXGInventory.values())
				.map(EXGInventory::getFilePath)
				.collect(Collectors.toUnmodifiableSet());
	}


	// -------------------------------------------------- //


	public static EXGInventory getByPath(String path) {
		return Arrays.stream(EXGInventory.values())
				.filter(inventory -> inventory.getFilePath().equalsIgnoreCase(path))
				.findFirst()
				.orElse(null);
	}


	public static EXGInventory getByName(String name) {
		return Arrays.stream(EXGInventory.values())
				.filter(inventory -> inventory.getFileName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}


	// -------------------------------------------------- //
}
