package com.lecheros.display.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;

public class ListButton<T> extends SelectBox<T> {

	private int alignment = Align.left;
	private String name;
	private boolean isOpen;

	public ListButton(Skin skin, String name) {
		super(skin);
		this.name = name;
	}

	/**
	 * Returns appropriate background drawable from the style based on the current
	 * select box state.
	 */
	protected @Null Drawable getBackgroundDrawable() {
		if (isDisabled() && getStyle().backgroundDisabled != null)
			return getStyle().backgroundDisabled;
		if (getScrollPane().hasParent() && getStyle().backgroundOpen != null)
			return getStyle().backgroundOpen;
		if (isOver() && getStyle().backgroundOver != null)
			return getStyle().backgroundOver;
		return getStyle().background;
	}

	@Override
	protected void onShow(Actor scrollPane, boolean below) {
		isOpen = true;
		super.onShow(scrollPane, below);
	}

	@Override
	protected void onHide(Actor scrollPane) {
		isOpen = false;
		super.onHide(scrollPane);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();

		Drawable background = getBackgroundDrawable();
		Color fontColor = getFontColor();
		BitmapFont font = getStyle().font;

		Color color = getColor();
		float x = getX(), y = getY();
		float width = getWidth(), height = getHeight();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		if (background != null)
			background.draw(batch, x, y, width, height);

		T selected = getSelection().first();
		if (selected != null) {
			if (background != null) {
				width -= background.getLeftWidth() + background.getRightWidth();
				height -= background.getBottomHeight() + background.getTopHeight();
				x += background.getLeftWidth();
				y += (int) (height / 2 + background.getBottomHeight() + font.getData().capHeight / 2);
			} else {
				y += (int) (height / 2 + font.getData().capHeight / 2);
			}
			font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
			// drawItem(batch, font, selected, x, y, width);
			font.draw(batch, name, x, y, 0, name.length(), width, alignment, false, "...");
		}
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (!getDebug())
			return;
		shapes.set(ShapeType.Line);
		if (getStage() != null)
			shapes.setColor(getStage().getDebugColor());
		shapes.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}

	@Override
	protected void drawDebugBounds(ShapeRenderer shapes) {
		if (!getDebug())
			return;
		shapes.set(ShapeType.Line);
		if (getStage() != null)
			shapes.setColor(getStage().getDebugColor());
		shapes.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}

	@Override
	protected GlyphLayout drawItem(Batch batch, BitmapFont font, T item, float x, float y, float width) {
		String string = toString(item);
		return font.draw(batch, string, x, y, 0, string.length(), width, alignment, false, "...");
	}

	public boolean isOpen() {
		return isOpen;
	}

}
