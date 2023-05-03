package com.lecheros.display.parts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.lecheros.animator.Engine;
import com.lecheros.display.handlers.ModelPropertiesDisplayHandler;
import com.lecheros.display.widgets.JgTextField;

public class PropertiesModifierPart implements IDisplayPart {

	private Vector2 pos;
	private VerticalGroup vertTGroup;
	private VerticalGroup labelsTVertGroup;

	private VerticalGroup vertRGroup;
	private VerticalGroup labelsRVertGroup;

	private JgTextField[] fields;
	private Label[] labels;

	public PropertiesModifierPart(Engine engine, float x, float y) {
		this.pos = new Vector2(x, y);

		fields = new JgTextField[6];
		labels = new Label[6];

		int tYOffset = 140;
		int rYOffset = 0;

		// Translation

		labels[0] = new Label("X", engine.getSkin());
		labels[1] = new Label("Y", engine.getSkin());
		labels[2] = new Label("Z", engine.getSkin());

		labelsTVertGroup = new VerticalGroup();
		labelsTVertGroup.align(Align.center);
		labelsTVertGroup.space(8);
		for (Label label : new Label[] { labels[0], labels[1], labels[2] }) {
			labelsTVertGroup.addActor(label);
		}
		labelsTVertGroup.setPosition(pos.x - 92, pos.y + tYOffset);
		engine.addActor(labelsTVertGroup);

		engine.getLabels().add(labels[0]);
		engine.getLabels().add(labels[1]);
		engine.getLabels().add(labels[2]);

		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.TX] = new JgTextField(ModelPropertiesDisplayHandler.TX, "",
						engine.getSkin()));
		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.TY] = new JgTextField(ModelPropertiesDisplayHandler.TY, "",
						engine.getSkin()));
		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.TZ] = new JgTextField(ModelPropertiesDisplayHandler.TZ, "",
						engine.getSkin()));

		vertTGroup = new VerticalGroup();
		vertTGroup.align(Align.center);
		for (JgTextField field : new JgTextField[] { fields[0], fields[1], fields[2] }) {
			vertTGroup.addActor(field);
		}
		vertTGroup.setPosition(pos.x - 6, pos.y + tYOffset);
		engine.addActor(vertTGroup);

		// Rotation

		labels[3] = new Label("RX ", engine.getSkin());
		labels[4] = new Label("RY ", engine.getSkin());
		labels[5] = new Label("RZ ", engine.getSkin());

		labelsRVertGroup = new VerticalGroup();
		labelsRVertGroup.align(Align.center);
		labelsRVertGroup.space(8);
		for (Label label : new Label[] { labels[3], labels[4], labels[5] }) {
			labelsRVertGroup.addActor(label);
		}
		labelsRVertGroup.setPosition(pos.x - 94, pos.y + rYOffset);
		engine.addActor(labelsRVertGroup);

		engine.getLabels().add(labels[3]);
		engine.getLabels().add(labels[4]);
		engine.getLabels().add(labels[5]);

		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.RX] = new JgTextField(ModelPropertiesDisplayHandler.RX, "",
						engine.getSkin()));
		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.RY] = new JgTextField(ModelPropertiesDisplayHandler.RY, "",
						engine.getSkin()));
		engine.getFields()
				.add(fields[ModelPropertiesDisplayHandler.RZ] = new JgTextField(ModelPropertiesDisplayHandler.RZ, "",
						engine.getSkin()));

		vertRGroup = new VerticalGroup();
		vertRGroup.align(Align.center);
		for (JgTextField field : new JgTextField[] { fields[3], fields[4], fields[5] }) {
			vertRGroup.addActor(field);
		}
		vertRGroup.setPosition(pos.x - 6, pos.y + rYOffset);
		engine.addActor(vertRGroup);
		
		for(JgTextField f : fields) {
			f.setFocusTraversal(false);
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {

	}

	public void clear() {
		setX(0);
		setY(0);
		setZ(0);
		setRX(0);
		setRY(0);
		setRZ(0);
	}
	
	public void setX(float x) {
		fields[ModelPropertiesDisplayHandler.TX].setText(String.valueOf(x));
	}

	public void setY(float y) {
		fields[ModelPropertiesDisplayHandler.TY].setText(String.valueOf(y));
	}

	public void setZ(float z) {
		fields[ModelPropertiesDisplayHandler.TZ].setText(String.valueOf(z));
	}

	public void setRX(float rx) {
		fields[ModelPropertiesDisplayHandler.RX].setText(String.valueOf(rx));
	}

	public void setRY(float ry) {
		fields[ModelPropertiesDisplayHandler.RY].setText(String.valueOf(ry));
	}

	public void setRZ(float rz) {
		fields[ModelPropertiesDisplayHandler.RZ].setText(String.valueOf(rz));
	}
	
	public JgTextField[] getFields() {
		return fields;
	}

}
