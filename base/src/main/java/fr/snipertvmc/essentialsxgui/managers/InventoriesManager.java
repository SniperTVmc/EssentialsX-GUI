package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.files.InventoryFile;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.ConfigurableHomeEditingInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.homes.ConfigurableHomesInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.kits.*;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others.ConfigurableDataEntryGUI;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpEditingInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpPlayerTeleportInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpsAdminViewInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.warps.ConfigurableWarpsPlayerViewInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.ConfigurableWhoisPlayersInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.ConfigurableWhoisViewInventory;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InventoriesManager {


	// -------------------------------------------------- //


	private Map<EXGInventory, ConfigurableInventory> inventories;


	// -------------------------------------------------- //


	public void loadInventories() {

		inventories = new HashMap<>();

		inventories.put(EXGInventory.BALANCE_TOP, new ConfigurableBalanceTopInventory(getInventoryFile(EXGInventory.BALANCE_TOP)));
		inventories.put(EXGInventory.ECO_ACTION, new ConfigurableEcoActionInventory(getInventoryFile(EXGInventory.ECO_ACTION)));
		inventories.put(EXGInventory.ECO_AMOUNT, new ConfigurableEcoAmountInventory(getInventoryFile(EXGInventory.ECO_AMOUNT)));
		inventories.put(EXGInventory.ECO_PLAYERS, new ConfigurableEcoPlayersInventory(getInventoryFile(EXGInventory.ECO_PLAYERS)));
		inventories.put(EXGInventory.SELL, new ConfigurableSellInventory(getInventoryFile(EXGInventory.SELL)));
		inventories.put(EXGInventory.WORTH, new ConfigurableWorthInventory(getInventoryFile(EXGInventory.WORTH)));
		inventories.put(EXGInventory.WORTH_ALL, new ConfigurableWorthAllInventory(getInventoryFile(EXGInventory.WORTH_ALL)));
		inventories.put(EXGInventory.WORTH_INVENTORY, new ConfigurableWorthInventoryInventory(getInventoryFile(EXGInventory.WORTH_INVENTORY)));

		inventories.put(EXGInventory.HOME_EDITING, new ConfigurableHomeEditingInventory(getInventoryFile(EXGInventory.HOME_EDITING)));
		inventories.put(EXGInventory.HOMES, new ConfigurableHomesInventory(getInventoryFile(EXGInventory.HOMES)));

		inventories.put(EXGInventory.KIT_EDITING, new ConfigurableKitEditingInventory(getInventoryFile(EXGInventory.KIT_EDITING)));
		inventories.put(EXGInventory.KIT_EDITOR, new ConfigurableKitEditorInventory(getInventoryFile(EXGInventory.KIT_EDITOR)));
		inventories.put(EXGInventory.KIT_PLAYER_GIVE, new ConfigurableKitPlayerGiveInventory(getInventoryFile(EXGInventory.KIT_PLAYER_GIVE)));
		inventories.put(EXGInventory.KIT_PREVIEW, new ConfigurableKitPreviewInventory(getInventoryFile(EXGInventory.KIT_PREVIEW)));
		inventories.put(EXGInventory.KITS_ADMIN_VIEW, new ConfigurableKitsAdminViewInventory(getInventoryFile(EXGInventory.KITS_ADMIN_VIEW)));
		inventories.put(EXGInventory.KITS_PLAYER_VIEW, new ConfigurableKitsPlayerViewInventory(getInventoryFile(EXGInventory.KITS_PLAYER_VIEW)));

		inventories.put(EXGInventory.DATA_ENTRY_GUI, new ConfigurableDataEntryGUI(getInventoryFile(EXGInventory.DATA_ENTRY_GUI)));

		inventories.put(EXGInventory.WARP_EDITING, new ConfigurableWarpEditingInventory(getInventoryFile(EXGInventory.WARP_EDITING)));
		inventories.put(EXGInventory.WARP_PLAYER_TELEPORT, new ConfigurableWarpPlayerTeleportInventory(getInventoryFile(EXGInventory.WARP_PLAYER_TELEPORT)));
		inventories.put(EXGInventory.WARPS_ADMIN_VIEW, new ConfigurableWarpsAdminViewInventory(getInventoryFile(EXGInventory.WARPS_ADMIN_VIEW)));
		inventories.put(EXGInventory.WARPS_PLAYER_VIEW, new ConfigurableWarpsPlayerViewInventory(getInventoryFile(EXGInventory.WARPS_PLAYER_VIEW)));

		inventories.put(EXGInventory.WHOIS_PLAYERS, new ConfigurableWhoisPlayersInventory(getInventoryFile(EXGInventory.WHOIS_PLAYERS)));
		inventories.put(EXGInventory.WHOIS_VIEW, new ConfigurableWhoisViewInventory(getInventoryFile(EXGInventory.WHOIS_VIEW)));
	}


	private InventoryFile getInventoryFile(EXGInventory inventory) {
		return Main.getInstance().getFilesManager().getInventoryFile(inventory);
	}


	public InventoryFile getInventoryFileByItemPath(String itemPath) {
		String inventoryNameFromItemPath = itemPath.split("\\.")[0];
		Optional<EXGInventory> result = Arrays.stream(EXGInventory.values())
				.filter(inventory -> inventory.getFileName().equalsIgnoreCase(inventoryNameFromItemPath))
				.findFirst();
		if (result.isPresent()) return getInventoryFile(result.get());
		if (itemPath.contains(".items.")) ConsoleLogger.warn("No inventory file was found for item path '" + itemPath + "'.");
		return null;
	}


	public ConfigurableInventory getInventory(EXGInventory inventory) {
		return inventories.get(inventory);
	}


	// -------------------------------------------------- //
}
