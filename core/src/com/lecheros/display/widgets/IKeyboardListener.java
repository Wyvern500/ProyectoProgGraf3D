package com.lecheros.display.widgets;

public interface IKeyboardListener<T extends Widget> {

	public void onKeyDown(int keycode);

	public void onKeyUp(int keycode);

}
