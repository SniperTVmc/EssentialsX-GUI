package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesFile extends BaseFile {


	// -------------------------------------------------- //


	public MessagesFile(YamlConfiguration yamlConfiguration) {
		super(yamlConfiguration, "messages.yml");
	}


	// -------------------------------------------------- //


	public String getPrefix() {
		return getYamlConfiguration().getString("prefix");
	}


	// -------------------------------------------------- //


	public String getString(String path) {
		return getYamlConfiguration().getString(path);
	}
	public String getString(String path, String defaultValue) {
		return getYamlConfiguration().getString(path, defaultValue);
	}


	public double getNumber(String path) {
		return getYamlConfiguration().getDouble(path);
	}
	public double getNumber(String path, double defaultValue) {
		return getYamlConfiguration().getDouble(path, defaultValue);
	}

	public boolean getBoolean(String path) {
		return getYamlConfiguration().getBoolean(path);
	}
	public boolean getBoolean(String path, boolean defaultValue) {
		return getYamlConfiguration().getBoolean(path, defaultValue);
	}


	// -------------------------------------------------- //
}
