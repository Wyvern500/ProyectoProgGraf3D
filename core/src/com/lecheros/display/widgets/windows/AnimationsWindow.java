package com.lecheros.display.widgets.windows;

import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lecheros.animation.Animation;
import com.lecheros.display.handlers.AnimationsDisplayerDisplayHandler;
import com.lecheros.display.handlers.FocusHandler;
import com.lecheros.display.widgets.JgTypedList.JgTypedListKey;
import com.lecheros.display.widgets.JgTextField;
import com.lecheros.display.widgets.WindowWidget;

public class AnimationsWindow extends WindowWidget<AnimationsDisplayerDisplayHandler> {

	public static final String ID = "AnimationsWindow";
	
	private int current;
	private TextField[] fields;
	
	public AnimationsWindow(int x, int y, int w, int h, AnimationsDisplayerDisplayHandler parent) {
		super(parent, x, y, w, h, ID);
		this.parent = parent;
		this.fields = new TextField[2];
		FocusHandler.INSTANCE.setLastFocus(getClass());
	}

	@Override
	public void initWidgets() {
		TextField field = new TextField("", engine.getSkin());
		field.setFocusTraversal(false);
		setWPos(field, 90, 50);

		fields[0] = field;
		
		Label title = new Label("Create Animation", engine.getSkin());
		setWPos(title, 90, 10);

		Label animationName = new Label("Name:", engine.getSkin());
		setWPos(animationName, 10, 50);

		Label animationDuration = new Label("Duration:", engine.getSkin());
		setWPos(animationDuration, 10, 90);

		TextField durationField = new TextField("", engine.getSkin());
		durationField.setFocusTraversal(false);
		setWPos(durationField, 90, 90);
		
		fields[1] = durationField;
		
		TextButton createAnimation = new TextButton("Create", engine.getSkin());
		createAnimation.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Animation anim = new Animation(field.getText(), 
						Integer.parseInt(durationField.getText()));
				engine.getModelDisplay().getModel().addAnimation(anim);
				parent.getList().addKey(new JgTypedListKey<Animation>(anim) {
					@Override
					public String getDisplayText() {
						return getType().name;
					}
				});
				Logger.getGlobal().info("Animation with name: " + field.getText() + " added");
				/*AnimationsWindow.this.engine.getModelDisplay().getModel()
					.addAnimation(new Animation(field.getText(), 
							Integer.parseInt(durationField.getText())));*/
				AnimationsWindow.this.destroy();
			}
		});

		createAnimation.setSize(100, 40);
		setWPos(createAnimation, 95, 130);
	}

	@Override
	public void onKeyDown(int keycode) {
		if(keycode == Input.Keys.TAB && FocusHandler.INSTANCE.getLastFocusedClass() 
				== getClass()){
			engine.setKeyboardFocus(nextField());
		}
	}
	
	private Actor nextField() {
		current++;
		if(current > fields.length-1) {
			current = 0;
		}
		return fields[current];
	}
	
}
