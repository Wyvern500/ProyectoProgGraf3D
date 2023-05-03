package com.lecheros.display.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.model.JgModel;

public class BackgroundDisplayHandler extends ADisplayHandler {

	public BackgroundDisplayHandler(int zindex) {
		super(zindex);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color color = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(0.6f, 0.6f, 0.6f, 1.0f);
		sr.rect(175, 32, 625, 400);
		sr.end();
		sr.setColor(color);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void onNewModel(JgModel model) {

	}

	@Override
	public void onSaveModel(JgModel model) {

	}

}
