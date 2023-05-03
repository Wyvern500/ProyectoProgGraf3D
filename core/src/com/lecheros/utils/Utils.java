package com.lecheros.utils;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.badlogic.gdx.math.Vector2;

public class Utils {

	public static void log(String text) {
		Logger.getGlobal().info(text);
	}
	
	public static boolean collidesWithMouse(float mx, float my, Vector2 pos, float w, float h) {
		return mx > pos.x && mx < pos.x + w && my > pos.y && my < pos.y + h;
	}

	public static void showMessage(String txt) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, txt);
			}
		}).start();
	}

	public static String getRestOfString(String[] arr, int start) {
		Logger.getGlobal().info("Input arr: " + Arrays.toString(arr));
		String res = "";
		for (int i = start; i < arr.length; i++) {
			res += arr[i] + "\n";
		}
		return res;
	}

	public static String[] getFromToString(String[] arr, int start, int end) {
		String[] res = new String[end - start];
		for (int i = start; i < end; i++) {
			res[i - start] = arr[i];
		}
		return res;
	}

	public static String remove(String text, int index) {
		String res = "";
		for (int i = 0; i < text.length(); i++) {
			if (i != index) {
				res += text.charAt(i);
			}
		}
		return res;
	}

}
