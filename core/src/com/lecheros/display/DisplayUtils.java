package com.lecheros.display;

import com.badlogic.gdx.Gdx;

public class DisplayUtils {

	public static void viewPort(int x, int y, int width, int height) {
		Gdx.gl.glViewport(x, y, width, height);
	}

}
