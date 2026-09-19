package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import org.bukkit.configuration.file.YamlConfiguration;

public class BaseFile {


	// -------------------------------------------------- //


	private final YamlConfiguration yamlConfiguration;

	private final String fileName;
	private final String filePath;


	// -------------------------------------------------- //


	public BaseFile(YamlConfiguration yamlConfiguration, String filePath) {
		this.yamlConfiguration = yamlConfiguration;

		this.fileName = filePath.split("/")[filePath.split("/").length - 1].replace(".yml", "");
		this.filePath = filePath;
	}


	// -------------------------------------------------- //


	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}

	public String getFileName() {
		return fileName;
	}
	public String getFilePath() {
		return filePath;
	}


	public String getFileVersion() {
		return yamlConfiguration.getString("fileVersion", "File version not found.");
	}


	// -------------------------------------------------- //
}
