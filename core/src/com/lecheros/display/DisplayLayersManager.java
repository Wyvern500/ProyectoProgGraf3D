package com.lecheros.display;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DisplayLayersManager {

	private List<ADisplayHandler> parts;

	public DisplayLayersManager() {
		parts = new ArrayList<>();
	}

	public void tick() {
		for (ADisplayHandler handler : parts) {
			handler.tick();
			;
		}
	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		for (ADisplayHandler handler : parts) {
			handler.render(sr, font, sp);
		}
	}

	public void mouseDown(int x, int y, int button) {
		for (ADisplayHandler handler : parts) {
			handler.mouseDown(x, y, button);
		}
	}

	public void mouseUp(int x, int y, int button) {
		for (ADisplayHandler handler : parts) {
			handler.mouseUp(x, y, button);
		}
	}

	public void mouseMoved(int x, int y) {
		for (ADisplayHandler handler : parts) {
			handler.mouseMoved(x, y);
		}
	}
	
	public void mouseDragged(int x, int y) {
		for (ADisplayHandler handler : parts) {
			handler.mouseDragged(x, y);
		}
	}

	public void onScroll(float delta) {
		for (ADisplayHandler handler : parts) {
			handler.onScroll(delta);
		}
	}

	public void onKeyDown(int key) {
		for (ADisplayHandler handler : parts) {
			handler.onKeyDown(key);
		}
	}

	public void onKeyUp(int key) {
		for (ADisplayHandler handler : parts) {
			handler.onKeyUp(key);
		}
	}

	public void dispose() {
		for (ADisplayHandler handler : parts) {
			handler.dispose();
		}
	}

	public void addPart(ADisplayHandler part) {
		parts.add(part);
	}

	public void sort() {
		parts.sort(new Comparator<ADisplayHandler>() {
			@Override
			public int compare(ADisplayHandler o1, ADisplayHandler o2) {
				return o1.zindex - o2.zindex;
			}
		});
	}

	public List<ADisplayHandler> getLayers() {
		return parts;
	}

}
