package com.lecheros.display.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lecheros.animator.Engine;

public abstract class WindowWidget<T> {

	protected List<Actor> widgets;
	protected Map<Integer, Vector2> offsets;

	protected T parent;
	
	protected int w;
	protected int h;

	protected boolean visible;
	protected boolean destroy;
	protected boolean overX;
	protected boolean canMove;

	protected Vector2 pos;
	protected Vector2 offset;

	protected Color color;

	protected Engine engine;
	
	protected String id;

	public WindowWidget(T parent, int w, int h, String id) {
		this(parent, 0, 0, w, h, id);
	}

	public WindowWidget(T parent, int x, int y, int w, int h, String id) {
		this.parent = parent;
		this.pos = new Vector2(x, Gdx.graphics.getHeight() - y);
		this.offset = new Vector2();
		this.w = w;
		this.h = h;
		this.id = id;
		this.color = new Color(0.3f, 0.3f, 0.3f, 1.0f);
		widgets = new ArrayList<>();
		offsets = new HashMap<>();
	}

	public Vector2 toWindow(int x, int y, float h) {
		int nx = (int) MathUtils.clamp(pos.x + x, pos.x, pos.x + w);
		int ny = (int) MathUtils.clamp(pos.y - y, pos.y, pos.y - h);
		Logger.getGlobal().info("Pos: " + pos.toString() + " y: " + y + " pos.y - y: "
				+ (Gdx.graphics.getHeight() - Math.abs(pos.y - y)));
		return new Vector2(nx, Gdx.graphics.getHeight() - (pos.y + (y + h + 20)));
	}

	public Vector2 toWindow(float x, float y) {
		float nx = MathUtils.clamp(pos.x + x, pos.x, pos.x + w);
		float ny = MathUtils.clamp(pos.y + y, pos.y, pos.y + h);
		return new Vector2(nx, ny);
	}

	public Vector2 toWindow(Vector2 vec, float h) {
		float nx = MathUtils.clamp(pos.x + vec.x, pos.x, pos.x + w);
		float ny = MathUtils.clamp(pos.y + vec.y, pos.y, pos.y + h);
		return new Vector2(nx, Gdx.graphics.getHeight() - (pos.y + (vec.y + h + 20)));
	}

	public void addWidget(Actor a, int x, int y) {
		offsets.put(widgets.size(), new Vector2(x, y));
		widgets.add(a);
	}

	public abstract void initWidgets();

	public void init(Engine engine) {
		this.engine = engine;
		this.initWidgets();
		for (Actor a : widgets) {
			engine.addActor(a);
		}
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void tick() {

	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color c = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(color.cpy().sub(0.1f, 0.1f, 0.1f, 0));
		sr.rect(pos.x, pos.y, w, 20);
		Color o = overX ? Color.BLACK : sr.getColor();
		sr.setColor(o);
		sr.rect(pos.x + w - 26, pos.y, 26, 20);
		sr.setColor(Color.WHITE);
		sr.rect(pos.x + w - 18, pos.y + 8, 10, 2);
		sr.setColor(color);
		sr.rect(pos.x, pos.y + 20, w, h);
		sr.end();
		sr.setColor(c);
	}

	public void text(BitmapFont font, SpriteBatch sp, String text, float x, float y) {
		sp.begin();
		Vector2 p = toWindow(x, y);
		font.draw(sp, text, p.x, p.y);
		sp.end();
	}

	public void clearWidgets() {
		for (Actor a : widgets) {
			a.remove();
		}
		widgets.clear();
	}

	// Misc methods

	protected void setWPos(Actor a, int x, int y) {
		Vector2 fieldPos = toWindow(x, y, (int) a.getHeight());
		a.setPosition(fieldPos.x, fieldPos.y);
		addWidget(a, x, y);
	}

	public boolean insideBounds(int x, int y) {
		if (!(x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h)) {
			return false;
		}
		return true;
	}

	public boolean collide(int x, int y) {
		return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
	}

	public boolean collide(int x, int y, int ox, int oy, int w, int h) {
		return x > ox && x < ox + w && y > oy && y < oy + h;
	}

	public void mouseDown(int x, int y) {
		if (!insideBounds(x, y))
			return;
		if (overX) {
			Logger.getGlobal().info("Destroying window");
			destroy();
		}
		if (collide(x, y, (int) pos.x, (int) pos.y, w, 20)) {
			canMove = true;
			offset.set(x - pos.x - w, y - pos.y - 20);
		}
	}

	public void mouseUp(int x, int y) {
		canMove = false;
	}

	public void mouseDragged(int x, int y) {
		if (canMove) {
			this.pos.set(x - (w - Math.abs(offset.x)), y - (20 - Math.abs(offset.y)));
			for (int i = 0; i < widgets.size(); i++) {
				Actor a = widgets.get(i);
				Vector2 fieldPos = toWindow(offsets.get(i), a.getHeight());
				a.setPosition(fieldPos.x, fieldPos.y);
			}
		}
	}

	public void mouseMoved(int x, int y) {
		if (collide(x, y, (int) pos.x + w - 26, (int) pos.y, 26, 20)) {
			overX = true;
		} else {
			overX = false;
		}
	}
	
	public void onKeyDown(int keycode) {
		
	}
	
	public void onKeyUp(int keycode) {
		
	}

	public void destroy() {
		destroy = true;
		for (Actor a : widgets) {
			a.remove();
		}
		widgets.clear();
	}

	public boolean shouldDestroy() {
		return destroy;
	}
	
	public String getId() {
		return id;
	}

}
