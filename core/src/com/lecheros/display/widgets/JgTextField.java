package com.lecheros.display.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class JgTextField extends TextField {

	private OnChange change;
	private int id;

	public JgTextField(int id, String text, Skin skin) {
		this(id, text, skin, null);
	}

	public JgTextField(int id, String text, Skin skin, OnChange onChange) {
		super(text, skin);
		this.id = id;
		this.change = onChange;
	}
	
	public void setValue(float val) {
		setText(String.valueOf(val));
	}

	@Override
	public void setText(String str) {
		super.setText(str);
		if (change != null) {
			change.onChange(this, str);
		}
	}

	public void setOnChange(OnChange change) {
		this.change = change;
	}

	public int getId() {
		return id;
	}

	public interface OnChange {
		public void onChange(JgTextField tf, String txt);
	}

}
