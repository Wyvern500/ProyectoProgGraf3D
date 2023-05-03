package com.lecheros.display.handlers;

import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lecheros.animation.Animation;
import com.lecheros.animation.Keyframe;
import com.lecheros.animation.serializer.AnimationSerializer;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.widgets.JgTypedList;
import com.lecheros.display.widgets.JgTypedList.JgTypedListKey;
import com.lecheros.display.widgets.JgTypedList.OnSelectKey;
import com.lecheros.display.widgets.animation.AnimationTimeline;
import com.lecheros.display.widgets.windows.AnimationModifierWindow;
import com.lecheros.display.widgets.windows.AnimationsWindow;
import com.lecheros.model.JgModel;
import com.lecheros.utils.FileUtils;
import com.lecheros.utils.SerializerUtils;

public class AnimationsDisplayerDisplayHandler extends ADisplayHandler {

	private JgTypedList<JgTypedListKey<Animation>> list;
	private Engine engine;
	
	public AnimationsDisplayerDisplayHandler(int zindex, Engine engine) {
		super(zindex);
		this.engine = engine;
		list = new JgTypedList<JgTypedListKey<Animation>>(0, 62, 155, 20, 9, 
				(key, index) -> {
					if (key != null) {
						FocusHandler.INSTANCE.setLastFocus(getClass());
						engine.getAnimStuff().getAnimTime().setAnimation(key.getType());
					}
				});
		list.setOnRightClick((key) -> {
			engine.getWindowHandler()
			.addWindow(engine, new AnimationModifierWindow(this, key.getType(), 200, 500, 300, 
					180));
			Logger.getGlobal().info("Creating animation");
		});
		String[] animations = new String[] { "Test", "sds", "sdsd", "Test", "sds", "sdsd", 
				"Test", "sds", "sdsd", "Test", "sds", "sdsd", "Test", "sds", "sdsd", "Test", 
				"sds", "sdsd" };
		for (String s : animations) {
			list.addKey(new JgTypedListKey<Animation>(new Animation(s)) {
				@Override
				public String getDisplayText() {
					return this.getType().name;
				}
			});
		}

		addWidget(list);

		// list.addTestAnimations(engine);

		// Create Animation button

		TextButton createAnimation = new TextButton("Create Animation", engine.getSkin());
		createAnimation.setPosition(14, 290);
		engine.getButtons().add(createAnimation);
		createAnimation.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				engine.getWindowHandler()
						.addWindow(engine, new AnimationsWindow(200, 500, 300, 180, 
								AnimationsDisplayerDisplayHandler.this));
				Logger.getGlobal().info("Creating animation");
			}
		});
		engine.addActor(createAnimation);

		// Import animation button

		TextButton importAnimation = new TextButton("Import Animation", engine.getSkin());
		importAnimation.setPosition(14, 328);
		engine.getButtons().add(importAnimation);
		importAnimation.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				Logger.getGlobal().info("Importing animation");

				new Thread(new Runnable() {
					@Override
					public void run() {
						JFileChooser chooser = new JFileChooser();
						JFrame f = new JFrame();
						f.setVisible(true);
						f.toFront();
						f.setVisible(false);
						int res = chooser.showOpenDialog(f);
						f.dispose();
						if (res == JFileChooser.APPROVE_OPTION) {
							String data = FileUtils.readFile(chooser.getSelectedFile());
							/*
							 * Aqui debo compararlo con el modelo actual si quiero importar la animacion
							 * porque si no las partes no van a estar coordinadas y va a dar un error.
							 */
							JgModel model = engine.getModelDisplay().getModel();
							if (!SerializerUtils.getModelName(data).equals(model.getName()))
								return;
							Animation anim = AnimationSerializer.deserialize(model, data);
							list.addKey(new JgTypedListKey<Animation>(anim) {
								@Override
								public String getDisplayText() {
									return getType().name;
								}
							});
							engine.getAnimationsHandler().addAnimation(anim);
							AnimationTimeline animLine = engine.getAnimStuff().getAnimTime();
							animLine.setModel(model);
							animLine.setAnimation(anim);
							animLine.setKeyframes(anim.getKeyframes(), anim.getDuration());
							Logger.getGlobal()
									.info("Animation name: " + anim.name + " keyframes size: " + anim.getKeyframes().size());
							for (Keyframe kf : anim.getKeyframes()) {
								Logger.getGlobal().info("Keyframe startTick: " + kf.startTick
										+ " Keyframe startVisualTick: " + kf.startVisualTick);
							}
						}
					}
				}).start();
				/*
				 * JFileChooser jfc = new JFileChooser();
				 * 
				 * Logger.getGlobal().info("Before open");
				 * 
				 * int result = jfc.showOpenDialog(null);
				 * 
				 * Logger.getGlobal().info("After open");
				 * 
				 * if(result == JFileChooser.APPROVE_OPTION) { String data =
				 * FileUtils.readFile(jfc.getSelectedFile()); Animation anim =
				 * AnimationSerializer.deserialize(ModelDisplayHandler .model, data);
				 * Logger.getGlobal().info("Animation name: " + anim.name); }
				 */
			}
		});
		engine.addActor(importAnimation);

		/*
		 * "Test", "sds", "sdsd", "Test", "sds", "sdsd", "Test", "sds", "sdsd", "Test",
		 * "sds", "sdsd" , "Test", "sds", "sdsd", "Test", "sds", "sdsd"
		 */
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color color = sr.getColor();
		sr.setColor(0.2f, 0.2f, 0.2f, 1.0f);
		sr.begin(ShapeType.Filled);
		sr.rect(0, 0, 175, Gdx.graphics.getHeight());
		sr.end();
		sr.setColor(0.15f, 0.15f, 0.15f, 1.0f);
		sr.begin(ShapeType.Filled);
		sr.rect(0, 30, 175, 32);
		sr.end();
		sr.setColor(color);
		sp.begin();
		font.draw(sp, "Animations", 40, 40);
		sp.end();
		super.render(sr, font, sp);
		// list.render(sr, sp, font);
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
		if (Engine.keys[112] && FocusHandler.INSTANCE.getLastFocusedClass() == getClass()) {
			list.removeSelectedKeys();
		}
	}

	@Override
	public void onKeyUp(int keycode) {

	}

	@Override
	public void dispose() {

	}

	// Getters and setters

	public JgTypedList<JgTypedListKey<Animation>> getList() {
		return list;
	}

	@Override
	public void onNewModel(JgModel model) {
		list.clear();
		for(Animation anim : model.getAnimations()) {
			engine.getAnimDisplay().getList().addKey(new JgTypedListKey<Animation>(anim) {
				@Override
				public String getDisplayText() {
					return getType().name;
				}
			});
		}
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

}
