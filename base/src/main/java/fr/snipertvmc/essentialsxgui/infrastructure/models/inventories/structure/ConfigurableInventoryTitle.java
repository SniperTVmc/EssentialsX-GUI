package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfigurableInventoryTitle {


	// -------------------------------------------------- //


	private boolean isCopied = false;
	private String title;


	// -------------------------------------------------- //


	public ConfigurableInventoryTitle(String title) {
		this.title = title;
	}


	public ConfigurableInventoryTitle(ConfigurableInventoryTitle configurableInventoryTitle) {
		this.isCopied = true;
		this.title = configurableInventoryTitle.getTitle();
	}


	// -------------------------------------------------- //


	public String getTitle() {
		return title;
	}


	// -------------------------------------------------- //


	public void setTitle(String title) {
		this.title = title;
	}


	// -------------------------------------------------- //


	private void applyVariables(Map<String, String> variables) {
		variables.forEach((key, value) -> {
			title = title.replace("{" + key + "}", value);
		});
	}


	private void applyPlaceholders(Player player) {
		if (Main.getInstance().getHookManager().getPlaceholderAPIHook().isSupported()) {
			title = PlaceholderAPI.setPlaceholders(player, title);
		}
	}



	// -------------------------------------------------- //



	public String build(Player player) {
		return build(player, Map.of());
	}


	public String build(Player player, Map<String, String> variables) {
		if (!isCopied) {
			return get().build(player, variables);
		}
		applyVariables(variables);
		applyPlaceholders(player);
		return title;
	}


	// -------------------------------------------------- //


	public ConfigurableInventoryTitle get() {
		return new ConfigurableInventoryTitle(this);
	}


	// -------------------------------------------------- //
}
