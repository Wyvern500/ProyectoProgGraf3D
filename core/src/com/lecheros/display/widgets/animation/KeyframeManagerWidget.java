package com.lecheros.display.widgets.animation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.animation.Keyframe;
import com.lecheros.animator.Engine;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.IMouseListener;
import com.lecheros.display.widgets.SideBarWidget;
import com.lecheros.display.widgets.Widget;
import com.lecheros.model.ModelPart;

public class KeyframeManagerWidget extends Widget
		implements IKeyboardListener<KeyframeManagerWidget>, IMouseListener<KeyframeManagerWidget> {

	private List<KeyframeEntry> entries;
	private LinkedHashMap<ModelPart, KeyframeEntry> entriesMap;
	private Engine engine;
	private SideBarWidget sideBar;

	public KeyframeManagerWidget(Engine engine, float x, float y, float w, float h) {
		super(x, y, w, h);
		entries = new ArrayList<>();
		entriesMap = new LinkedHashMap<>();
		this.engine = engine;
		sideBar = new SideBarWidget(pos, 20, h, 60, 0, 3);
		sideBar.setTransparency(true, 0.01f);
	}

	@Override
	public void tick() {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.tick();
		}
		sideBar.tick();
		// Logger.getGlobal().info("From To");
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.render(sr, font, batch, 0, pos.y + (i - sideBar.getFrom()) * 60);
		}

		sideBar.render(sr);
	}

	public void addKeyframeEntry(KeyframeEntry e) {
		entriesMap.putIfAbsent(e.getModelPart(), e);
		entries = new ArrayList<>(entriesMap.values());
		sideBar.update(entries.size());
		Logger.getGlobal().info("SideBar items Count: " + sideBar.getItemsCount());
	}

	@Override
	public void dispose() {

	}

	@Override
	public void mouseDown(int x, int y, int button) {
		KeyframeEntry tempEntry = null;
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.mouseDown(x, y, button);
			float ex = 0;
			float ey = pos.y + (i - sideBar.getFrom()) * 60;
			float w = 175;
			float h = 60;
			float offset = 40;

			if (x > ex + w - offset && x < ex + w) {
				if (y > ey && y < ey + (h / 3)) { // Delete keyframe entry
					Logger.getGlobal().info("Delete keyframe");
					tempEntry = entries.get(i);
				} else if (y > ey + (h / 3) && y < ey + ((h / 3) * 2)) { // Add translation
					Logger.getGlobal().info("Add Translation");
					AnimationTimeline animTime = engine.getAnimStuff().getAnimTime();
					if(animTime.getAnimation() != null) {
						Keyframe kf = new Keyframe(0, animTime.getAnimationTickMarker()
								.getTick());
						if(!animTime.getAnimation().hasKeyframe(kf)) {
							animTime.addKeyframe(kf);
						}
						entry.addTransformEntry(kf, engine.getAnimStuff().getAnimTime(), ey, true);
						Logger.getGlobal().info("AnimTick: " + animTime.getAnimationTickMarker()
						.getTick());
					}
				} else if (y > ey + h - (h / 3) && y < ey + h) { // Add rotation
					Logger.getGlobal().info("Add Rotation");
					AnimationTimeline animTime = engine.getAnimStuff().getAnimTime();
					if(animTime.getAnimation() != null) {
						Keyframe kf = new Keyframe(0, animTime.getAnimationTickMarker()
								.getTick());
						if(!animTime.getAnimation().hasKeyframe(kf)) {
							animTime.addKeyframe(kf);
						}
						entry.addTransformEntry(kf, engine.getAnimStuff().getAnimTime(), ey, false);
					}
				}
			}
		}
		sideBar.mouseDown(x, y);
		if (tempEntry != null) {
			tempEntry.remove();
			entriesMap.remove(tempEntry.getModelPart());
			entries = new ArrayList<>(entriesMap.values());
			sideBar.update(entries.size());
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.mouseUp(x, y, button);
		}
	}

	@Override
	public void mouseDragged(int x, int y) {
		sideBar.mouseDragged(x, y);
	}

	@Override
	public void mouseMoved(int x, int y) {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.mouseMoved(x, y);
		}
		
	}
	
	@Override
	public void mouseScroll(float delta) {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			KeyframeEntry entry = entries.get(i);
			entry.mouseScroll(delta);
		}
		sideBar.onScroll(delta);
	}

	@Override
	public void onKeyDown(int keycode) {
		for(KeyframeEntry e : entries) {
			e.onKeyDown(keycode);
		}
	}

	@Override
	public void onKeyUp(int keycode) {
		for(KeyframeEntry e : entries) {
			e.onKeyUp(keycode);
		}
	}

}
