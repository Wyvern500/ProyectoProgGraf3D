package com.lecheros.display.widgets.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lecheros.animation.Keyframe;
import com.lecheros.animator.Engine;
import com.lecheros.display.handlers.FocusHandler;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.IMouseListener;
import com.lecheros.display.widgets.Widget;
import com.lecheros.model.ModelPart;
import com.lecheros.utils.Utils;

public class KeyframeEntry extends Widget implements IMouseListener<KeyframeEntry>, IKeyboardListener<KeyframeEntry> {

	private Engine engine;
	private ModelPart part;
	private List<TransformEntry> entries;
	private Map<String, TransformEntry> transforms;
	private TransformEntry trEntry;
	private TransformEntry rtEntry;
	private int[] selected;
	private Vector2 mouse;
	
	public KeyframeEntry(ModelPart part, Engine engine) {
		super(0, 0, 175, 60);
		this.part = part;
		this.engine = engine;
		entries = new ArrayList<>();
		transforms = new HashMap<>();
		selected = new int[] { -1, -1 };
		mouse = new Vector2();
	}

	public void tick() {
		
	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp, float x, float y) {
		Color c = sr.getColor();

		int offset = 40;
		int iconOffset = 10;
		// float y = pos.y + yOffset;

		sr.begin(ShapeType.Filled);
		// Delete keyframe entry boundaries
		sr.setColor(0.13f, 0.13f, 0.13f, 1.0f);
		sr.rect(x, y, w, this.h);
		// Delete keyframe entry boundaries
		sr.setColor(0.2f, 0.2f, 0.2f, 1.0f);
		sr.rect(x + w - offset, y, offset, 20);
		sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		sr.rect(x + w - offset + iconOffset + 2, y + 8, (offset / 2) - 4, 2);
		sr.setColor(0.13f, 0.13f, 0.13f, 1.0f);
		// Add translation keyframe entry boundaries
		sr.rect(x + w - offset, y + 20, offset, 20);
		// Plus icon
		sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		sr.rect(x + w - offset + iconOffset + 2, y + 8 + 20, (offset / 2) - 4, 2);
		sr.rect(x + w - offset + (iconOffset * 2) - 1, y + 2 + 20, 2, 16);
		// Add rotation keyframe entry boundaries
		sr.setColor(0.13f, 0.13f, 0.13f, 1.0f);
		sr.rect(x + w - offset, y + 40, offset, 20);
		sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		sr.rect(x + w - offset + iconOffset + 2, y + 8 + 40, (offset / 2) - 4, 2);
		sr.rect(x + w - offset + (iconOffset * 2) - 1, y + 2 + 40, 2, 16);
		sr.end();

		sp.begin();
		font.draw(sp, part.getName(), x, y);
		// Translation and rotation kayframe parts
		font.draw(sp, "Translation", x + 20, y + 20);
		font.draw(sp, "Rotation", x + 20, y + 40);
		sp.end();

		sr.setColor(c);
		
		for(TransformEntry entry : entries) {
			entry.render(sr, font, sp);
		}
		
		for(int i = 0; i < entries.size(); i++) {
			TransformEntry entry = entries.get(i);
			if(Utils.collidesWithMouse(mouse.x, mouse.y, entry.getPos(), 
					entry.getKeyframeSize(), entry.getKeyframeSize())) {
				renderTooltip(entry, sr, font, sp);
			}	
		}
	}

	private void renderTooltip(TransformEntry entry, ShapeRenderer sr, BitmapFont font, 
			SpriteBatch batch) {
		float x = mouse.x;
		float y = mouse.y;
		pushColor(sr);
		sr.begin(ShapeType.Filled);
		sr.setColor(0.13f, 0.13f, 0.13f, 1.0f);
		sr.rect(x, y, 120, 20);
		sr.end();
		batch.begin();
		font.draw(batch, "StartTick: " + entry.getKeyframe().startTick, x + 10, y);
		batch.end();
		popColor(sr);
	}
	
	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
		
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		for(int i = 0; i < entries.size(); i++) {
			TransformEntry entry = entries.get(i);
			if(Utils.collidesWithMouse(x, y, entry.getPos(), entry.getKeyframeSize(), 
					entry.getKeyframeSize())) {
				if(entry.isATranslationEntry()) {
					if(selected[0] != i) {
						if(trEntry != null) {
							trEntry.setSelected(false);
						}
						trEntry = entry;
						FocusHandler.INSTANCE.setLastFocus(TransformEntry.class);
						entry.setSelected(true);
						selected[0] = i;
					} else {
						entry.setSelected(false);
						selected[0] = -1;
					}
				} else {
					if(selected[1] != i) {
						if(rtEntry != null) {
							rtEntry.setSelected(false);
						}
						rtEntry = entry;
						entry.setSelected(true);
						selected[1] = i;
					} else {
						entry.setSelected(false);
						selected[1] = -1;
					}
				}
				Logger.getGlobal().info("Clicked");
			}
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {
		mouse.set(x, y);
	}
	
	@Override
	public void mouseScroll(float delta) {

	}
	
	@Override
	public void onKeyDown(int keycode) {
		for(TransformEntry e : entries) {
			e.onKeyDown(keycode);
		}
	}

	@Override
	public void onKeyUp(int keycode) {
		for(TransformEntry e : entries) {
			e.onKeyUp(keycode);
		}
	}

	@Override
	public void dispose() {

	}
	
	public void remove() {
		for(TransformEntry e : entries) {
			e.remove();
		}
	}
	
	public void addTransformEntry(Keyframe kf, AnimationTimeline animLine, float ey, 
			boolean translation) {
		ey += translation ? 20 : 40;
		String key = String.valueOf(kf.startTick) + ":" + String.valueOf(translation);
		if(!transforms.containsKey(key)) {
			transforms.put(key, new TransformEntry(this, kf, animLine.getAnimation(), animLine.getX(), 
					animLine.getMaxVX(), ey, 15, 15, translation));
			synchronize();
		} else {
			Logger.getGlobal().info("Transform Entry already exists");
		}
	}
	
	public void addTransformEntry(Keyframe kf, AnimationTimeline animLine, float ey, 
			float[] data, boolean translation) {
		ey += translation ? 20 : 40;
		String key = String.valueOf(kf.startTick) + ":" + String.valueOf(translation);
		if(!transforms.containsKey(key)) {
			TransformEntry entry = new TransformEntry(this, kf, animLine.getAnimation(), 
					animLine.getX(), animLine.getMaxVX(), ey, 15, 15, translation);
			entry.setTransform(data);
			transforms.put(key, entry);
			synchronize();
		} else {
			Logger.getGlobal().info("Transform Entry already exists");
		}
	}
	
	public void removeTransformEntry(Keyframe kf, boolean translation) {
		transforms.remove(String.valueOf(kf.startTick) + ":" + translation);
		synchronize();
	}
	
	public void synchronize() {
		entries = new ArrayList<>(transforms.values());
	}
	
	public ModelPart getModelPart() {
		return part;
	}
	
	public List<TransformEntry> getTransformEntries(){
		return entries;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public int[] getSelectedIndex() {
		return selected;
	}

}
