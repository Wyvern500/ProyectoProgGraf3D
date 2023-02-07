package com.lecheros.display.widgets;

import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.lecheros.events.JgClickListener;

public class TextButtonTest extends TextButton {

	JgNode node;
	
	public TextButtonTest(String text, Skin skin) {
		super(text, skin);
		this.node = node;
		addListener(new JgClickListener());
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// x >= 0 && x < width && y >= 0 && y < height
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			if(x >= -18 && x < 0 && y >= 0 && y < getHeight()) {
				if(!node.isExpanded()) {
					node.setExpanded(true);
				} else {
					node.setExpanded(false);
				}
				Logger.getGlobal().info("Open");
			}
		}
		return super.hit(x, y, touchable);
	}
	
}
