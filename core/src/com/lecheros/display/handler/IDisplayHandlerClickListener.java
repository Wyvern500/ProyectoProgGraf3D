package com.lecheros.display.handler;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface IDisplayHandlerClickListener<T> {

	public void handleClick(InputEvent e, float x, float y);
	
}
