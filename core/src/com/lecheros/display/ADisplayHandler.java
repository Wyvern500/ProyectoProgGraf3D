package com.lecheros.display;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.IMouseListener;
import com.lecheros.display.widgets.Widget;
import com.lecheros.model.JgModel;

public abstract class ADisplayHandler {

	protected int zindex;
	protected List<Widget> widgets;

	public ADisplayHandler(int zindex) {
		this.zindex = zindex;
		widgets = new ArrayList<>();
	}

	public void addWidget(Widget w) {
		widgets.add(w);
	}

	public int getZindex() {
		return zindex;
	}

	public void setZindex(int zindex) {
		this.zindex = zindex;
	}

	public void tick() {
		for (Widget w : widgets) {
			w.tick();
		}
	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		for (Widget w : widgets) {
			w.render(sr, font, sp);
		}
	}

	public abstract void onNewModel(JgModel model);

	public abstract void onSaveModel(JgModel model);

	public void mouseDown(int x, int y, int button) {
		for (Widget w : widgets) {
			if (w instanceof IMouseListener) {
				((IMouseListener<?>) w).mouseDown(x, y, button);
			}
		}
	}

	public void mouseUp(int x, int y, int button) {
		for (Widget w : widgets) {
			if (w instanceof IMouseListener) {
				((IMouseListener<?>) w).mouseUp(x, y, button);
			}
		}
	}

	public void mouseDragged(int x, int y) {
		for (Widget w : widgets) {
			if (w instanceof IMouseListener) {
				((IMouseListener<?>) w).mouseDragged(x, y);
			}
		}
	}
	
	public void mouseMoved(int x, int y) {
		for (Widget w : widgets) {
			if (w instanceof IMouseListener) {
				((IMouseListener<?>) w).mouseMoved(x, y);
			}
		}
	}

	public void onScroll(float delta) {
		for (Widget w : widgets) {
			if (w instanceof IMouseListener) {
				((IMouseListener<?>) w).mouseScroll(delta);
			}
		}
	}

	public void onKeyDown(int key) {
		for (Widget w : widgets) {
			if (w instanceof IKeyboardListener) {
				((IKeyboardListener<?>) w).onKeyDown(key);
			}
		}
	}

	public void onKeyUp(int key) {
		for (Widget w : widgets) {
			if (w instanceof IKeyboardListener) {
				((IKeyboardListener<?>) w).onKeyUp(key);
			}
		}
	}

	public abstract void dispose();
}
