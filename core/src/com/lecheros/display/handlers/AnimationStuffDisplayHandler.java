package com.lecheros.display.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.handler.IDisplayHandlerClickListener;
import com.lecheros.display.handler.IDisplayHandlerEventListener;
import com.lecheros.display.widgets.AbstractJgButton;
import com.lecheros.display.widgets.JgTextField;
import com.lecheros.display.widgets.animation.AnimationTimeline;
import com.lecheros.display.widgets.animation.KeyframeManagerWidget;
import com.lecheros.model.JgModel;

public class AnimationStuffDisplayHandler extends ADisplayHandler implements IDisplayHandlerEventListener<AnimationStuffDisplayHandler> {

	private int w;
	private int h;
	private int current;
	private AnimationTimeline animTime;
	private KeyframeManagerWidget keyframeMan;
	private JgTextField animTick;
	private AbstractJgButton playBtn;
	private AbstractJgButton stopBtn;
	private Engine engine;
	
	public AnimationStuffDisplayHandler(int zindex, Engine engine) {
		super(zindex);
		this.w = 625;
		this.h = 180;
		animTime = new AnimationTimeline(175, Gdx.graphics.getHeight() - 182, w, h, this);// x = 175

		this.engine = engine;
		this.animTick = new JgTextField(6, "0.0", engine.getSkin());
		animTick.setOnChange((tf, txt) -> {
			// Logger.getGlobal().info("Text: " + txt);
		});
		animTick.setPosition(0, 180);
		animTick.setSize(60, 30);
		engine.getFields().add(animTick);
		engine.addActor(animTick);

		addWidget(animTime);

		playBtn = new AbstractJgButton(65, Gdx.graphics.getHeight() - 210, 40, 30) {
			@Override
			public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
				Color c = new Color(0.15f, 0.15f, 0.15f, 1.0f);
				if(pressed) {
					c.set(0.18f, 0.18f, 0.18f, 1.0f);
				}
				pushColor(sr);
				sr.begin(ShapeType.Filled);
				sr.setColor(c);
				sr.rect(pos.x, pos.y, w, h);
				sr.setColor(Color.WHITE);
				triangle(sr, pos.x + 15, pos.y + 6, 16);
				sr.end();
				popColor(sr);
			}
		}.setOnClick((btn) -> {
			animTime.getAnimator().setPlay(true);
		});
		
		stopBtn = new AbstractJgButton(105, Gdx.graphics.getHeight() - 210, 40, 30) {
			@Override
			public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
				Color c = new Color(0.15f, 0.15f, 0.15f, 1.0f);
				if(pressed) {
					c.set(0.18f, 0.18f, 0.18f, 1.0f);
				}
				pushColor(sr);
				sr.begin(ShapeType.Filled);
				sr.setColor(c);
				sr.rect(pos.x, pos.y, w, h);
				sr.setColor(Color.WHITE);
				sr.rect(pos.x + 12, pos.y + 6, 16, 16);
				sr.end();
				popColor(sr);
			}
		}.setOnClick((btn) -> {
			animTime.getAnimator().setPlay(false);
		});
		
		keyframeMan = new KeyframeManagerWidget(engine, 0, Gdx.graphics.getHeight() - 180, 175, 180);
		addWidget(keyframeMan);

		/*
		 * test = new Animation(); test.startKeyframe(16).startKeyframe(80)
		 * .startKeyframe(24).startKeyframe(40) .startKeyframe(42).startKeyframe(90)
		 * .startKeyframe(68).startKeyframe(80) .startKeyframe(68).startKeyframe(80)
		 * .startKeyframe(68).startKeyframe(80)
		 * .startKeyframe(68).startKeyframe(80).end(); // Dur = 440 instead of 1686
		 * animTime.setModel(ModelDisplayHandler.model); animTime.setAnimation(test);
		 * animTime.setKeyframes(test.getKeyframes(), test.getDuration());
		 */
	}

	// Me quede haciendo la parte en la que sale el tiempo de animacion y las partes
	// del modelo
	// asi como su traslacion y rotacion

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color color = sr.getColor();
		sr.setColor(0.2f, 0.1f, 0.1f, 1.0f);
		sr.begin(ShapeType.Filled);
		sr.rect(175, Gdx.graphics.getHeight() - 180, w, h);
		sr.setColor(0.15f, 0.15f, 0.15f, 1.0f);
		sr.rect(0, Gdx.graphics.getHeight() - 180, 175, 200);
		sr.setColor(0.1f, 0.1f, 0.1f, 1.0f);
		sr.rect(0, Gdx.graphics.getHeight() - 210, 175, 30);
		sr.setColor(1.0f, 0.4f, 0.4f, 1.0f);
		sr.rect(175, Gdx.graphics.getHeight() - 180 + (h * 0.5f), w, 2);
		sr.end();
		sr.setColor(color);
		super.render(sr, font, sp);
		playBtn.render(sr, font, sp);
		stopBtn.render(sr, font, sp);
	}
	
	private void triangle(ShapeRenderer sr, float x, float y, float w) {
		sr.triangle(x, y, x + w, y + (w / 2), x, y + w);
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		super.mouseDown(x, y, button);
		playBtn.mouseDown(x, y, button);
		stopBtn.mouseDown(x, y, button);
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		super.mouseUp(x, y, button);
		playBtn.mouseUp(x, y, button);
		stopBtn.mouseUp(x, y, button);
	}
	
	@Override
	public void mouseDragged(int x, int y) {
		super.mouseDragged(x, y);
	}

	@Override
	public void onEventFired(Event e) {
		if(e instanceof ChangeEvent) {
			if(e.getTarget() instanceof JgTextField) {
				if(((JgTextField)e.getTarget()).getId() == 6) {
					if(!animTime.getAnimator().play) {
						String text = animTick.getText();
						float val = 0;
						if(text.matches("[0-9.]+")) {
							val = Math.max(0, Float.parseFloat(text));
						} else {
							val = 0.0f;
						}
						if(val > animTime.getAnimator().getAnimation().getDuration()) {
							val = animTime.getAnimator().getAnimation().getDuration();
						}
						animTime.getAnimationTickMarker().setX(MathUtils.lerp(animTime.getX(), 
								animTime.getMaxVX(), val / animTime.getAnimation().getDuration()));
					}
				}
			}
		}
	}
	
	@Override
	public void dispose() {

	}

	public int getCurrent() {
		return 0;
	}

	public JgTextField getField() {
		return animTick;
	}

	public AnimationTimeline getAnimTime() {
		return animTime;
	}
	
	public KeyframeManagerWidget getKeyframeManager() {
		return keyframeMan;
	}
	
	public Engine getEngine() {
		return engine;
	}

	@Override
	public void onNewModel(JgModel model) {
		animTick.setValue(0);
		animTime.clear();
		animTime.setModel(model);
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

}
