package com.lecheros;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.lecheros.LecherosAnimator;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Animator");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.setWindowSizeLimits(1000, 600, 1000, 600);
		new Lwjgl3Application(new LecherosAnimator(), config);
	}
}
