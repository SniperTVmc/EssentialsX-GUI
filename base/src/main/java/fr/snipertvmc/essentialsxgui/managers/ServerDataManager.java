package fr.snipertvmc.essentialsxgui.managers;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGKit;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGWarp;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerDataManager {


	// -------------------------------------------------- //


	public Map<String, Object> generateDefaultKitsData() {

		Map<String, Object> serverKits = new HashMap<>();

		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();
		Pair<XMaterial, Integer> defaultKitIcon = Main.getInstance().getConfiguration().getDefaultKitIcon();

		for (String kitName : essentialsKits) {
			serverKits.put(kitName, new HashMap<>() {{
				put("displayName", kitName);
				put("material", defaultKitIcon.getLeft().name());
				put("data", defaultKitIcon.getRight());
				put("customItemStack", null);
			}});

			if (!Main.getInstance().getDatabaseManager().getKitsTableManager().isKitExists(kitName)) {
				Main.getInstance().getDatabaseManager().getKitsTableManager().insertKit(kitName, (Map<String, Object>) serverKits.get(kitName));
			}
		}

		return serverKits;
	}


	public void updateServerKits() {

		EXGServer exgServer = Main.getInstance().getEXGServer();

		Set<String> essentialsKits = Main.getInstance().getEssentials().getKits().getKitKeys();
		Set<EXGKit> serverKits = exgServer.getKits();

		Set<EXGKit> updatedKits = new HashSet<>();

		for (EXGKit kit : serverKits) {
			if (essentialsKits.contains(kit.getName())) {
				updatedKits.add(kit);
			}
		}

		for (String kitName : essentialsKits) {
			if (serverKits.stream().noneMatch(kit -> kit.getName().equals(kitName))) {
				updatedKits.add(new EXGKit(kitName));
			}
		}

		exgServer.setKits(updatedKits);
	}


	// -------------------------------------------------- //


	public Map<String, Object> generateDefaultWarpsData() {

		Map<String, Object> serverWarps = new HashMap<>();

		Set<String> essentialsWarps = new HashSet<>(Main.getInstance().getEssentials().getWarps().getList());
		Pair<XMaterial, Integer> defaultWarpIcon = Main.getInstance().getConfiguration().getDefaultWarpIcon();

		for (String warpName : essentialsWarps) {
			serverWarps.put(warpName, new HashMap<>() {{
				put("displayName", warpName);
				put("material", defaultWarpIcon.getLeft().name());
				put("data", defaultWarpIcon.getRight());
				put("customItemStack", null);
			}});

			if (!Main.getInstance().getDatabaseManager().getWarpsTableManager().isWarpExists(warpName)) {
				Main.getInstance().getDatabaseManager().getWarpsTableManager().insertWarp(warpName, (Map<String, Object>) serverWarps.get(warpName));
			}
		}

		return serverWarps;
	}


	public void updateServerWarps() {

		EXGServer exgServer = Main.getInstance().getEXGServer();

		Set<String> essentialsWarps = new HashSet<>(Main.getInstance().getEssentials().getWarps().getList());
		Set<EXGWarp> serverWarps = exgServer.getWarps();

		Set<EXGWarp> updatedWarps = new HashSet<>();

		for (EXGWarp warp : serverWarps) {
			if (essentialsWarps.contains(warp.getName())) {
				updatedWarps.add(warp);
			}
		}

		for (String warpName : essentialsWarps) {
			if (serverWarps.stream().noneMatch(warp -> warp.getName().equals(warpName))) {
				updatedWarps.add(new EXGWarp(warpName));
			}
		}

		exgServer.setWarps(updatedWarps);
	}


	// -------------------------------------------------- //
}
