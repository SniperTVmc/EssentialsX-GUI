package fr.snipertvmc.essentialsxgui.utilities.serializers;

import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemStackSerializer {


	// -------------------------------------------------- //


	public static String serialize(ItemStack itemStack) {

		try {
			return itemStackArrayToBase64(new ItemStack[]{itemStack});

		} catch (IllegalStateException exception) {
			throw new IllegalStateException(exception);
		}
	}


	public static String serialize(ItemStack[] itemStacks) {

		try {
			return itemStackArrayToBase64(itemStacks);

		} catch (IllegalStateException exception) {
			throw new IllegalStateException(exception);
		}
	}


	private static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {

		if (items == null) {
			return null;
		}

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		     BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

			dataOutput.writeInt(items.length);

			for (ItemStack item : items) {
				dataOutput.writeObject(item);
			}

			return Base64.getEncoder().encodeToString(outputStream.toByteArray());

		} catch (IOException e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}


	// -------------------------------------------------- //


	public static ItemStack[] deserialize(String data) {

		try {
			return itemStackArrayFromBase64(data);

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}


	private static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {

		if (data == null || data.isEmpty()) {
			return new ItemStack[0];
		}

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
		     BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

			ItemStack[] items = new ItemStack[dataInput.readInt()];
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			return items;

		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		} catch (IllegalArgumentException e) {
			ConsoleLogger.exception(e);
			ConsoleLogger.error("Unable to decode item stacks from Base64 string. The data may be corrupted or invalid.");
			return new ItemStack[0];
		}
	}


	// -------------------------------------------------- //
}
