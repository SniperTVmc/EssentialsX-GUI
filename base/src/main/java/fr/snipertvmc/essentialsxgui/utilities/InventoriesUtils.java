package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGIcon;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurableInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.ConfigurablePaginatedInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.InventoryScheme;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.stream.Collectors;

public class InventoriesUtils {


	// -------------------------------------------------- //


	public static void insertBorderItems(Player player, ConfigurableInventory config, FastInv inv) {
		if (!config.getBorderItems().isEmpty()) {
			config.getBorderItems().forEach(borderItem -> {
				inv.setItem(borderItem.getSlot(), borderItem.build(player));
			});
		}
	}


	public static void insertCloseItem(Player player, ConfigurableItem closeItem, FastInv inv) {
		if (closeItem != null) {
			inv.setItem(closeItem.getSlot(), closeItem.build(player), e -> {
				e.getWhoClicked().closeInventory();
				SoundsUtils.playSound(player, EXGSound.GUI_CLOSE);
			});
		}
	}


	// -------------------------------------------------- //



	public static void initializePaginatedInventory(Player player, ConfigurablePaginatedInventory config, PaginatedFastInv inv, InventoryScheme scheme) {
		scheme.apply(inv);
		insertPaginationItems(player, config, inv);
		updateCurrentPageItem(player, config, inv);
	}


	public static void insertPaginationItems(Player player, ConfigurablePaginatedInventory config, PaginatedFastInv inv) {
		if (config.getPreviousPageItem() != null) {
			inv.previousPageItem(config.getPreviousPageItem().getSlot(), p -> config.getPreviousPageItem()
					.build(player, Map.of(
							"currentPage", String.valueOf(p + 1),
							"previousPage", String.valueOf(p))
					));
		}
		if (config.getNextPageItem() != null) {
			inv.nextPageItem(config.getNextPageItem().getSlot(), p -> config.getNextPageItem()
					.build(player, Map.of(
							"currentPage", String.valueOf(p - 1),
							"nextPage", String.valueOf(p))
					));
		}
	}


	public static void updateCurrentPageItem(Player player, ConfigurablePaginatedInventory config, PaginatedFastInv inv) {
		if (config.getCurrentPageItem() != null) {
			inv.setItem(config.getCurrentPageItem().getSlot(), config.getCurrentPageItem()
					.build(player, Map.of("currentPage", String.valueOf(inv.currentPage()),
							"totalPages", String.valueOf(inv.lastPage()),
							"previousPage", String.valueOf(inv.currentPage() - 1),
							"nextPage", String.valueOf(inv.currentPage() + 1))
					));
		}
	}


	// -------------------------------------------------- //


	public static ItemStack getCustomItemStack(ConfigurableItem configurableItem, EXGIcon target, String nameKey, Player player) {

		configurableItem.setMaterial(target.getMaterial().name());
		configurableItem.setData(target.getData());

		String placeholderDisplayName = "{" + nameKey + "DisplayName}";
		String placeholderName = "{" + nameKey + "Name}";

		if (target.getCustomItemStack() != null) {
			ItemStack itemStack = target.getCustomItemStack().clone();
			ItemMeta meta = itemStack.getItemMeta();

			if (meta != null) {
				meta.setDisplayName(configurableItem.getDisplayName()
						.replace(placeholderDisplayName, target.getDisplayName())
						.replace(placeholderName, target.getName()));

				meta.setLore(configurableItem.getLore().stream()
						.map(line -> line
								.replace(placeholderDisplayName, target.getDisplayName())
								.replace(placeholderName, target.getName()))
						.collect(Collectors.toList()));

				itemStack.setItemMeta(meta);
			}
			return itemStack;

		} else {
			Map<String, String> variables = Map.of(
					nameKey + "DisplayName", target.getDisplayName(),
					nameKey + "Name", target.getName()
			);

			return configurableItem.build(player, variables);
		}
	}


	// -------------------------------------------------- //
}
