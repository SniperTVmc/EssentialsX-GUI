package fr.snipertvmc.essentialsxgui.infrastructure.models.punishment;

public class EXGKick {


	// -------------------------------------------------- //


	private int id;
	private String material;
	private String reason;
	private String description;


	// -------------------------------------------------- //


	public EXGKick(int id, String material, String reason, String description) {
		this.id = id;
		this.material = material;
		this.reason = reason;
		this.description = description;
	}


	// -------------------------------------------------- //


	public int getId() {
		return id;
	}
	public String getMaterial() {
		return material;
	}
	public String getReason() {
		return reason;
	}
	public String getDescription() {
		return description;
	}


	// -------------------------------------------------- //


	public void setId(int id) {
		this.id = id;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	// -------------------------------------------------- //
}
