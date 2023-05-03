package com.lecheros.display.widgets.animation;

import java.util.logging.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.lecheros.animation.Animation;
import com.lecheros.animation.Keyframe;
import com.lecheros.animator.Engine;
import com.lecheros.display.handlers.FocusHandler;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.Widget;
import com.lecheros.display.widgets.windows.TransformModifierWindow;
import com.lecheros.utils.WindowUtils;

public class TransformEntry extends Widget implements IKeyboardListener<Widget> {

	private Keyframe kf;
	private KeyframeEntry kfEntry;
	private int kfSize;
	private boolean selected;
	private boolean translation;
	
	public TransformEntry(KeyframeEntry entry, Keyframe kf, Animation anim, float x, 
			float maxX, float y, float w, float h, boolean translation) {
		super(x, y, w, h);
		this.pos.x = (int)(x
				+ (MathUtils.lerp(0, maxX - x, kf.startTick 
						/ (float) anim.getDuration())));
		this.kfEntry = entry;
		this.kf = kf;
		this.translation = translation;
		if(translation) {
			kf.translations.put(entry.getModelPart(), entry.getModelPart().getTransform()
					.getTranslation());
		} else {
			kf.rotations.put(entry.getModelPart(), entry.getModelPart().getTransform()
					.getRotation());
		}
		this.kfSize = 15;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
		pushColor(sr);
		Color c = translation ? Color.YELLOW : Color.GREEN;
		if(selected) {
			c = c.cpy().sub(0.3f, 0.3f, 0.3f, 0.0f);
		}
		Animation anim = kfEntry.getEngine().getAnimStuff().getAnimTime().getAnimation();
		if(anim.getKeyframes().get(kfEntry.getEngine().getAnimStuff().getAnimTime()
				.getAnimator().getCurrent()).startTick == kf.startTick) {
			c = Color.BLACK;
		}
		sr.setColor(c);
		sr.begin(ShapeType.Filled);
		sr.rect(pos.x, pos.y, kfSize, kfSize);
		sr.end();
		popColor(sr);
	}
	
	@Override
	public void onKeyDown(int keycode) {
		if(Engine.keys[112] && selected && FocusHandler.INSTANCE.getLastFocusedClass() 
				== getClass()) {
			remove();
			TransformModifierWindow<?> window = ((TransformModifierWindow<?>)kfEntry.getEngine()
					.getWindowHandler().getWindow(TransformModifierWindow.ID));
			if(window != null) {
				window.destroy();
			}
			Logger.getGlobal().info("Removing");
		}
	}

	@Override
	public void onKeyUp(int keycode) {
		
	}

	@Override
	public void dispose() {
		
	}
	
	private void onSelect() {
		WindowUtils.createTransformWindow(kfEntry.getEngine(), TransformEntry.this, 
				200, 400);
		TransformModifierWindow<?> window = ((TransformModifierWindow<?>)kfEntry.getEngine()
				.getWindowHandler().getWindow(TransformModifierWindow.ID));
		if(window != null) {
			window.set(kf, kfEntry.getModelPart(), translation);
		}
	}
	
	public void remove() {
		kfEntry.getEngine().getAnimStuff().getAnimTime().getAnimation().deleteKeyframe(kf);
		kfEntry.removeTransformEntry(kf, translation);
	}
	
	public void setTransform(float[] data) {
		if(translation) {
			kf.translations.put(kfEntry.getModelPart(), data);
		} else {
			kf.rotations.put(kfEntry.getModelPart(), data);
		}
	}

	public boolean isATranslationEntry() {
		return translation;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		if(selected) {
			onSelect();
		}
	}
	
	public int getKeyframeSize() {
		return kfSize;
	}
	
	public Keyframe getKeyframe() {
		return kf;
	}
	
}
