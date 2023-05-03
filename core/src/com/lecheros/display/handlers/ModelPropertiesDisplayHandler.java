package com.lecheros.display.handlers;

import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.handler.IDisplayHandlerClickListener;
import com.lecheros.display.handler.IDisplayHandlerEventListener;
import com.lecheros.display.parts.PropertiesModifierPart;
import com.lecheros.display.widgets.JgTextField;
import com.lecheros.model.JgModel;

public class ModelPropertiesDisplayHandler extends ADisplayHandler
		implements IDisplayHandlerEventListener<ModelPropertiesDisplayHandler>, 
				   IDisplayHandlerClickListener<ModelPropertiesDisplayHandler> {

	public static final int TX = 0;
	public static final int TY = 1;
	public static final int TZ = 2;
	public static final int RX = 3;
	public static final int RY = 4;
	public static final int RZ = 5;

	private Vector2 pos;
	private PropertiesModifierPart properties;
	private Engine engine;
	private int current;

	public ModelPropertiesDisplayHandler(int zindex, Engine engine) {
		super(zindex);
		pos = new Vector2(Gdx.graphics.getWidth() - 200, 0);
		properties = new PropertiesModifierPart(engine, pos.x + 114, 70);
		this.engine = engine;
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color color = sr.getColor();
		sr.begin(ShapeType.Filled);
		sr.setColor(0.15f, 0.15f, 0.15f, 1.0f);
		sr.rect(pos.x, pos.y + Gdx.graphics.getHeight() - 340, 200, 340); // y = 168
		sr.setColor(0.1f, 0.1f, 0.1f, 1.0f);
		sr.rect(Gdx.graphics.getWidth() - 200, pos.y + 280, 210, 330);
		sr.setColor(0.30f, 0.30f, 0.30f, 1.0f);
		sr.rect(Gdx.graphics.getWidth() - 200, pos.y + 418, 18, 16);
		sr.end();
		sr.setColor(color);
		sp.begin();
		font.draw(sp, "Model properties", pos.x + 30, pos.y + Gdx.graphics.getHeight() - 300);
		sp.end();
	}

	@Override
	public void onEventFired(Event e) {
		if (e instanceof ChangeEvent) {
			if(engine.getPartsSelection().getList().getSelectedKey() == null) return;
			if (e.getTarget() instanceof JgTextField) {
				JgTextField field = (JgTextField) e.getTarget();
				String text = field.getText();
				float val = 0;
				if(text.matches("[0-9.]+")) {
					val = Float.parseFloat(text);
				} else {
					val = 0.0f;
				}
				Logger.getGlobal().info("ChangeEvent");
				switch (field.getId()) {
				case TX:
					current = TX;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setOffsetX(val);
					break;
				case TY:
					current = TY;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setOffsetY(val);
					break;
				case TZ:
					current = TZ;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setOffsetZ(val);
					break;
				case RX:
					current = RX;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setRotationX(val);
					break;
				case RY:
					current = RY;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setRotationY(val);
					break;
				case RZ:
					current = RZ;
					engine.getPartsSelection().getList().getSelectedKey().getType()
						.getTransform().setRotationZ(val);
					break;
				}
			}
		}
	}
	
	@Override
	public void onKeyDown(int key) {
		super.onKeyDown(key);
		//Logger.getGlobal().info(FocusHandler.INSTANCE.getLastFocusedClass().toString());
		if(key == Input.Keys.TAB && FocusHandler.INSTANCE.getLastFocusedClass() == getClass()) {
			engine.setKeyboardFocus(nextField());
		}
	}
	
	@Override
	public void handleClick(InputEvent e, float x, float y) {
		if(e.getButton() == Input.Buttons.LEFT) {
			if(e.getTarget() instanceof JgTextField) {
				JgTextField field = ((JgTextField)e.getTarget());
				if(field.getId() >= 0 && field.getId() <= 5) {
					current = field.getId();
					FocusHandler.INSTANCE.setLastFocus(getClass());
				}
			}
		}
	}
	
	private Actor nextField() {
		current++;
		if(current > properties.getFields().length-1) {
			current = 0;
		}
		return properties.getFields()[current];
	}

	@Override
	public void dispose() {

	}

	@Override
	public void onNewModel(JgModel model) {
		properties.clear();
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

	public PropertiesModifierPart getProperties() {
		return properties;
	}
	
}
