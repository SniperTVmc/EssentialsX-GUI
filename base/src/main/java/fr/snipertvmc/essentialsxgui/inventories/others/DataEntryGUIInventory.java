package fr.snipertvmc.essentialsxgui.inventories.others;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGInventory;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.others.ConfigurableDataEntryGUI;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.items.ConfigurableItem;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.ItemBuilder;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.PaginatedFastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DataEntryGUIInventory extends PaginatedFastInv {


	// -------------------------------------------------- //


	ConfigurableDataEntryGUI config = (ConfigurableDataEntryGUI) Main.getInstance().getInventory(EXGInventory.DATA_ENTRY_GUI);


	// -------------------------------------------------- //


	public DataEntryGUIInventory(Player player, EXGEntrySettings entrySettings,
	                             Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                             Consumer<Pair<String, EXGEntryResult>> onFailure) {
		super(
				Main.getInstance().getInventory(EXGInventory.DATA_ENTRY_GUI).getRows() * 9,
				Main.getInstance().getInventory(EXGInventory.DATA_ENTRY_GUI).getTitle().build(player, Map.of(
						"entryDisplayName", entrySettings.getEntryDisplayName()))
		);


		InventoriesUtils.insertBorderItems(player, config, this);
		InventoriesUtils.initializePaginatedInventory(player, config, this, config.getInventoryScheme());


		if (config.getCancelItem().isEnabled()) {
			setItem(config.getCancelItem().getSlot(), config.getCancelItem().build(player), e -> {

				onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
				EXGEntryResult.CANCELED.playResult(player);
			});
		}


		addMaterialItems(player, entrySettings, onSuccess, onFailure);
	}


	// -------------------------------------------------- //


	private void addMaterialItems(Player player, EXGEntrySettings entrySettings,
	                              Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                              Consumer<Pair<String, EXGEntryResult>> onFailure) {


		List<Pair<XMaterial, Integer>> materialList = Main.getInstance().getConfiguration().getMaterialsList(entrySettings.getMaterialListPath());

		if (materialList.isEmpty()) {

			addContent(new ItemBuilder(XMaterial.BARRIER.get())
					.name("<dark_red><bold>No materials found")
					.lore(
							"<red>Please contact an administrator and inform them of the following details:",
							"",
							"<dark_gray>No materials found in the config file for the entry type <white>" + entrySettings.getType().name() + "<dark_gray>.",
							"<dark_gray>Path: " + entrySettings.getMaterialListPath())
					.build()
			);
			return;
		}

		for (Pair<XMaterial, Integer> materialPair : materialList) {

			ConfigurableItem materialIconItem = config.getMaterialIconItem();
			materialIconItem.setMaterial(materialPair.getLeft().name());
			materialIconItem.setData(materialPair.getRight());

			addContent(materialIconItem
					.build(player, Map.of(
							"materialName", materialPair.getLeft().name()
					)), e -> {

				String completeMaterial = materialPair.getLeft() + ":" + materialPair.getRight();
				Pair<Pair<XMaterial, Byte>, EXGEntryResult> result = DataEntryUtils.checkMaterialEntry(completeMaterial);

				if (result.getRight() == EXGEntryResult.SUCCESS) {
					onSuccess.accept(new Pair<>(completeMaterial, EXGEntryResult.SUCCESS));

				} else {
					onFailure.accept(new Pair<>(completeMaterial, result.getRight()));
					result.getRight().playResult(player);
				}
			});
		}
	}


	// -------------------------------------------------- //


	@Override
	protected void onPageChange(int page) {
		Player player = this.getInventory().getViewers().isEmpty() ? null : (Player) this.getInventory().getViewers().getFirst();
		InventoriesUtils.updateCurrentPageItem(player, config, this);
		SoundsUtils.playSound(player, EXGSound.GUI_PAGE_CHANGE);
	}


	// -------------------------------------------------- //
}
