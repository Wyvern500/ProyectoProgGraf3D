package com.lecheros.display.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class Widget {

	protected Vector2 pos;
	protected float w;
	protected float h;

	protected GlyphLayout layout;

	protected Color renderColor;
	protected Color border;
	protected Color background;

	protected boolean mouseDown;
	protected boolean focused;

	public Widget(float x, float y, float w, float h) {
		this.pos = new Vector2(x, y);
		this.w = w;
		this.h = h;
		this.layout = new GlyphLayout();
		this.border = Color.BLACK;
		this.background = Color.GRAY;
	}

	public void keyDown(int key) {

	}

	public void keyUp(int key) {

	}

	protected void pushColor(ShapeRenderer sr) {
		renderColor = sr.getColor();
	}

	protected void popColor(ShapeRenderer sr) {
		if (renderColor != null) {
			sr.setColor(renderColor);
		}
	}

	protected void enableTransparency() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	protected void disableTransparency() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	// Getters and setters

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(float x, float y) {
		this.pos.set(x, y);
	}
	
	public float getX() {
		return pos.x;
	}

	public void setX(float x) {
		this.pos.x = x;
	}

	public float getY() {
		return pos.y;
	}

	public void setY(float y) {
		this.pos.y = y;
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

	public abstract void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch);

	public abstract void dispose();

}
