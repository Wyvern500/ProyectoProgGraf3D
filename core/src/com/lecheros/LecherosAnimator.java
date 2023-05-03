package com.lecheros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.lecheros.animator.Engine;

public class LecherosAnimator extends ApplicationAdapter {

	Texture img;

	float y;
	float angle;
	int other;

	Engine engine;

	@Override
	public void create() {

		// img = new Texture("badlogic.jpg");

		y = 5f;
		/*
		 * modelBuilder.createBox(5f, y, 4f, new
		 * Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position |
		 * Usage.Normal);
		 */

		engine = new Engine();

	}

	@Override
	public void render() {
		/*
		 * ScreenUtils.clear(1, 0, 0, 1); batch.begin(); batch.draw(img, 0, 0);
		 * batch.end();
		 */

		// Gdx.gl.glViewport((int)viewPortX, (int)viewPortY, (int)viewPortWidth,
		// (int)viewPortHeight);//Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		engine.tick();
		engine.render();

		/*
		 * modelViewport.setScreenBounds(0, 0, width, height);
		 * uiViewport.setScreenBounds(0, 0, width, height);
		 * 
		 * modelViewport.apply();
		 * 
		 * uiViewport.apply();
		 * 
		 * btn.render(sr, batch);
		 */

		/*
		 * batch.begin(); font.draw(batch, "Hola como estas?", other, 20); batch.end();
		 */

		// model.meshParts.get(1).mesh.transform(new Matrix4().idt()
		// .translate((float)Math.cos(angle)*0.1f, 0, 0));

		// other += 1;

	}

	@Override
	public void resize(int width, int height) {
		// super.resize(width, height);
	}

	@Override
	public void dispose() {
		// img.dispose();
		engine.dispose();
	}

}
