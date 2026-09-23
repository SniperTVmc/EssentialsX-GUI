package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

public class EXGKit extends EXGIcon {


	// -------------------------------------------------- //


	public EXGKit(String name) {
		super(name);

		Pair<XMaterial, Integer> defaultKitIcon = Main.getInstance().getConfiguration().getDefaultKitIcon();
		this.setMaterial(defaultKitIcon.getLeft());
		this.setData(defaultKitIcon.getRight().byteValue());
	}


	// -------------------------------------------------- //
}
