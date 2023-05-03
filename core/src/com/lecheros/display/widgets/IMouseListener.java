package com.lecheros.display.widgets;

public interface IMouseListener<T extends Widget> {

	public void mouseDown(int x, int y, int button);

	public void mouseUp(int x, int y, int button);

	public void mouseDragged(int x, int y);

	public void mouseScroll(float delta);

	public void mouseMoved(int x, int y);

}
