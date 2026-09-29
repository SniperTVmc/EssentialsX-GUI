package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import org.bukkit.Material;

public enum EXGEntryType {


	// -------------------------------------------------- //


	CHAT(String.class),
	GUI(Material.class),
	ANVIL(String.class),
	ITEM_IN_HAND(Material.class),
	DIALOG(String.class);


	// -------------------------------------------------- //


	private final Object typeAccepted;


	// -------------------------------------------------- //


	EXGEntryType(Object typeAccepted) {
		this.typeAccepted = typeAccepted;
	}


	// -------------------------------------------------- //


	public Object getTypeAccepted() {
		return typeAccepted;
	}


	// -------------------------------------------------- //
}
