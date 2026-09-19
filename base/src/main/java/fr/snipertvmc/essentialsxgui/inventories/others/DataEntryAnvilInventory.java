package fr.snipertvmc.essentialsxgui.inventories.others;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DataEntryAnvilInventory {


	// -------------------------------------------------- //


	public DataEntryAnvilInventory(Player player, EXGEntrySettings entrySettings,
	                               Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                               Consumer<Pair<String, EXGEntryResult>> onFailure) {

		AtomicBoolean canceledOnClose = new AtomicBoolean(true);

		new AnvilGUI.Builder()

				.text(MessagesUtils.getString(EXGMessage.TYPE_HERE))
				.title(TextUtils.parseAsString(entrySettings.getEntryDisplayName()))

				.onClick((slot, stateSnapshot) -> {

					canceledOnClose.set(false);

					if (slot != AnvilGUI.Slot.OUTPUT) {
						return List.of();
					}

					EXGEntrySettings defaultSettings = new EXGEntrySettings(EXGEntryType.CHAT)
							.setMinLength(entrySettings.getMinLength())
							.setMaxLength(entrySettings.getMaxLength());

					String text = stateSnapshot.getText();
					Pair<String, EXGEntryResult> result = DataEntryUtils.checkStringEntry(text, defaultSettings);

					if (result.getRight() == EXGEntryResult.SUCCESS) {
						onSuccess.accept(result);
					} else {
						onFailure.accept(result);
						result.getRight().playResult(player);
					}

					return List.of();
				})

				.onClose(stateSnapshot -> {

					if (!canceledOnClose.get()) {
						return;
					}

					Bukkit.getScheduler().runTask(Main.getInstance(), () -> onFailure.accept(new Pair<>("", EXGEntryResult.CANCELED)));
					EXGEntryResult.CANCELED.playResult(player);
				})

				.plugin(Main.getInstance())
				.open(player);
	}


	// -------------------------------------------------- //
}
