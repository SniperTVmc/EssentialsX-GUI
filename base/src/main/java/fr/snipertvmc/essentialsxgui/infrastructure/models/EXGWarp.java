package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

public class EXGWarp extends EXGIcon {


	// -------------------------------------------------- //


	public EXGWarp(String name) {
		super(name);

		Pair<XMaterial, Integer> defaultWarpIcon = Main.getInstance().getConfiguration().getDefaultWarpIcon();
		this.setMaterial(defaultWarpIcon.getLeft());
		this.setData(defaultWarpIcon.getRight().byteValue());
	}


	// -------------------------------------------------- //
}
