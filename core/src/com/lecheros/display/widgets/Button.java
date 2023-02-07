package com.lecheros.display.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Button extends Widget {

	private boolean pressed;
	private String text;
	private ToDo toDo;
	
	public Button(float x, float y, float w, float h, String text, BitmapFont font, ToDo toDo) {
		super(x, y, w, h, font);
		this.text = text;
		this.layout.setText(font, text);
		this.toDo = toDo;
		if(layout.width > w) {
			this.w = layout.width;
		}
	}
	
	@Override
	public void tick() {

	}
	
	@Override
	public void clickDown(int x, int y, int button) {
		if(x > this.x && x < this.x + this.w &&
				   y > this.y && y < this.y + this.h) {
			pressed = true;
			focused = true;
			toDo.toDo();
		}
	}
	
	@Override
	public void clickUp(int x, int y, int button) {
		pressed = false;
		focused = false;
	}
	
	@Override
	public void render(ShapeRenderer sr, SpriteBatch batch) {
		Color old = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(pressed ? background.r - 0.1f : background.r, background.g, 
				background.b, background.a);
		sr.rect(x, y, w, h);
		sr.end();
		sr.begin(ShapeType.Line);
		sr.setColor(border.r, border.g, border.b, border.a);
		sr.rect(x, y, w, h);
		sr.end();
		sr.setColor(old);
		batch.begin();
		font.draw(batch, text, x-2, y+2);
		batch.end();
	}
	
	@Override
	public void dispose() {
		
	}
	
	public void setText(String text) {
		this.text = text;
		this.layout.setText(font, text);
		if(layout.width > w) {
			this.w = layout.width+2;
		} else {
			this.w = layout.width+2;
		}
	}
	
	public Button setBorderColor(Color color) {
		this.border = color;
		return this;
	}
	
	public Button setBackgroundColor(Color color) {
		this.background = color;
		return this;
	}
	
	public interface ToDo {
		public void toDo();
	}
	
}
