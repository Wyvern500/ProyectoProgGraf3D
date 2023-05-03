package com.lecheros.display.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.animator.Engine;
import com.lecheros.display.widgets.WindowWidget;

public class WindowHandler {

	private List<WindowWidget<?>> windows;

	public WindowHandler() {
		windows = new ArrayList<>();
	}

	public void addWindow(Engine engine, WindowWidget<?> w) {
		boolean already = false;
		for(WindowWidget<?> window : windows) {
			if(window.getId().equals(w.getId())) {
				already = true;
			}
		}
		if(!already) {
			windows.add(w);
			initWidgets(engine);
		} else {
			Logger.getGlobal().info("Window already exists");
		}
	}

	public void initWidgets(Engine engine) {
		for (WindowWidget<?> w : windows) {
			w.init(engine);
		}
	}

	public void tick() {
		for (int i = 0; i < windows.size(); i++) {
			WindowWidget<?> w = windows.get(i);
			if (w.shouldDestroy()) {
				windows.remove(i);
				continue;
			}
			w.tick();
		}
	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		for (WindowWidget<?> w : windows) {
			w.render(sr, font, sp);
		}
	}

	public void reset(Engine engine) {
		for (WindowWidget<?> w : windows) {
			w.clearWidgets();
			w.init(engine);
		}
	}

	public void mouseDown(int x, int y) {
		for (WindowWidget<?> w : windows) {
			w.mouseDown(x, y);
		}
	}

	public void mouseUp(int x, int y) {
		for (WindowWidget<?> w : windows) {
			w.mouseUp(x, y);
		}
	}

	public void mouseDragged(int x, int y) {
		for (WindowWidget<?> w : windows) {
			w.mouseDragged(x, y);
		}
	}

	public void mouseMoved(int x, int y) {
		for (WindowWidget<?> w : windows) {
			w.mouseMoved(x, y);
		}
	}
	
	public void onKeyDown(int keycode) {
		for(WindowWidget<?> w : windows) {
			w.onKeyDown(keycode);
		}
	}
	
	public void onKeyUp(int keycode) {
		for(WindowWidget<?> w : windows) {
			w.onKeyUp(keycode);
		}
	}

	public WindowWidget<?> getWindow(String id) {
		for(WindowWidget<?> window : windows) {
			if(window.getId().equals(id)) {
				return window;
			}
		}
		Logger.getGlobal().info("Window with id " + id +  " not found");
		return null;
	}
	
}
