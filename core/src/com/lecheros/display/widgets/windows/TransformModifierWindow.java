package com.lecheros.display.widgets.windows;

import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.lecheros.animation.Keyframe;
import com.lecheros.display.handlers.FocusHandler;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.JgTextField;
import com.lecheros.display.widgets.WindowWidget;
import com.lecheros.display.widgets.animation.TransformEntry;
import com.lecheros.model.ModelPart;

public class TransformModifierWindow<T> extends WindowWidget<T> {

	public static final String ID = "TransformModifierWindow";

	public static final int TX = 8;
	public static final int TY = 9;
	public static final int TZ = 10;
	
	private int current;
	private Keyframe keyframe;
	private ModelPart part;
	private boolean isTranslationType;
	
	JgTextField[] fields;
	Label[] labels;
	
	public TransformModifierWindow(T parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h, ID);
		fields = new JgTextField[3];
		labels = new Label[3];
		this.current = 0;
		FocusHandler.INSTANCE.setLastFocus(getClass());
	}

	@Override
	public void initWidgets() {
		
		int startLX = 10;
		int startLY = 90;

		// Translation
		
		labels[0] = new Label("X", engine.getSkin());
		labels[1] = new Label("Y", engine.getSkin());
		labels[2] = new Label("Z", engine.getSkin());
		
		fields[0] = 
				new JgTextField(TransformModifierWindow.TX, "0", engine.getSkin());
		fields[1] = 
				new JgTextField(TransformModifierWindow.TY, "0", engine.getSkin());
		fields[2] = 
				new JgTextField(TransformModifierWindow.TZ, "0", engine.getSkin());
		
		// Roatation
		
		for(int i = 0; i < labels.length; i++) {
			setWPos(labels[i], startLX, startLY + 4 + (i * 30));
		}
		
		for(int i = 0; i < fields.length; i++) {
			fields[i].setFocusTraversal(false);
			setWPos(fields[i], startLX + 30, startLY + (i * 30));
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		super.render(sr, font, sp);
		float offsetX = 160;
		String part = this.part == null ? "" : this.part.getName();
		String keyframeTick = this.keyframe == null ? "" : 
			String.valueOf(this.keyframe.startTick);
		sp.begin();
		font.draw(sp, "Transform Modifier Window", pos.x + 10, pos.y + 2);
		font.draw(sp, "Model Part: " + part, pos.x + 200 - offsetX, pos.y + 30);
		font.draw(sp, "Transform Type: " + isTranslationType, pos.x + 164 - offsetX, pos.y + 50);
		font.draw(sp, "Keyframe Tick: " + keyframeTick, pos.x + 177 - offsetX, pos.y + 70);
		sp.end();
	}

	public void set(Keyframe keyframe, ModelPart part, boolean isTranslation) {
		this.keyframe = keyframe;
		this.part = part;
		this.isTranslationType = isTranslation;
		
		if(isTranslation) {
			if(keyframe.translations.containsKey(part)) {
				float[] translation = keyframe.translations.get(part);
				for(int i = 0; i < 3; i++) {
					JgTextField f = fields[i];
					f.setText(String.valueOf(translation[i]));
				}
			}
		} else {
			if(keyframe.rotations.containsKey(part)) {
				float[] rotation = keyframe.rotations.get(part);
				for(int i = 0; i < 3; i++) {
					JgTextField f = fields[i];
					f.setText(String.valueOf(rotation[i]));
				}
			}
		}
	}
	
	@Override
	public void onKeyDown(int keycode) {
		Logger.getGlobal().info("Focus: " + (FocusHandler.INSTANCE.getLastFocusedClass() 
				== getClass()) + " Focused Class: " + FocusHandler.INSTANCE.getLastFocusedClass());
		if(keycode == Input.Keys.TAB && FocusHandler.INSTANCE.getLastFocusedClass() 
				== getClass()) {
			engine.setKeyboardFocus(nextField());
		}
	}
	
	private Actor nextField() {
		current++;
		if(current > fields.length-1) {
			current = 0;
		}
		return fields[current];
	}

	public void onEventFired(Event e) {
		Logger.getGlobal().info("onEventFired");
		if(this.keyframe == null || this.part == null) return;
		if(e instanceof ChangeEvent) {
			if(e.getTarget() instanceof JgTextField) {
				Logger.getGlobal().info("JgTextField");
				JgTextField field = (JgTextField) e.getTarget();
				String text = field.getText();
				float val = 0;
				if(text.matches("[0-9.]+")) {
					val = Float.parseFloat(text);
				} else {
					val = 0.0f;
				}
				switch (field.getId()) {
				case TX:
					this.current = 0;
					if(isTranslationType) {
						keyframe.translations.get(part)[0] = val;
					} else {
						keyframe.rotations.get(part)[0] = val;
					}
					break;
				case TY:
					this.current = 1;
					if(isTranslationType) {
						keyframe.translations.get(part)[1] = val;
					} else {
						keyframe.rotations.get(part)[1] = val;
					}
					break;
				case TZ:
					this.current = 2;
					if(isTranslationType) {
						keyframe.translations.get(part)[2] = val;
					} else {
						keyframe.rotations.get(part)[2] = val;
					}
					break;
				}
			}
			System.out.println("sdsd");
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		FocusHandler.INSTANCE.setLastFocus(TransformEntry.class);
	}
	
}
