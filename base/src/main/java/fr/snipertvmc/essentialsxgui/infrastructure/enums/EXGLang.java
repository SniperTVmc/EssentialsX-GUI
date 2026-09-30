package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGLang {


	// -------------------------------------------------- //


	ENGLISH("en", "English"),
	FRENCH("fr", "Français"),
	UNKNOWN("unk", "Unknown");


	// -------------------------------------------------- //


	private String key;
	private final String displayName;


	// -------------------------------------------------- //


	EXGLang(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}


	// -------------------------------------------------- //


	public String getKey() {
		return key;
	}
	public String getDisplayName() {
		return displayName;
	}

	public void setKey(String key) {
		this.key = key;
	}


	// -------------------------------------------------- //


	public static EXGLang getByKey(String key) {
		for (EXGLang lang : EXGLang.values()) {
			if (lang.getKey().equalsIgnoreCase(key)) {
				return lang;
			}
		}
		EXGLang.UNKNOWN.setKey(key);
		return UNKNOWN;
	}


	// -------------------------------------------------- //
}
