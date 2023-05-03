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
import com.lecheros.animator.Engine;

public class JgTypedList<T> extends Widget implements IJgList<T>, IMouseListener<JgTypedList<T>> {

	protected List<T> keys;
	protected List<Integer> selected;
	protected float itemHeight;
	protected int visibleItemsCount;
	protected int timer;
	protected int clicks;
	protected int doubleClickDuration;
	protected boolean leftClicked;
	protected Color bgColor;
	protected Color keyColor;
	protected SideBarWidget sideBar;
	protected T lastKeySelected;
	protected OnSelectKey<T> onSelect;
	protected OnRightClick<T> onRightClick;
	protected OnDoubleClick<T> onDoubleClick;

	public JgTypedList(int x, int y, float w, float itemHeight, int visibleItemsCount, OnSelectKey<T> onSelect) {
		this(x, y, w, itemHeight, visibleItemsCount, onSelect, null);
	}

	public JgTypedList(int x, int y, float w, float itemHeight, int visibleItemsCount, OnSelectKey<T> onSelect,
			T... keys) {
		super(x, y, w, itemHeight * visibleItemsCount);
		this.w = w;
		this.h = itemHeight * visibleItemsCount;
		this.visibleItemsCount = visibleItemsCount;
		this.keys = keys != null ? new ArrayList<T>(Arrays.asList(keys)) : new ArrayList<T>();
		sideBar = new SideBarWidget(pos.cpy().add(w, 0), 20, h, itemHeight, this.keys.size(), visibleItemsCount);
		bgColor = new Color(0.1f, 0.1f, 0.1f, 1f);
		keyColor = new Color(0.3f, 0.3f, 0.3f, 1f);
		this.itemHeight = itemHeight;
		this.selected = new ArrayList<>();
		this.onSelect = onSelect;
		this.onRightClick = (k) -> {};
		this.onDoubleClick = (k, i) -> {};
		this.doubleClickDuration = 6;
	}

	/*
	 * public void addTestAnimations(Engine engine) { for(T s : this.keys) { JgModel
	 * model = Utils.getModelDisplayHandler(engine).getModel();
	 * model.getAnimations().add(new Animation(s).setModelName(model.getName())); }
	 * }
	 */

	public void tick() {
		sideBar.tick();
		if(timer < doubleClickDuration && leftClicked) {
			timer++;
		} else {
			timer = 0;
			clicks = 0;
			leftClicked = false;
		}
	}

	public void onClick(int mx, int my, int button) {
		doubleClickDuration = 18;
		for (int i = sideBar.getFrom(); i < sideBar.getTo(); i++) {
			int rcoly = (int) ((this.pos.y) + ((i - sideBar.getFrom()) * this.itemHeight));
			if (mx > this.pos.x && mx < (this.pos.x + this.w - this.sideBar.getWidth())) {
				if (my > rcoly && my < rcoly + this.itemHeight) {
					if(button == Input.Buttons.LEFT) {
						// Double Click check
						leftClicked = true;
						if(timer < doubleClickDuration) {
							clicks++;
							if(clicks >= 2) {
								onDoubleClick.onDoubleTick(lastKeySelected, i);
								timer = 0;
								clicks = 0;
								leftClicked = false;
							}
						}
						// Checking if a key is clicked
						if (selected.contains(i)) {
							if (Engine.keys[Input.Keys.CONTROL_LEFT]) {
								selected.remove(selected.indexOf(i));
							} else {
								selected.clear();
							}
						} else {
							if (Engine.keys[Input.Keys.CONTROL_LEFT]) {
								selected.add(i);
								lastKeySelected = keys.get(i);
								onSelect.onClick(getSelectedKey(), i);
							} else {
								selected.clear();
								selected.add(i);
								lastKeySelected = keys.get(i);
								onSelect.onClick(getSelectedKey(), i);
							}
						}
					} else if(button == Input.Buttons.RIGHT) {
						onRightClick.run(keys.get(i));
					}
				}
			}
		}
	}
	
	public void onDoubleClick() {
		
	}
	
	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
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
				font.draw(sp, keys.get(i).toString(), pos.x,
						this.pos.y + 2 + ((i - sideBar.getFrom()) * this.itemHeight));
			}
			sp.end();
		}

		sr.setColor(c);
		sideBar.render(sr);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void mouseDown(int x, int y, int button) {
		sideBar.mouseDown(x, y);
		onClick(x, y, button);
	}

	@Override
	public void mouseUp(int x, int y, int button) {

	}

	@Override
	public void mouseScroll(float delta) {
		sideBar.onScroll(delta);
	}

	public void mouseDragged(int mx, int my) {
		sideBar.mouseDragged(mx, my);
	}

	@Override
	public void mouseMoved(int x, int y) {
		
	}
	
	public void update() {
		if (keys.isEmpty())
			return;
		sideBar.update(this.keys.size());
	}

	public void setKeys(T... keys) {
		this.keys = new ArrayList<T>(Arrays.asList(keys));
		sideBar.setItemsCount(keys.length);
		update();
	}

	public void addKey(T key) {
		if (key != null) {
			try {
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

	public T getSelectedKey() {
		if (keys.size() > 0 && !selected.isEmpty()) {
			try {
				return keys.get(this.selected.get(0));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}

	public void clear() {
		keys.clear();
		selected.clear();
		sideBar.clear();
	}

	public List<T> getKeys() {
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

	public void setOnRightClick(OnRightClick<T> rightClick) {
		this.onRightClick = rightClick;
	}
	
	public void setOnDoubleClick(OnDoubleClick<T> doubleClick) {
		this.onDoubleClick = doubleClick;
	}

	public static abstract class JgTypedListKey<T> {

		private T type;

		public JgTypedListKey(T type) {
			this.type = type;
		}

		public T getType() {
			return type;
		}

		@Override
		public String toString() {
			return getDisplayText();
		}

		public abstract String getDisplayText();

	}

	public interface OnRightClick<T> {
		public void run(T key);
	}
	
	public static interface OnSelectKey<T> {
		public void onClick(T key, int index);
	}
	
	public static interface OnDoubleClick<T> {
		public void onDoubleTick(T key, int index);
	}
	
}
