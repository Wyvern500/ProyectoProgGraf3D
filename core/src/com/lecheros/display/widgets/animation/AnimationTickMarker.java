package com.lecheros.display.widgets.animation;

import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lecheros.display.widgets.IMouseListener;
import com.lecheros.display.widgets.Widget;
import com.lecheros.utils.Utils;

public class AnimationTickMarker extends Widget implements IMouseListener<AnimationTickMarker> {

	private float minX;
	private float maxX;
	private Vector2 mouse;
	private AnimationTimeline handler;
	
	public AnimationTickMarker(AnimationTimeline handler, float x, float y, float w, float h, float minX, float maxX) {
		super(x, y, w, h);
		this.handler = handler;
		this.minX = minX;
		this.maxX = maxX;
		this.mouse = new Vector2();
	}

	@Override
	public void tick() {
		/*if(handler.getAnimation() != null) {
			float prog = MathUtils.lerp(0, handler.getAnimation().getDuration(), 
					getProgress());
			handler.getAnimStuff().getField().setText(
					String.valueOf(Math.floor(prog)));
		}*/
		if(handler.getAnimation() != null) {
		//	System.out.println("Marker raw tick: " + MathUtils.lerp(0, handler.getAnimation().getDuration(), 
		//			getProgress()));
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
		boolean renderWindow = false;
		if(Utils.collidesWithMouse(Gdx.input.getX(), Gdx.input.getY(), pos, 20, 16)) {
			renderWindow = true;
		}
		
		float w = 20;
		float h = 16;
		pushColor(sr);
		sr.setColor(new Color(0.8f, 0.0f, 0.0f, 1.0f));
		sr.begin(ShapeType.Filled);
		sr.rect(pos.x, pos.y, w, h);
		sr.rect(pos.x, pos.y, 2, this.h);
		if(renderWindow) {
			sr.setColor(new Color(0.13f, 0.13f, 0.13f, 1.0f));
			sr.rect(Gdx.input.getX() - 100, Gdx.input.getY(), 100, 20);
		}
		sr.end();
		popColor(sr);
		
		if(renderWindow) {
			batch.begin();
			font.draw(batch, "Tick: " + getTick(), Gdx.input.getX() + 8 - 100, 
					Gdx.input.getY() + 2);
			batch.end();
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		
	}

	@Override
	public void mouseDragged(int x, int y) {
		float h = 16;
		
		if(x > minX && x < minX + this.w &&
		   y > pos.y && y < pos.y + h) {
			pos.x = clamp(x, minX, maxX);
		}
	}

	@Override
	public void mouseScroll(float delta) {
		
	}
	
	@Override
	public void mouseMoved(int x, int y) {
		mouse.set(x, y);
	}
	
	public float getProgress() {
		return ((pos.x - minX) / (maxX - minX - 1));
	}
	
	public int getTick() {
		if(handler.getAnimation() != null) {
			float tick = MathUtils.lerp(0, handler.getAnimation().getDuration(), getProgress());
			return (int)Math.floor(tick);
		}
		return 0;
	}
	
	private float clamp(float val, float min, float max) {
		if(val <= min) {
			return min;
		} else if(val >= max) {
			return max;
		}
		return val;
	}
	
}
