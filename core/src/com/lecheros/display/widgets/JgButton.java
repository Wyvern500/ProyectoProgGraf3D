package com.lecheros.display.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;
import com.lecheros.events.JgClickListener;

public class JgButton extends Table implements Disableable {
	private ButtonStyle style;
	boolean isChecked, isDisabled;
	private JgClickListener clickListener;
	private boolean programmaticChangeEvents = true;

	public JgButton (Skin skin) {
		super(skin);
		initialize();
		setStyle(skin.get(ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public JgButton (Skin skin, String styleName) {
		super(skin);
		initialize();
		setStyle(skin.get(styleName, ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public JgButton (Actor child, Skin skin, String styleName) {
		this(child, skin.get(styleName, ButtonStyle.class));
		setSkin(skin);
	}

	public JgButton (Actor child, ButtonStyle style) {
		initialize();
		add(child);
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	public JgButton (ButtonStyle style) {
		initialize();
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	/** Creates a button without setting the style or size. At least a style must be set before using this button. */
	public JgButton () {
		initialize();
	}

	private void initialize () {
		setTouchable(Touchable.enabled);
		addListener(clickListener = new JgClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (isDisabled()) return;
				setChecked(!isChecked, true);
			}
		});
	}

	public JgButton (@Null Drawable up) {
		this(new ButtonStyle(up, null, null));
	}

	public JgButton (@Null Drawable up, @Null Drawable down) {
		this(new ButtonStyle(up, down, null));
	}

	public JgButton (@Null Drawable up, @Null Drawable down, @Null Drawable checked) {
		this(new ButtonStyle(up, down, checked));
	}

	public JgButton (Actor child, Skin skin) {
		this(child, skin.get(ButtonStyle.class));
	}

	public void setChecked (boolean isChecked) {
		setChecked(isChecked, programmaticChangeEvents);
	}

	void setChecked (boolean isChecked, boolean fireEvent) {
		if (this.isChecked == isChecked) return;
		this.isChecked = isChecked;

		if (fireEvent) {
			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
			if (fire(changeEvent)) this.isChecked = !isChecked;
			Pools.free(changeEvent);
		}
	}

	/** Toggles the checked state. This method changes the checked state, which fires a {@link ChangeEvent} (if programmatic change
	 * events are enabled), so can be used to simulate a button click. */
	public void toggle () {
		setChecked(!isChecked);
	}

	public boolean isChecked () {
		return isChecked;
	}

	public boolean isPressed () {
		return clickListener.isVisualPressed();
	}

	public boolean isOver () {
		return clickListener.isOver();
	}

	public JgClickListener getClickListener () {
		return clickListener;
	}

	public boolean isDisabled () {
		return isDisabled;
	}

	/** When true, the button will not toggle {@link #isChecked()} when clicked and will not fire a {@link ChangeEvent}. */
	public void setDisabled (boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	/** If false, {@link #setChecked(boolean)} and {@link #toggle()} will not fire {@link ChangeEvent}. The event will only be
	 * fired only when the user clicks the button */
	public void setProgrammaticChangeEvents (boolean programmaticChangeEvents) {
		this.programmaticChangeEvents = programmaticChangeEvents;
	}

	public void setStyle (ButtonStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;

		setBackground(getBackgroundDrawable());
	}

	/** Returns the button's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called. */
	public ButtonStyle getStyle () {
		return style;
	}

	/** Returns appropriate background drawable from the style based on the current button state. */
	protected @Null Drawable getBackgroundDrawable () {
		if (isDisabled() && style.disabled != null) return style.disabled;
		if (isPressed()) {
			if (isChecked() && style.checkedDown != null) return style.checkedDown;
			if (style.down != null) return style.down;
		}
		if (isOver()) {
			if (isChecked()) {
				if (style.checkedOver != null) return style.checkedOver;
			} else {
				if (style.over != null) return style.over;
			}
		}
		boolean focused = hasKeyboardFocus();
		if (isChecked()) {
			if (focused && style.checkedFocused != null) return style.checkedFocused;
			if (style.checked != null) return style.checked;
			if (isOver() && style.over != null) return style.over;
		}
		if (focused && style.focused != null) return style.focused;
		return style.up;
	}

	public void draw (Batch batch, float parentAlpha) {
		validate();

		setBackground(getBackgroundDrawable());

		float offsetX = 0, offsetY = 0;
		if (isPressed() && !isDisabled()) {
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else if (isChecked() && !isDisabled()) {
			offsetX = style.checkedOffsetX;
			offsetY = style.checkedOffsetY;
		} else {
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		boolean offset = offsetX != 0 || offsetY != 0;

		Array<Actor> children = getChildren();
		if (offset) {
			for (int i = 0; i < children.size; i++)
				children.get(i).moveBy(offsetX, offsetY);
		}
		super.draw(batch, parentAlpha);
		if (offset) {
			for (int i = 0; i < children.size; i++)
				children.get(i).moveBy(-offsetX, -offsetY);
		}

		Stage stage = getStage();
		if (stage != null && stage.getActionsRequestRendering() && isPressed() != clickListener.isPressed())
			Gdx.graphics.requestRendering();
	}

	public float getPrefWidth () {
		float width = super.getPrefWidth();
		if (style.up != null) width = Math.max(width, style.up.getMinWidth());
		if (style.down != null) width = Math.max(width, style.down.getMinWidth());
		if (style.checked != null) width = Math.max(width, style.checked.getMinWidth());
		return width;
	}

	public float getPrefHeight () {
		float height = super.getPrefHeight();
		if (style.up != null) height = Math.max(height, style.up.getMinHeight());
		if (style.down != null) height = Math.max(height, style.down.getMinHeight());
		if (style.checked != null) height = Math.max(height, style.checked.getMinHeight());
		return height;
	}

	public float getMinWidth () {
		return getPrefWidth();
	}

	public float getMinHeight () {
		return getPrefHeight();
	}

	/** The style for a button, see {@link Button}.
	 * @author mzechner */
	static public class ButtonStyle {
		public @Null Drawable up, down, over, focused, disabled;
		public @Null Drawable checked, checkedOver, checkedDown, checkedFocused;
		public float pressedOffsetX, pressedOffsetY, unpressedOffsetX, unpressedOffsetY, checkedOffsetX, checkedOffsetY;

		public ButtonStyle () {
		}

		public ButtonStyle (@Null Drawable up, @Null Drawable down, @Null Drawable checked) {
			this.up = up;
			this.down = down;
			this.checked = checked;
		}

		public ButtonStyle (ButtonStyle style) {
			up = style.up;
			down = style.down;
			over = style.over;
			focused = style.focused;
			disabled = style.disabled;

			checked = style.checked;
			checkedOver = style.checkedOver;
			checkedDown = style.checkedDown;
			checkedFocused = style.checkedFocused;

			pressedOffsetX = style.pressedOffsetX;
			pressedOffsetY = style.pressedOffsetY;
			unpressedOffsetX = style.unpressedOffsetX;
			unpressedOffsetY = style.unpressedOffsetY;
			checkedOffsetX = style.checkedOffsetX;
			checkedOffsetY = style.checkedOffsetY;
		}
	}
}
