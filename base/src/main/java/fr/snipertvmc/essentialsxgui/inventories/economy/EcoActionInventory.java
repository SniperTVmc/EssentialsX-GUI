package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEcoAction;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.ConfigurableEcoActionInventory;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;

public class EcoActionInventory extends FastInv {


	// -------------------------------------------------- //


	private final ConfigurableEcoActionInventory config = (ConfigurableEcoActionInventory) Main.getInstance().getInventory(EXGInventory.ECO_ACTION);


	// -------------------------------------------------- //


	public EcoActionInventory(Player player, Player target) {
		super(
				Main.getInstance().getInventory(EXGInventory.ECO_ACTION).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.ECO_ACTION).getTitle()
						.build(player, Map.of("target", target.getName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);


		if (config.getPlayerItem().isEnabled()) {

			BigDecimal targetBalanceValue = Main.getInstance().getEssentials().getUser(target).getMoney();
			String targetBalance = NumberUtil.displayCurrency(targetBalanceValue, Main.getInstance().getEssentials());

			setItem(config.getPlayerItem().getSlot(), config.getPlayerItem()
					.build(player, Map.of(
							"targetName", target.getName(),
							"targetBalance", targetBalance)
					));
		}


		if (config.getAddBalanceItem().isEnabled()) {
			setItem(config.getAddBalanceItem().getSlot(), config.getAddBalanceItem().build(player), e -> {

				new EcoAmountInventory(player, target, EXGEcoAction.GIVE).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		if (config.getTakeBalanceItem().isEnabled()) {
			setItem(config.getTakeBalanceItem().getSlot(), config.getTakeBalanceItem().build(player), e -> {

				new EcoAmountInventory(player, target, EXGEcoAction.TAKE).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		if (config.getSetBalanceItem().isEnabled()) {
			setItem(config.getSetBalanceItem().getSlot(), config.getSetBalanceItem().build(player), e -> {

				new EcoAmountInventory(player, target, EXGEcoAction.SET).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		if (config.getResetBalanceItem().isEnabled()) {
			setItem(config.getResetBalanceItem().getSlot(), config.getResetBalanceItem().build(player), e -> {

				new EcoAmountInventory(player, target, EXGEcoAction.RESET).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
			});
		}


		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(player), e -> {

				new EcoPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //
}