package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;

public class EXGHome extends EXGIcon {


	// -------------------------------------------------- //


	public EXGHome(String name) {
		super(name);

		Pair<XMaterial, Integer> defaultHomeIcon = Main.getInstance().getConfiguration().getDefaultHomeIcon();
		this.setMaterial(defaultHomeIcon.getLeft());
		this.setData(defaultHomeIcon.getRight().byteValue());
	}


	// -------------------------------------------------- //
}
