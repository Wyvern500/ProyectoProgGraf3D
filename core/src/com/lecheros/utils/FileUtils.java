package com.lecheros.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

	public static String readFile(File file) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(file));
			String all = "";
			String line = "";
			while ((line = bfr.readLine()) != null) {
				all += line + "\n";
			}
			bfr.close();
			return all;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
