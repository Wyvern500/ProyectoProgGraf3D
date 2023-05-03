package com.lecheros.display.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.parts.PartsSelectionPart;
import com.lecheros.display.widgets.JgTypedList;
import com.lecheros.display.widgets.JgTypedList.JgTypedListKey;
import com.lecheros.display.widgets.animation.KeyframeEntry;
import com.lecheros.model.JgModel;
import com.lecheros.model.ModelPart;

public class PartsSelectionDisplayHandler extends ADisplayHandler {

	PartsSelectionPart tree;
	JgTypedList<JgTypedListKey<ModelPart>> list;

	public PartsSelectionDisplayHandler(int zindex, Engine engine) {
		super(zindex);
		tree = new PartsSelectionPart(Gdx.graphics.getWidth() - 200, 60);
		
		list = new JgTypedList<>(Gdx.graphics.getWidth() - 200, 60, 180, 20, 10, (key, index) -> {
			engine.getModelProperties().getProperties().setX(key.getType().getTransform().getOffsetX());
			engine.getModelProperties().getProperties().setY(key.getType().getTransform().getOffsetY());
			engine.getModelProperties().getProperties().setZ(key.getType().getTransform().getOffsetZ());
			engine.getModelProperties().getProperties().setRX(key.getType().getTransform().getRotationX());
			engine.getModelProperties().getProperties().setRY(key.getType().getTransform().getRotationY());
			engine.getModelProperties().getProperties().setRZ(key.getType().getTransform().getRotationZ());
		});
		list.setOnDoubleClick((key, index) -> {
			engine.getAnimStuff().getKeyframeManager().addKeyframeEntry(
					new KeyframeEntry(key.getType(), engine));
		});
		addWidget(list);
	}

	public void initForModel(JgModel model) {
		for (ModelPart part : model.getModelParts()) {
			// Logger.getGlobal().info(part.getName());
			list.addKey(new JgTypedListKey<ModelPart>(part) {
				@Override
				public String getDisplayText() {
					return part.getName();
				}
			});
		}
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		tree.render(sr, font, sp);
		super.render(sr, font, sp);
		// list.render(sr, sp, font);
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		super.mouseDown(x, y, button);
		// list.mouseDown(x, y);
	}

	public void mouseDragged(int x, int y) {
		list.mouseDragged(x, y);
	}

	public void onScroll(float delta) {
		super.onScroll(delta);
		// list.onScroll(delta);
	}

	@Override
	public void onKeyDown(int key) {
		// Logger.getGlobal().info("key: " + key);
		if (Engine.keys[112]) {
			list.removeSelectedKeys();
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void onKeyUp(int keycode) {

	}

	public JgTypedList<JgTypedListKey<ModelPart>> getList() {
		return list;
	}

	@Override
	public void onNewModel(JgModel model) {
		list.clear();
		initForModel(model);
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

}
