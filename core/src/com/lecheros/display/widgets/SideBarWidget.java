package com.lecheros.display.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SideBarWidget {

	private Vector2 pos;
	private int itemsCount;
	private int from;
	private int to;
	private int hidedItems;
	private int visibleItemsCount;
	private int offset;
	private float itemHeight;
	private float scrollY;
	private float scrollHeight;
	private float w;
	private float h;
	private float alpha;
	private boolean transparent;

	public SideBarWidget(Vector2 pos, float w, float h, int visibleItemsCount) {
		this(pos, w, h, 50, 12, visibleItemsCount);
	}

	public SideBarWidget(Vector2 pos, float w, float h, float itemHeight, int itemsCount, int visibleItemsCount) {
		this.pos = pos;
		this.scrollY = pos.y;
		this.w = w;
		this.h = h;
		this.alpha = 1.0f;
		this.itemHeight = itemHeight;
		this.setItemsCount(itemsCount);
		this.from = 0;
		this.visibleItemsCount = visibleItemsCount == 0 ? 1 : visibleItemsCount;
		this.to = this.visibleItemsCount > itemsCount ? itemsCount : this.visibleItemsCount;
		this.hidedItems = (itemsCount - visibleItemsCount) < 0 ? 0 : itemsCount - visibleItemsCount;
		if (this.hidedItems != 0) {
			/*
			 * Logger.getGlobal() .info("HideItems: " + this.hidedItems + " keys size: " +
			 * itemsCount + " hideItems/keys size: " + ((float) this.hidedItems /
			 * itemsCount) + " other: " + (1 - (((float) this.hidedItems / itemsCount) == 0
			 * ? 1 : (float) this.hidedItems / itemsCount)));
			 */
			updateScrollHeight();
		}
		/*
		 * if(itemHeight > h / visibleItemsCount) { this.itemHeight = h /
		 * visibleItemsCount; }
		 */
	}

	public void tick() {

	}

	public void render(ShapeRenderer sr) {
		if (this.visibleItemsCount < itemsCount) {
			if (transparent) {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}
			Color c = sr.getColor();
			sr.begin(ShapeType.Filled);
			sr.setColor(0, 0, 0, alpha);
			sr.rect(pos.x, pos.y, w, h);
			sr.setColor(1, 1, 1, alpha + 0.2f);
			sr.rect(pos.x, scrollY, w, scrollHeight);
			sr.end();
			sr.setColor(c);
			if (transparent) {
				Gdx.gl.glDisable(GL20.GL_BLEND);
			}
		}
	}

	public void mouseDown(int mx, int my) {

	}

	public void mouseDragged(int mx, int my) {
		if (visibleItemsCount < itemsCount) {
			if (mx > this.pos.x && mx < (this.pos.x + this.w) && my > this.pos.y && my < this.pos.y + h) {
				// Logger.getGlobal().info("Detecting");
				this.scrollY = my - (this.scrollHeight / 2);
				this.wrapScrollY();
				float part1 = this.pos.y - this.scrollY;
				float part2 = ((float) (visibleItemsCount * itemHeight) - (float) scrollHeight);
				this.offset = MathUtils
						.floor(Math.abs(MathUtils.lerp(part1 / part2, 0, itemsCount - (visibleItemsCount - 1))));
				this.from = 0 + this.offset;
				if (visibleItemsCount > itemsCount) {
					this.to = itemsCount;
				} else {
					this.to = visibleItemsCount + offset;
				}
				/*
				 * Logger.getGlobal().info("Visible Items: " + visibleItemsCount +
				 * " total Items: " + itemsCount + " from: " + from + " to: " + to + " offset: "
				 * + offset);
				 */
			}
		}
	}

	public void onScroll(float delta) {
		if (this.visibleItemsCount < itemsCount && this.hidedItems != 0) {
			this.scrollY += delta * 10f;
			this.wrapScrollY();
			float part1 = (((float) this.pos.y) - ((float) this.scrollY));
			float part2 = (((float) (this.visibleItemsCount * this.itemHeight) - (float) this.scrollHeight));
			this.offset = MathUtils
					.floor(Math.abs(MathUtils.lerp(part1 / part2, 0, itemsCount - this.visibleItemsCount)));
			this.from = 0 + this.offset;
			if (this.visibleItemsCount > itemsCount) {
				this.to = itemsCount;
			} else {
				this.to = this.visibleItemsCount + offset;
			}
		}
	}

	private void wrapScrollY() {
		if (this.scrollY <= this.pos.y) {
			this.scrollY = this.pos.y;
			// Logger.getGlobal().info("SAds");
		}
		if (this.scrollY + this.scrollHeight > (this.pos.y + (this.itemsCount * this.itemHeight))) {
			this.scrollY = (this.pos.y + (this.itemsCount * this.itemHeight)) - this.scrollHeight;
			// Logger.getGlobal().info("SAds2");
		}
		if (this.scrollY + scrollHeight > this.pos.y + h) {
			this.scrollY = this.pos.y + h - scrollHeight;
		}
		/*
		 * Logger.getGlobal().info("x: " + this.pos.x + " scrollY: " + this.scrollY +
		 * " scrollHeight: " + scrollHeight + " su: " + (this.pos.y + (this.itemsCount *
		 * this.itemHeight)));
		 */
	}

	public void updateScrollHeight() {
		this.scrollHeight = (int) MathUtils.lerp(0, this.itemHeight * this.visibleItemsCount,
				1 - (((float) hidedItems / itemsCount) == 0 ? 1 : (float) hidedItems / itemsCount));
	}

	public void update(int newSize) {
		try {
			this.itemsCount = newSize;
			this.from = 0;
			this.to = this.visibleItemsCount > itemsCount ? itemsCount : this.visibleItemsCount;
			this.hidedItems = (itemsCount - this.visibleItemsCount) < 0 ? 0 : itemsCount - this.visibleItemsCount;
			/*
			 * Logger.getGlobal().info("hideItems: " + hidedItems + " keys size: " +
			 * itemsCount); Logger.getGlobal().info(" div: " + (hidedItems / itemsCount));
			 */
			updateScrollHeight();
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		this.from = 0;
		this.to = 0;
	}

	// Setters

	public void setTransparency(boolean transparency, float alpha) {
		this.transparent = transparency;
		this.alpha = alpha;
	}

	public void setItemsCount(int count) {
		this.itemsCount = count;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public int getVisibleItemsCount() {
		return visibleItemsCount;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setItemHeight(float itemHeight) {
		this.itemHeight = itemHeight;
	}

	public int getHidedItems() {
		return hidedItems;
	}

	public void setHidedItems(int hidedItems) {
		this.hidedItems = hidedItems;
	}

	public void setScrollHeight(float scrollHeight) {
		this.scrollHeight = scrollHeight;
	}

	public float getWidth() {
		return w;
	}

}
