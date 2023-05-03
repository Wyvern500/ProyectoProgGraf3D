package com.lecheros.display.handler;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.lecheros.display.ADisplayHandler;

public interface IDisplayHandlerEventListener<T extends ADisplayHandler> {

	public void onEventFired(Event e);

}
