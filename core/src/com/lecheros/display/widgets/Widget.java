package com.lecheros.display.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Widget {

	protected float x;
	protected float y;
	protected float w;
	protected float h;
	
	protected BitmapFont font;
	protected GlyphLayout layout;
	
	protected Color border;
	protected Color background;
	
	protected boolean mouseDown;
	protected boolean focused;
	
	public Widget(float x, float y, float w, float h, BitmapFont font) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.font = font;
		this.layout = new GlyphLayout();
		this.border = Color.BLACK;
		this.background = Color.GRAY;
	}
	
	// Getters and setters
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public GlyphLayout getLayout() {
		return layout;
	}

	public void setLayout(GlyphLayout layout) {
		this.layout = layout;
	}

	public boolean isMouseDown() {
		return mouseDown;
	}

	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public abstract void tick();
	
	public abstract void render(ShapeRenderer sr, SpriteBatch batch);
	
	public abstract void clickDown(int x, int y, int button);
	
	public abstract void clickUp(int x, int y, int button);
	
	public abstract void dispose();
	
}
