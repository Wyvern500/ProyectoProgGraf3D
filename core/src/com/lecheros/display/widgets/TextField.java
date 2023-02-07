package com.lecheros.display.widgets;

import java.util.logging.Logger;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lecheros.animator.Engine;
import com.lecheros.utils.Utils;

public class TextField extends Widget implements IKeyboardListener {

	private String text;
	
	private int from;
	private int to;
	private int offset;
	private int toOffset;
	
	private int current;
	private int timer;
	private int timeStep;
	private boolean back;
	
	public TextField(float x, float y, float w, float h, BitmapFont font) {
		super(x, y, w, h, font);
		this.text = "Hola";
		timeStep = 30;
		findLimit();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(ShapeRenderer sr, SpriteBatch batch) {
		Color old = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(background.r, background.g, 
				background.b, background.a);
		sr.rect(x, y, w, h);
		sr.end();
		sr.begin(ShapeType.Line);
		sr.setColor(border.r, border.g, border.b, border.a);
		sr.rect(x, y, w, h);
		if(true) {
			if(timer < timeStep && !back) {
				timer++;
				GlyphLayout l = new GlyphLayout();
				l.setText(font, text.substring(0, Math.max(0, current-offset)));
				sr.rect(x+l.width+2, y-2, 2, h+4);
			} else {
				back = true;
			}
			if(back){
				if(timer > 0) {
					timer--;
				} else {
					back = false;
				}
			}
		}
		sr.end();
		sr.setColor(old);
		batch.begin();
		font.draw(batch, text.substring(from+offset, to+offset+toOffset), x-2, y+2);
		batch.end();
	}
	
	public void findLimit() {
		String temp = "";
		GlyphLayout l = new GlyphLayout();
		this.to = text.length();
		for(int i = 0; i < text.length(); i++) {
			temp += text.charAt(i);
			l.setText(font, temp);
			if(l.width > w) {
				this.to = i;
				this.offset++;
				System.out.println(i);
				break;
			}
		}
	}
	
	@Override
	public void clickDown(int x, int y, int button) {
		if(x > this.x && x < this.x + this.w &&
		   y > this.y && y < this.y + this.h) {
			focused = true;
		}
	}
	
	@Override
	public void clickUp(int x, int y, int button) {
		if(x < this.x && x > this.x + this.w &&
		   y < this.y && y > this.y + this.h) {
			focused = false;
		}
	}

	@Override
	public void keyDown(int keycode) {
		String key = Keys.toString(keycode);
		if(!Engine.keys[Keys.SHIFT_LEFT]) {
			//if(key.matches("[a-zA-Z0-9]+")) {
			if(key.length() == 1) {
				text += key.toLowerCase();
				current++;
				findLimit();
			}
		} else {
			if(key.length() == 1) {
				text += key;
				current++;
				findLimit();
			}
		}
		if(keycode == Keys.LEFT) {
			current = Math.max(0, current-1);
		} else if(keycode == Keys.RIGHT) {
			current = Math.min(text.length()-1, current+1);
		}
		if(keycode == Keys.DEL) {
			if(text.length() > 0) {
				if(offset > 0) {
					if((to+offset) - (from+offset) <= 5) {
						this.offset = Math.max(0, offset-1);
						this.toOffset++;
					}
					this.to = Math.max(0, to-1);
				} else {
					this.to = Math.max(0, to-1);
				}
				Logger.getGlobal().info("offset: " + offset + " from: " + from 
						+ " to: " + to + " from+offset: " + (from+offset) + " to+offset: " + 
						(to+offset));
				text = Utils.remove(text, current);
				//text = text.substring(0, text.length()-1);
			}
		}
		
	}

	@Override
	public void keyUp(int keycode) {
		
	}
	
	@Override
	public void dispose() {
		
	}

}
