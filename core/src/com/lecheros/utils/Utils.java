package com.lecheros.utils;

import java.util.logging.Logger;

public class Utils {
	
	public static void log(String text) {
		Logger.getGlobal().info(text);
	}
	
	public static String remove(String text, int index) {
		String res = "";
		for(int i = 0; i < text.length(); i++) {
			if(i != index) {
				res += text.charAt(i);
			}
		}
		return res;
	}
	
}
