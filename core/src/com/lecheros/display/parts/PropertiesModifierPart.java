package com.lecheros.display.parts;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.lecheros.animator.Engine;

public class PropertiesModifierPart {

	private Vector2 pos;
	private VerticalGroup vertGroup;
	private VerticalGroup labelsVertGroup;
	
	public PropertiesModifierPart(Engine engine, float x, float y) {
		this.pos = new Vector2(x, y);
		engine.getLabels().add(new Label("X", engine.getSkin()));
		engine.getLabels().add(new Label("Y", engine.getSkin()));
		engine.getLabels().add(new Label("Z", engine.getSkin()));
		
		labelsVertGroup = new VerticalGroup();
		labelsVertGroup.align(Align.center);
		labelsVertGroup.space(8);
		for(Label label  : engine.getLabels()) {
			labelsVertGroup.addActor(label);
		}
		labelsVertGroup.setPosition(pos.x-88, pos.y);
		engine.addActor(labelsVertGroup);
		
		engine.getFields().add(new TextField("", engine.getSkin()));
		engine.getFields().add(new TextField("", engine.getSkin()));
		engine.getFields().add(new TextField("", engine.getSkin()));
		
		vertGroup = new VerticalGroup();
		vertGroup.align(Align.center);
		for(TextField field  : engine.getFields()) {
			vertGroup.addActor(field);
		}
		vertGroup.setPosition(pos.x, pos.y);
		engine.addActor(vertGroup);
	}
	
}
