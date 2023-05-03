package com.lecheros.utils;

public class SerializerUtils {

	public static String getModelName(String data) {
		String[] spData = data.split("\n");
		return spData[1].split(":")[1];
	}

}
