package fr.snipertvmc.essentialsxgui.managers;

import com.cryptomorin.xseries.XMaterial;
import com.earth2me.essentials.User;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGHome;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGPlayer;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

import java.util.*;

public class PlayerDataManager {


	// -------------------------------------------------- //


	public Map<String, Object> generateDefaultHomesData(EXGPlayer player) {

		Map<String, Object> playerHomes = new HashMap<>();

		if (Main.getInstance().getEssentials().getUser(player.getName()) == null) {
			return playerHomes;
		}

		List<String> essentialsHomes = Main.getInstance().getEssentials().getUser(player.getName()).getHomes();
		Pair<XMaterial, Integer> defaultHomeIcon = Main.getInstance().getConfiguration().getDefaultHomeIcon();

		for (String homeName : essentialsHomes) {

			playerHomes.put(homeName, new HashMap<>() {{
				put("displayName", homeName);
				put("material", defaultHomeIcon.getLeft().name());
				put("data", defaultHomeIcon.getRight());
				put("customItemStack", null);
			}});
		}

		return playerHomes;
	}


	public void updatePlayerHomes(EXGPlayer exgPlayer) {

		User user = Main.getInstance().getEssentials().getUser(exgPlayer.getName());

		List<String> essentialsHomes = user.getHomes();
		Set<EXGHome> playerHomes = exgPlayer.getHomes();

		Set<EXGHome> updatedHomes = new HashSet<>();

		for (EXGHome home : playerHomes) {
			if (essentialsHomes.contains(home.getName())) {
				updatedHomes.add(home);
			}
		}

		for (String homeName : essentialsHomes) {
			if (playerHomes.stream().noneMatch(home -> home.getName().equals(homeName))) {
				updatedHomes.add(new EXGHome(homeName));
			}
		}

		exgPlayer.setHomes(updatedHomes);
	}


	// -------------------------------------------------- //
}
