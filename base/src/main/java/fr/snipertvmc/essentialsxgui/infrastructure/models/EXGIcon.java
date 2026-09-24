package fr.snipertvmc.essentialsxgui.infrastructure.models;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

public class EXGIcon {


	// -------------------------------------------------- //


	private final String name;

	private String displayName;
	private XMaterial material;
	private int data;

	private ItemStack customItemStack;


	// -------------------------------------------------- //


	public EXGIcon(String name) {
		this.name = name;

		this.displayName = name;
		this.data = 0;
	}


	// -------------------------------------------------- //


	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	public XMaterial getMaterial() {
		return material;
	}
	public int getData() {
		return data;
	}

	public ItemStack getCustomItemStack() {
		return customItemStack;
	}


	// -------------------------------------------------- //


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setMaterial(XMaterial material) {
		this.material = material;
	}
	public void setData(int data) {
		this.data = data;
	}

	public void setCustomItemStack(ItemStack customItemStack) {
		this.customItemStack = customItemStack;
	}


	// -------------------------------------------------- //
}
