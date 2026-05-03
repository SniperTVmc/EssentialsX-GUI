package fr.snipertvmc.essentialsxgui.utilities.data;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.inventories.others.DataEntryAnvilInventory;
import fr.snipertvmc.essentialsxgui.inventories.others.DataEntryGUIInventory;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.type.TypeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DataEntryUtils {


	// -------------------------------------------------- //


	public static void processStringEntry(Player player, EXGEntrySettings entrySettings,
	                                      Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                                      Consumer<Pair<String, EXGEntryResult>> onFailure) {

		if (!entrySettings.getAcceptedTypes().contains(entrySettings.getType())) {
			onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
			throw new IllegalArgumentException("The entry type " + entrySettings.getType() + " is not accepted in the accepted types list.");
		}

		switch (entrySettings.getType()) {

			case CHAT -> Main.getInstance().getChatManager().addChat(player, entry -> {

				Pair<String, EXGEntryResult> result = checkStringEntry(entry, entrySettings);

				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					if (result.getRight() == EXGEntryResult.SUCCESS) {
						onSuccess.accept(result);
					} else {
						onFailure.accept(result);
						result.getRight().playResult(player);
					}
				});

			}, Main.getInstance().getConfiguration().getDelayForTypingInChat());

			case ANVIL -> new DataEntryAnvilInventory(player, entrySettings, onSuccess, onFailure);

			case GUI -> new DataEntryGUIInventory(player, entrySettings, onSuccess, onFailure).open(player);
		}

	}


	public static Pair<String, EXGEntryResult> checkStringEntry(String value, EXGEntrySettings entrySettings) {

		if (entrySettings.getEqualsToSomething() != null) {
			if (value.equalsIgnoreCase(entrySettings.getEqualsToSomething())) {
				return new Pair<>(value, EXGEntryResult.SUCCESS);
			}
			return new Pair<>(value, EXGEntryResult.CANCELED);
		}

		if (value.equalsIgnoreCase("cancel")) {
			return new Pair<>(value, EXGEntryResult.CANCELED);
		}

		Pair<String, EXGEntryResult> formatAnalysisResult = getFormatAnalysisResult(value);
		if (formatAnalysisResult != null) getFormatAnalysisResult(value);

		if ( (entrySettings.getMinLength() != -1 && value.length() < entrySettings.getMinLength())
			|| (entrySettings.getMaxLength() != -1 && value.length() > entrySettings.getMaxLength()) ) {

			return new Pair<>(value, EXGEntryResult.LENGTH_LIMIT.setMessageVariables(
					Map.of("min", String.valueOf(entrySettings.getMinLength()),
							"max", String.valueOf(entrySettings.getMaxLength()))
			));
		}

		if (entrySettings.isMustBeNumber() && !TypeUtils.isInteger(value)) {
			return new Pair<>(value, EXGEntryResult.INVALID_NUMBER);
		}

		if (entrySettings.getCharactersListPath() != null) {
			String charactersList = Main.getInstance().getConfiguration().getCharacterList(entrySettings.getCharactersListPath());
			if (!charactersList.isEmpty()) {
				return getCharactersAnalysisResult(charactersList, value);
			}
		}

		return new Pair<>(value, EXGEntryResult.SUCCESS);
	}


	// -------------------------------------------------- //


	public static void processMaterialEntry(Player player, EXGEntrySettings entrySettings,
	                                        Consumer<Pair<Pair<XMaterial, Byte>, EXGEntryResult>> onSuccess,
	                                        Consumer<Pair<Pair<XMaterial, Byte>, EXGEntryResult>> onFailure) {

		if (!entrySettings.getAcceptedTypes().contains(entrySettings.getType())) {
			onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
			throw new IllegalArgumentException("The entry type " + entrySettings.getType() + " is not accepted in the accepted types list.");
		}

		switch (entrySettings.getType()) {

			case CHAT -> Main.getInstance().getChatManager().addChat(player, entry -> {

				Pair<Pair<XMaterial, Byte>, EXGEntryResult> result = checkMaterialEntry(entry);

				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					if (result.getRight() == EXGEntryResult.SUCCESS) {
						onSuccess.accept(result);
					} else {
						onFailure.accept(result);
						result.getRight().playResult(player);
					}
				});

			}, Main.getInstance().getConfiguration().getDelayForTypingInChat());

			case ANVIL -> new DataEntryAnvilInventory(player, entrySettings,

					materialPairResult -> {

						Pair<Pair<XMaterial, Byte>, EXGEntryResult> result = checkMaterialEntry(materialPairResult.getLeft());

						if (result.getRight() == EXGEntryResult.SUCCESS) {
							onSuccess.accept(result);
						} else {
							onFailure.accept(result);
							result.getRight().playResult(player);
						}

					},
					materialPairResult -> onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED))
			);

			case GUI -> new DataEntryGUIInventory(player, entrySettings,

					materialPairResult -> {

						Pair<Pair<XMaterial, Byte>, EXGEntryResult> result = checkMaterialEntry(materialPairResult.getLeft());

						if (result.getRight() == EXGEntryResult.SUCCESS) {
							onSuccess.accept(result);
						} else {
							onFailure.accept(result);
							result.getRight().playResult(player);
						}

					},
					materialPairResult -> onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED))

			).open(player);
		}

	}


	public static Pair<Pair<XMaterial, Byte>, EXGEntryResult> checkMaterialEntry(String value) {

		if (value.equalsIgnoreCase("cancel")) {
			return new Pair<>(null, EXGEntryResult.CANCELED);
		}

		XMaterial material;
		byte data = 0;

		if (value.contains(":")) {

			String[] materialSplit = value.split(":");
			if (materialSplit.length != 2 || !TypeUtils.isByte(materialSplit[1])) {
				return new Pair<>(null, EXGEntryResult.INVALID_MATERIAL);
			}

			material = XMaterial.matchXMaterial(materialSplit[0]).orElse(null);
			data = Byte.parseByte(materialSplit[1]);

		} else {
			material = XMaterial.matchXMaterial(value).orElse(null);
		}

		if (material == null) {
			return new Pair<>(null, EXGEntryResult.INVALID_MATERIAL);
		}

		return new Pair<>(new Pair<>(material, data), EXGEntryResult.SUCCESS);
	}


	// -------------------------------------------------- //


	private static Pair<String, EXGEntryResult> getFormatAnalysisResult(String value) {

		if (TextUtils.hasMixedFormat(value)) {
			return new Pair<>(value, EXGEntryResult.INVALID_MIXED_FORMAT);
		}

		if (Main.getInstance().getConfiguration().acceptOnlyMiniMessageFormatInEntries() && TextUtils.hasLegacyFormat(value)) {
			return new Pair<>(value, EXGEntryResult.INVALID_MINIMESSAGE_FORMAT);
		}

		if (!Main.getInstance().getConfiguration().acceptOnlyMiniMessageFormatInEntries() && TextUtils.hasMiniMessageFormat(value)) {
			return new Pair<>(value, EXGEntryResult.INVALID_LEGACY_FORMAT);
		}

		return null;
	}


	private static Pair<String, EXGEntryResult> getCharactersAnalysisResult(String characterListString, String value) {

		if (characterListString.startsWith("regex:")) {
			String regex = characterListString.substring("regex:".length());
			Pattern pattern = Pattern.compile(regex);
			if (!pattern.matcher(value).matches()) {
				return new Pair<>(value, EXGEntryResult.INVALID_CHARACTER);
			}

		} else {
			for (String character : value.split("")) {
				if (!characterListString.contains(character)) {
					return new Pair<>(value, EXGEntryResult.INVALID_CHARACTER);
				}
			}
		}

		return new Pair<>(value, EXGEntryResult.SUCCESS);
	}


	// -------------------------------------------------- //
}
