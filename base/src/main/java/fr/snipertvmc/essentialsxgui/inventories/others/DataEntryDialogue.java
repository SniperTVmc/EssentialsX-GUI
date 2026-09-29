package fr.snipertvmc.essentialsxgui.inventories.others;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.DataEntryUtils;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class DataEntryDialogue {


	// -------------------------------------------------- //


	@SuppressWarnings("UnstableApiUsage")
	public DataEntryDialogue(Player player, EXGEntrySettings entrySettings,
	                         Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                         Consumer<Pair<String, EXGEntryResult>> onFailure) {


		// Determine kind of dialog to use based on the entry settings
		DialogBase dialogBase = entrySettings.getEqualsToSomething() == null

				// If no specific value is required, use a standard dialog with a text input
				? DialogBase.builder(Component.text(TextUtils.parseAsString(entrySettings.getEntryDisplayName())))
				.inputs(List.of(
						DialogInput.text("dataEntry", Component.text(TextUtils.parseAsString(EXGMessage.TYPE_HERE))).build()
				))
				.build()

				// If a specific value is required, use a yes/no confirmation dialog
				: DialogBase.builder(Component.text(TextUtils.parseAsString(entrySettings.getEntryDisplayName())))
				.build();


		// Create the dialog with the specified settings and actions
		Dialog dialog = Dialog.create(builder -> builder.empty()

				.base(dialogBase)

				.type(DialogType.confirmation(
						ActionButton.create(
								Component.text(TextUtils.parseAsString(EXGMessage.CONFIRM)),
								Component.text(TextUtils.parseAsString(EXGMessage.CONFIRM_DESCRIPTION)),
								Main.getInstance().getConfiguration().getDialogButtonsWidth(),
								DialogAction.customClick(
										(view, audience) -> {
											if (!(audience instanceof Player dialogPlayer)) return;


											// Case 1. Yes/No confirmation dialog
											if (entrySettings.getEqualsToSomething() != null) {
												onSuccess.accept(new Pair<>(entrySettings.getEqualsToSomething(), EXGEntryResult.SUCCESS));
												return;
											}


											// Case 2. Standard dialog with text input
											String input = view.getText("dataEntry");
											Pair<String, EXGEntryResult> result = DataEntryUtils.checkStringEntry(input, entrySettings);

											if (result.getRight() == EXGEntryResult.SUCCESS) {
												onSuccess.accept(result);
											} else {
												onFailure.accept(result);
												result.getRight().playResult(dialogPlayer);
											}
										},
										ClickCallback.Options.builder()
												.uses(1)
												.lifetime(ClickCallback.DEFAULT_LIFETIME)
												.build()
								)
						),
						ActionButton.create(
								Component.text(TextUtils.parseAsString(EXGMessage.CANCEL)),
								Component.text(TextUtils.parseAsString(EXGMessage.CANCEL_DESCRIPTION)),
								Main.getInstance().getConfiguration().getDialogButtonsWidth(),
								DialogAction.customClick(
										(view, audience) -> {
											if (!(audience instanceof Player)) return;

											onFailure.accept(new Pair<>("", EXGEntryResult.CANCELED));
											EXGEntryResult.CANCELED.playResult(player);
										},
										ClickCallback.Options.builder()
												.uses(1)
												.lifetime(ClickCallback.DEFAULT_LIFETIME)
												.build()
								)
						)
				))
		);

		player.showDialog(dialog);
	}


	// -------------------------------------------------- //


}
