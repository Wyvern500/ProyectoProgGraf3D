package com.lecheros.display.parts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IDisplayPart {

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp);

}
