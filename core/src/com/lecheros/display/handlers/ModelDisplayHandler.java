package com.lecheros.display.handlers;

import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.model.JgModel;

public class ModelDisplayHandler extends ADisplayHandler {

	private JgModel model;
	private PerspectiveCamera cam;
	private FitViewport modelViewport;
	private int angle;

	public ModelDisplayHandler(int zindex, float viewPortWidth, float viewPortHeight, Engine engine) {
		super(zindex);
		// model = new JgModel("simpleentity.obj", "mob");
		// Logger.getGlobal().info("Model: \n" +
		// ModelSerializer.serialize(ModelSerializer.deserialize(ModelSerializer.serialize(model))));
		cam = new PerspectiveCamera(67, viewPortWidth, viewPortHeight);// Gdx.graphics.getWidth(),
																		// Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		modelViewport = new FitViewport(viewPortWidth, viewPortHeight, cam);
		modelViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void tick() {
		if (model != null) {
			model.tick();
			// How to move the model
			/*
			angle++;
			model.getModel().meshParts.get(0).mesh
					.transform(new Matrix4().idt().translate((float) Math.cos(angle) * 0.1f, 0, 0));
			model.getModel().meshParts.get(1).mesh
					.transform(new Matrix4().idt().translate((float) Math.sin(angle) * 0.1f, 0, 0));
			*/
			/*for() {
				
			}*/
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		// Render model
		if (model != null) {
			modelViewport.apply();
			model.render(cam);
		}
	}

	public void dispose() {
		if (model != null) {
			model.dispose();
		}
	}

	public JgModel getModel() {
		return model;
	}

	@Override
	public void onNewModel(JgModel model) {
		this.model = model;
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

}
