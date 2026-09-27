package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.libby.bukkit.BukkitLibraryManager;
import fr.snipertvmc.essentialsxgui.libraries.libby.core.Library;

public class LibraryManager {


	// -------------------------------------------------- //


	public LibraryManager() {
		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();
		bukkitLibraryManager.addMavenCentral();
		bukkitLibraryManager.addJitPack();
		bukkitLibraryManager.addRepository("https://repo.papermc.io/repository/maven-public/");
		loadEssentialLibraries();
	}


	// -------------------------------------------------- //


	public void loadEssentialLibraries() {

		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.moshi")
				.artifactId("moshi")
				.version("1.15.2")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okio")
				.artifactId("okio")
				.version("3.18.1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okhttp3")
				.artifactId("okhttp")
				.version("5.5.0")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okio")
				.artifactId("okio-jvm")
				.version("3.18.2")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.jetbrains.kotlin")
				.artifactId("kotlin-stdlib")
				.version("2.4.20")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.zaxxer")
				.artifactId("HikariCP")
				.version("7.1.0")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.json")
				.artifactId("json")
				.version("20260814")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.github.cryptomorin")
				.artifactId("XSeries")
				.version("13.7.1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.slf4j")
				.artifactId("slf4j-api")
				.version("2.0.20")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.slf4j")
				.artifactId("slf4j-simple")
				.version("2.0.20")
				.build());


		// Adventure support
		if (!hasNativeAdventureSupport()) {
			bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("net.kyori")
					.artifactId("adventure-text-minimessage")
					.version("4.26.1")
					.resolveTransitiveDependencies(true)
					.build());

			bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("net.kyori")
					.artifactId("adventure-platform-bukkit")
					.version("4.4.1")
					.resolveTransitiveDependencies(true)
					.build());
		}
	}


	public void loadLibraries(String libraryName) {

		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();

		switch (libraryName) {

			case "SQLite" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("org.xerial")
					.artifactId("sqlite-jdbc")
					.version("3.50.3.0")
					.build());

			case "MariaDB" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("org.mariadb.jdbc")
					.artifactId("mariadb-java-client")
					.version("3.5.6")
					.build());

			case "MySQL" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("com.mysql")
					.artifactId("mysql-connector-j")
					.version("9.4.0")
					.build());
		}
	}


	// -------------------------------------------------- //


	public boolean hasNativeAdventureSupport() {
		try {
			Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
			Class.forName("org.bukkit.entity.Player").getMethod("sendMessage", componentClass);
			return true;
		} catch (ClassNotFoundException | NoSuchMethodException | NoClassDefFoundError e) {
			return false;
		}
	}


	// -------------------------------------------------- //
}
