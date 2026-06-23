package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.earth2me.essentials.utils.FormatUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EXGBalanceTop {


	// -------------------------------------------------- //

	private final List<Pair<String, Double>> balanceTopEntries = new ArrayList<>();


	private long lastUpdate = System.currentTimeMillis();
	private BukkitTask updateTask;


	// -------------------------------------------------- //


	public CompletableFuture<?> forceUpdate() {
		return Main.getInstance().getEssentials().getBalanceTop().calculateBalanceTopMapAsync().thenRun(() -> {

			balanceTopEntries.clear();
			Main.getInstance().getEssentials().getBalanceTop().getBalanceTopCache().values().forEach(entry -> {
				balanceTopEntries.add(new Pair<>(FormatUtil.stripFormat(entry.getDisplayName()), entry.getBalance().doubleValue()));
			});

			lastUpdate = System.currentTimeMillis();
		});
	}


	public Pair<Integer, BigDecimal> getPlayerRanking(String playerName) {
		for (int i = 0; i < balanceTopEntries.size(); i++) {
			if (balanceTopEntries.get(i).getLeft().equals(playerName)) {
				return Pair.of(i + 1, BigDecimal.valueOf(balanceTopEntries.get(i).getRight()));
			}
		}
		return Pair.of(-1, BigDecimal.ZERO);
	}


	// -------------------------------------------------- //


	public void startUpdateTask() {
		if (!Main.getInstance().getConfiguration().isEconomyBalanceTopModuleEnabled()) {
			return;
		}

		if (isUpdateTaskRunning()) {
			return;
		}

		long updateInterval = Main.getInstance().getConfiguration().getBalanceTopUpdateInterval() * 20L;
		updateTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::forceUpdate, 0, updateInterval);
	}


	public void stopUpdateTask() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}


	public boolean isUpdateTaskRunning() {
		try {
			return updateTask != null && !updateTask.isCancelled();

		} catch (NoSuchMethodError e) {
			return updateTask != null;
		}
	}


	// -------------------------------------------------- //


	public List<Pair<String, Double>> getBalanceTopEntries() {
		return balanceTopEntries;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}


	// -------------------------------------------------- //
}
