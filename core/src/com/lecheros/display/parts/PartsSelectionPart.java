package com.lecheros.display.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lecheros.display.widgets.JgTree.OnClickOnNode;

public class PartsSelectionPart implements IDisplayPart {

	private int w;
	private int h;
	private Vector2 pos;

	public PartsSelectionPart(int x, int y) {
		this.pos = new Vector2(x, y);
		this.w = 200;
		this.h = 400;
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color c = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(0.2f, 0.2f, 0.2f, 1.0f);
		sr.rect(pos.x, pos.y, w, h);
		sr.end();
		sr.setColor(c);
		sr.begin(ShapeType.Filled);
		sr.setColor(0.15f, 0.15f, 0.15f, 1.0f);
		sr.rect(Gdx.graphics.getWidth() - 200, 30, w, 210);
		sr.end();
		sr.setColor(c);
		sp.begin();
		font.draw(sp, "Model Parts", Gdx.graphics.getWidth() - 150, 40);
		sp.end();
	}

	public void setOnNodeClick(OnClickOnNode onClick) {

	}

}
