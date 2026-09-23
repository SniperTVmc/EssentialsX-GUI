package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGServer;

import java.util.Map;

public class ServerManager {


	// -------------------------------------------------- //


	private final EXGServer exgServer;


	// -------------------------------------------------- //


	public EXGServer getEXGServer() {
		return exgServer;
	}


	// -------------------------------------------------- //


	public ServerManager() {

		EXGServer exgServer = new EXGServer();
		this.exgServer = exgServer;


		// KITS LOADING
		Map<String, Object> kitsRaw = Main.getInstance().getDatabaseManager().getKitsTableManager().fetchKits();
		if (kitsRaw.isEmpty()) {
			kitsRaw = Main.getInstance().getServerDataManager().generateDefaultKitsData();
		}

		exgServer.setKitsRaw(kitsRaw);


		// WARPS LOADING
		Map<String, Object> warpsRaw = Main.getInstance().getDatabaseManager().getWarpsTableManager().fetchWarps();
		if (warpsRaw.isEmpty()) {
			warpsRaw = Main.getInstance().getServerDataManager().generateDefaultWarpsData();
		}

		exgServer.setWarpsRaw(warpsRaw);
	}


	public void save() {

		EXGServer exgServer = Main.getInstance().getEXGServer();
		if (exgServer == null) {
			return;
		}


		// KITS SAVING
		Map<String, Object> kitsRaw = exgServer.getKitsRaw();
		Main.getInstance().getDatabaseManager().getKitsTableManager().updateKits(kitsRaw);


		// WARPS SAVING
		Map<String, Object> warpsRaw = exgServer.getWarpsRaw();
		Main.getInstance().getDatabaseManager().getWarpsTableManager().updateWarps(warpsRaw);
	}


	// -------------------------------------------------- //
}
