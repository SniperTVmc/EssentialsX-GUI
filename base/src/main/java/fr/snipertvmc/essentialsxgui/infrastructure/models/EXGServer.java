package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.utilities.serializers.ItemStackSerializer;
import fr.snipertvmc.essentialsxgui.utilities.type.TypeUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EXGServer {


	// -------------------------------------------------- //

	private Set<EXGKit> kits = new HashSet<>();
	private Set<EXGWarp> warps = new HashSet<>();

	private final EXGBalanceTop balanceTop = new EXGBalanceTop();
	private final EXGWorth worth = new EXGWorth();


	// -------------------------------------------------- //


	public EXGServer() {
		balanceTop.startUpdateTask();
	}


	// -------------------------------------------------- //


	public Set<EXGKit> getKits() {
		return kits;
	}
	public void setKits(Set<EXGKit> kits) {
		this.kits = kits;
	}


	public EXGKit getKit(String kitName) {
		return kits.stream()
				.filter(kit -> kit.getName().equals(kitName))
				.findFirst()
				.orElse(null);
	}


	public Map<String, Object> getKitsRaw() {

		Map<String, Object> kits = new HashMap<>();

		this.kits.forEach(kit -> {
			kits.put(kit.getName(), new HashMap<>() {{
				put("displayName", kit.getDisplayName());
				put("material", kit.getMaterial().toString());
				put("data", kit.getData());
			}});

			if (kit.getCustomItemStack() != null) {
				((Map<String, Object>) kits.get(kit.getName())).put("customItemStack", ItemStackSerializer.serialize(kit.getCustomItemStack()));
			}
		});

		return kits;
	}


	public void setKitsRaw(Map<String, Object> kits) {
		kits.forEach((kitName, kitData) -> {
			EXGKit kit = new EXGKit(kitName);

			Object displayNameObject = ((Map<String, Object>) kitData).get("displayName");
			Object materialObject = ((Map<String, Object>) kitData).get("material");

			Object dataObject = ((Map<String, Object>) kitData).get("data");
			String dataString = dataObject != null ? dataObject.toString() : "0";

			kit.setDisplayName((String) displayNameObject);
			kit.setMaterial(XMaterial.matchXMaterial((String) materialObject).orElse(XMaterial.GRASS_BLOCK));
			kit.setData(dataObject != null && TypeUtils.isByte(dataString) ? Byte.parseByte(dataString) : 0);

			String serializedItemStack = (String) ((Map<String, Object>) kitData).get("customItemStack");
			if (serializedItemStack != null && TypeUtils.isBase64(serializedItemStack)) {
				kit.setCustomItemStack(ItemStackSerializer.deserialize(serializedItemStack)[0]);
			} else {
				kit.setCustomItemStack(null);
			}

			this.kits.add(kit);
		});
	}


	// -------------------------------------------------- //


	public Set<EXGWarp> getWarps() {
		return warps;
	}
	public void setWarps(Set<EXGWarp> warps) {
		this.warps = warps;
	}


	public EXGWarp getWarp(String warpName) {
		return warps.stream()
				.filter(warp -> warp.getName().equals(warpName))
				.findFirst()
				.orElse(null);
	}


	public Map<String, Object> getWarpsRaw() {

		Map<String, Object> warps = new HashMap<>();

		this.warps.forEach(warp -> {
			warps.put(warp.getName(), new HashMap<>() {{
				put("displayName", warp.getDisplayName());
				put("material", warp.getMaterial().toString());
				put("data", warp.getData());
			}});

			if (warp.getCustomItemStack() != null) {
				((Map<String, Object>) warps.get(warp.getName())).put("customItemStack", ItemStackSerializer.serialize(warp.getCustomItemStack()));
			}
		});

		return warps;
	}


	public void setWarpsRaw(Map<String, Object> warps) {
		warps.forEach((warpName, warpData) -> {
			EXGWarp warp = new EXGWarp(warpName);

			Object displayNameObject = ((Map<String, Object>) warpData).get("displayName");
			Object materialObject = ((Map<String, Object>) warpData).get("material");

			Object dataObject = ((Map<String, Object>) warpData).get("data");
			String dataString = dataObject != null ? dataObject.toString() : "0";

			warp.setDisplayName((String) displayNameObject);
			warp.setMaterial(XMaterial.matchXMaterial((String) materialObject).orElse(XMaterial.GRASS_BLOCK));
			warp.setData(dataObject != null && TypeUtils.isByte(dataString) ? Byte.parseByte(dataString) : 0);

			String serializedItemStack = (String) ((Map<String, Object>) warpData).get("customItemStack");
			if (serializedItemStack != null && TypeUtils.isBase64(serializedItemStack)) {
				warp.setCustomItemStack(ItemStackSerializer.deserialize(serializedItemStack)[0]);
			} else {
				warp.setCustomItemStack(null);
			}

			this.warps.add(warp);
		});
	}


	// -------------------------------------------------- //


	public EXGBalanceTop getBalanceTop() {
		return balanceTop;
	}
	public EXGWorth getWorth() {
		return worth;
	}


	// -------------------------------------------------- //
}
