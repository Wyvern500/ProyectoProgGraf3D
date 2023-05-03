package com.lecheros.display.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lecheros.animation.Animation;
import com.lecheros.animator.Engine;
import com.lecheros.model.JgModel;

public class JgList implements IJgList<String> {

	protected List<String> keys;
	protected List<Integer> selected;
	protected Vector2 pos;
	protected float itemHeight;
	protected float w;
	protected float h;
	protected int visibleItemsCount;
	protected Color bgColor;
	protected Color keyColor;
	protected SideBarWidget sideBar;
	protected OnSelectKey onSelect;

	public JgList(Vector2 pos, float w, float itemHeight, int visibleItemsCount, OnSelectKey onSelect, String... keys) {
		this.pos = pos;
		this.w = w;
		this.h = itemHeight * visibleItemsCount;
		this.visibleItemsCount = visibleItemsCount;
		this.keys = new ArrayList<String>(Arrays.asList(keys));
		sideBar = new SideBarWidget(pos.cpy().add(w, 0), 20, h, itemHeight, keys.length, visibleItemsCount);
		bgColor = new Color(0.1f, 0.1f, 0.1f, 1f);
		keyColor = new Color(0.3f, 0.3f, 0.3f, 1f);
		this.itemHeight = itemHeight;
		this.selected = new ArrayList<>();
		this.onSelect = onSelect;
	}

	public void addTestAnimations(Engine engine) {
		for (String s : this.keys) {
			JgModel model = engine.getModelDisplay().getModel();
			model.getAnimations().add(new Animation(s).setModelName(model.getName()));
		}
	}

	public void tick() {
		sideBar.tick();
	}

	public void render(ShapeRenderer sr, SpriteBatch sp, BitmapFont font) {
		Color c = sr.getColor();

		sr.begin(ShapeType.Filled);
		sr.setColor(bgColor);
		sr.rect(pos.x, pos.y, w, h);
		if (!keys.isEmpty()) {
			sr.setColor(keyColor);
			for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
				Color s = keyColor;
				if (selected.contains(i))
					s = keyColor.cpy().add(0.2f, 0.2f, 0.2f, 1f);
				sr.setColor(s);
				sr.rect(pos.x + 2, this.pos.y + ((i - sideBar.getFrom()) * this.itemHeight) + 2, w - 2, itemHeight - 2);
			}
		}
		sr.end();

		if (!keys.isEmpty()) {
			sp.begin();
			sp.setColor(Color.WHITE);
			for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
				font.draw(sp, keys.get(i), pos.x, this.pos.y + 2 + ((i - sideBar.getFrom()) * this.itemHeight));
			}
			sp.end();
		}

		sr.setColor(c);
		sideBar.render(sr);
	}

	public void onClick(int mx, int my) {
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			int rcoly = (int) ((this.pos.y) + ((i - sideBar.getFrom()) * this.itemHeight));
			if (mx > this.pos.x && mx < (this.pos.x + this.w - this.sideBar.getWidth())) {
				if (my > rcoly && my < rcoly + this.itemHeight) {
					if (selected.contains(i)) {
						if (Engine.keys[Input.Keys.CONTROL_LEFT]) {
							selected.remove(selected.indexOf(i));
						} else {
							selected.clear();
						}
					} else {
						if (Engine.keys[Input.Keys.CONTROL_LEFT]) {
							selected.add(i);
							onSelect.onClick(getSelectedKey(), i);
						} else {
							selected.clear();
							selected.add(i);
							onSelect.onClick(getSelectedKey(), i);
						}
					}
				}
			}
		}
	}

	public void mouseDown(int mx, int my) {
		sideBar.mouseDown(mx, my);
		onClick(mx, my);
	}

	public void mouseDragged(int mx, int my) {
		sideBar.mouseDragged(mx, my);
	}

	public void onScroll(float delta) {
		sideBar.onScroll(delta);
	}

	public void update() {
		if (keys.isEmpty())
			return;
		sideBar.update(this.keys.size());
	}

	public void setKeys(String... keys) {
		this.keys = new ArrayList<String>(Arrays.asList(keys));
		sideBar.setItemsCount(keys.length);
		update();
	}

	public void addKey(String key) {
		if (key != null) {
			try {
				if (key != null) {
					Logger.getGlobal().info("Key not null");
				}
				keys.add(key);
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}
			update();
		} else {
			Logger.getGlobal().info("Key == null");
		}
	}

	public void removeKey(String key) {
		if (selected.contains(keys.indexOf(key))) {
			selected.remove(selected.indexOf(keys.indexOf(key)));
		}
		keys.remove(key);
		update();
	}

	public void removeKey(int index) {
		keys.remove(index);
		if (selected.contains(index)) {
			selected.remove(selected.indexOf(index));
		}
		update();
	}

	public void removeSelectedKeys() {
		List<Integer> temp = new ArrayList<>();
		for (int i : selected) {
			temp.add(i);
		}
		for (int i : temp) {
			if (i < keys.size()) {
				removeKey(i);
			}
		}
	}

	public String getSelectedKey() {
		if (keys.size() > 0 && !selected.isEmpty()) {
			try {
				return keys.get(this.selected.get(0));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return "empty";
		} else {
			return "empty";
		}
	}

	public List<String> getKeys() {
		return keys;
	}

	public Vector2 getPos() {
		return pos;
	}

	public float getItemHeight() {
		return itemHeight;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public int getVisibleItemsCount() {
		return visibleItemsCount;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public Color getKeyColor() {
		return keyColor;
	}

	public List<Integer> getSelectedKeys() {
		return selected;
	}

	public SideBarWidget getSideBar() {
		return sideBar;
	}

	public static interface OnSelectKey {

		public void onClick(String key, int index);

	}

}
