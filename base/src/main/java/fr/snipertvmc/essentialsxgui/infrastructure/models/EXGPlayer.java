package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.serializers.ItemStackSerializer;
import fr.snipertvmc.essentialsxgui.utilities.type.TypeUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class EXGPlayer {


	// -------------------------------------------------- //


	private final UUID uuid;
	private final Player player;

	private Set<EXGHome> homes = new HashSet<>();


	// -------------------------------------------------- //


	public EXGPlayer(Player player) {
		this.uuid = player.getUniqueId();
		this.player = player;
	}


	// -------------------------------------------------- //


	public UUID getUuid() {
		return uuid;
	}
	public String getName() {
		return player.getName();
	}

	public Player getPlayer() {
		return player;
	}


	// -------------------------------------------------- //


	public Set<EXGHome> getHomes() {
		return homes;
	}


	public EXGHome getHome(String homeName) {
		return homes.stream()
				.filter(home -> home.getName().equals(homeName))
				.findFirst()
				.orElse(null);
	}


	public void setHomes(Set<EXGHome> homes) {
		this.homes = homes;
	}


	// -------------------------------------------------- //


	public Map<String, Object> getHomesRaw() {

		Map<String, Object> homes = new HashMap<>();

		this.homes.forEach(home -> {
			homes.put(home.getName(), new HashMap<>() {{
				put("displayName", home.getDisplayName());
				put("material", home.getMaterial().toString());
				put("data", home.getData());
				put("customItemStack", ItemStackSerializer.serialize(home.getCustomItemStack()));
			}});
		});

		return homes;
	}


	public void setHomesRaw(Map<String, Object> homes) {
		homes.forEach((homeName, homeData) -> {
			EXGHome home = new EXGHome(homeName);

			Object displayNameObject = ((Map<String, Object>) homeData).get("displayName");
			Object materialObject = ((Map<String, Object>) homeData).get("material");

			Object dataObject = ((Map<String, Object>) homeData).get("data");
			String dataString = dataObject != null ? dataObject.toString() : "0";

			home.setDisplayName((String) displayNameObject);
			home.setMaterial(XMaterial.matchXMaterial((String) materialObject).orElse(XMaterial.GRASS_BLOCK));
			home.setData(dataObject != null && TypeUtils.isByte(dataString) ? Byte.parseByte(dataString) : 0);

			String serializedItemStack = (String) ((Map<String, Object>) homeData).get("customItemStack");
			if (serializedItemStack != null) {
				home.setCustomItemStack(ItemStackSerializer.deserialize(serializedItemStack)[0]);
			} else {
				home.setCustomItemStack(null);
			}

			this.homes.add(home);
		});
	}


	// -------------------------------------------------- //


	public boolean canDo(String command, String permission) {
		if (player.hasPermission("*") || player.isOp()) return true;
		if (permission != null && !player.hasPermission(permission)) return false;
		if (!Main.getInstance().getHookManager().getWorldGuardHook().canExecuteCommand(player, command)
			&& !player.hasPermission("worldguard.region.bypass." + player.getWorld().getName())) return false;
		return true;
	}


	// -------------------------------------------------- //
}
