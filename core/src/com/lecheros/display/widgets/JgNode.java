package com.lecheros.display.widgets;

import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public class JgNode extends Tree.Node<Node, String, TextButtonTest>{
	
	public JgNode (String text, Skin skin) {
		super(new TextButtonTest(text, skin));
		this.getActor().node = this;
		setValue(text);
	}
	
	public void mouseDown(int x, int y) {
		Vector2 mouse = new Vector2(x, Gdx.graphics.getHeight()-y);
		getActor().stageToLocalCoordinates(mouse);
		//Logger.getGlobal().info("Mouse: " + mouse.toString());
		/*y = Gdx.graphics.getHeight()-y;
		TextButton actor = getActor();
		float ax = actor.getX();
		float ay = actor.getY();
		java.util.logging.Logger.getGlobal().info("x: " + ax + " y: " + Math.abs(ay) + " mouseX: " + x + 
				" mouseY: " + y + " ay > y: " + 
		(Math.abs(ay) > y) + " ay + actor.getHeight() < y: " + (Math.abs(ay) + actor.getHeight() < y));
		if(ax < x && ax + actor.getWidth() > x &&
				Math.abs(ay) > y && Math.abs(ay) + actor.getHeight() < y) {
			java.util.logging.Logger.getGlobal().info("sd");
		}*/
	}
	
}
