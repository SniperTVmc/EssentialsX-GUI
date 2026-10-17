package fr.snipertvmc.essentialsxgui.managers;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.List;

public interface EssentialsManager {


	// -------------------------------------------------- //


	Essentials essentials = Main.getInstance().getEssentials();


	// -------------------------------------------------- //


	boolean canCreateHome(Player player);


	// -------------------------------------------------- //


	List<ItemStack> getKitItems(String kitName);


	// -------------------------------------------------- //


	void createWarpWithPlayer(Player player, String warpName) throws Exception;


	// -------------------------------------------------- //


	List<ItemStack> sellItems(Player player, List<ItemStack> items) throws Exception;
	BigDecimal customSellItem(final User user, final ItemStack is) throws Exception;


	// -------------------------------------------------- //


	boolean condenseItems(final Player player, final List<ItemStack> items);


	// -------------------------------------------------- //
}
