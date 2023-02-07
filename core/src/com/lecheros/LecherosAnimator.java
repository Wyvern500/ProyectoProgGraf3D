package com.lecheros;

import java.io.File;
import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lecheros.animator.Engine;
import com.lecheros.display.DisplayUtils;
import com.lecheros.display.widgets.Button;
import com.lecheros.meshes.JGMesh;

public class LecherosAnimator extends ApplicationAdapter {

	Texture img;
	
	float y;
	float angle;
	int other;
	
	Button btn;
	
	Engine engine;
	
	@Override
	public void create () {
		
		
	
		//img = new Texture("badlogic.jpg");

		y = 5f;
		/*modelBuilder.createBox(5f, y, 4f, 
			new Material(ColorAttribute.createDiffuse(Color.GREEN)), 
			Usage.Position | Usage.Normal);*/
		
		
		engine = new Engine();
		
	}

	@Override
	public void render () {
		/*ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
		
		//Gdx.gl.glViewport((int)viewPortX, (int)viewPortY, (int)viewPortWidth, (int)viewPortHeight);//Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		engine.tick();
		engine.render();
		
		/*
		modelViewport.setScreenBounds(0, 0, width, height);
		uiViewport.setScreenBounds(0, 0, width, height);
		
		modelViewport.apply();
		
		uiViewport.apply();
		
		btn.render(sr, batch);
		*/
		
		/*batch.begin();
		font.draw(batch, "Hola como estas?", other, 20);
		batch.end();*/
		
		//model.meshParts.get(1).mesh.transform(new Matrix4().idt()
		//		.translate((float)Math.cos(angle)*0.1f, 0, 0));
		
		//other += 1;
		
	}
	
	@Override
	public void resize(int width, int height) {
		//super.resize(width, height);
	}
	
	@Override
	public void dispose () {
		//img.dispose();
		engine.dispose();
	}

}
