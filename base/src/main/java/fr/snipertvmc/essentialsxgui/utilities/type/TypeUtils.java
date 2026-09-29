package fr.snipertvmc.essentialsxgui.utilities.type;

import java.util.Base64;

public class TypeUtils {


	// -------------------------------------------------- //


	public static boolean isByte(String value) {
		try {
			Byte.parseByte(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static boolean isShort(String value) {
		try {
			Short.parseShort(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static boolean isLong(String value) {
		try {
			Long.parseLong(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static boolean isFloat(String value) {
		try {
			Float.parseFloat(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}


	// -------------------------------------------------- //


	public static boolean isBoolean(String value) {
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
	}


	public static boolean isBase64(String value) {
		try {
			Base64.getDecoder().decode(value);
			return true;

		} catch (IllegalArgumentException e) {
			return false;
		}
	}


	// -------------------------------------------------- //
}
