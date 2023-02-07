package com.lecheros.animator.handlers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.Widget;

public class DisplayHandler {

	private List<Widget> widgets;
	
	public DisplayHandler() {
		widgets = new ArrayList<>();
	}
	
	public void addWidget(Widget widget) {
		widgets.add(widget);
	}
	
	public void tick() {
		for(Widget w : widgets) {
			w.tick();
		}
	}
	
	public void render(ShapeRenderer sr, SpriteBatch batch) {
		for(Widget w : widgets) {
			w.render(sr, batch);
		}
	}
	
	public void clickDown(int x, int y, int button) {
		for(Widget w : widgets) {
			w.clickDown(x, y, button);
		}
	}
	
	public void clickUp(int x, int y, int button) {
		for(Widget w : widgets) {
			w.clickUp(x, y, button);
		}
	}
	
	public void keyDown(int keycode) {
		for(Widget w : widgets) {
			if(w instanceof IKeyboardListener) {
				((IKeyboardListener)w).keyDown(keycode);
			}
		}
	}
	
	public void keyUp(int keycode) {
		for(Widget w : widgets) {
			if(w instanceof IKeyboardListener) {
				((IKeyboardListener)w).keyUp(keycode);
			}
		}
	}
	
	public void dispose() {
		for(Widget w : widgets) {
			w.dispose();
		}
	}
	
}
