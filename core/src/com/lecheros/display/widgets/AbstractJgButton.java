package com.lecheros.display.widgets;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lecheros.utils.Utils;

public abstract class AbstractJgButton extends Widget implements IMouseListener<AbstractJgButton> {

	protected boolean pressed;
	protected OnBtnClick onClick;
	
	public AbstractJgButton(float x, float y, float w, float h) {
		super(x, y, w, h);
	}

	public AbstractJgButton setOnClick(OnBtnClick click) {
		onClick = click;
		return this;
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void dispose() {
		
	}
	
	protected void renderBasic(ShapeRenderer sr) {
		sr.rect(pos.x, pos.y, w, h);
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		if(Utils.collidesWithMouse(x, y, pos, w, h)) {
			pressed = true;
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		if(pressed) {
			pressed = false;
			onClick.run(this);
		}
	}

	@Override
	public void mouseDragged(int x, int y) {
		
	}
	
	@Override
	public void mouseMoved(int x, int y) {
		
	}

	@Override
	public void mouseScroll(float delta) {
		
	}
	
	public static interface OnBtnClick {
		public void run(AbstractJgButton btn);
	}

}
