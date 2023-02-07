package com.lecheros.animator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lecheros.display.parts.PropertiesModifierPart;
import com.lecheros.display.widgets.JgNode;
import com.lecheros.display.widgets.JgTree;
import com.lecheros.display.widgets.JgTree.JgTreeNode;
import com.lecheros.display.widgets.ListButton;
import com.lecheros.models.JgModel;

public class Engine extends Stage {

	SpriteBatch batch;
	
	BitmapFont font;
	
	ShapeRenderer sr;
	
	PerspectiveCamera cam;
	FitViewport modelViewport;
	
	JgModel model;
	
	OrthographicCamera ortho;
	FitViewport uiViewport;
	
	OrthographicCamera ui;
	FitViewport uiView;
	
	float viewPortWidth = 500;
	float viewPortHeight = 500;
	float viewPortX = 0;
	float viewPortY = 0;
	float step = 1f;
	float angle = 0f;
	
	private Skin skin;
	
	//Widgets
	private TextField field;
	
	private Table upperOptions;
	
	private VerticalGroup vertGroup;
	private VerticalGroup labelsVertGroup;
	
	private Tree<JgNode, String> tree;
	
	private JgTree jgTree;
	
	private java.util.List<TextButton> buttons;
	
	private java.util.List<ListButton<String>> boxes;
	
	private java.util.List<TextField> fields;
	
	private java.util.List<Label> labels;
	
	public static final boolean[] keys = new boolean[300];
	
	public Engine() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), true);
		sr = new ShapeRenderer();
		
		cam = new PerspectiveCamera(67, viewPortWidth, viewPortHeight);//Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		modelViewport = new FitViewport(viewPortWidth, viewPortHeight, cam);
		
		model = new JgModel("w.obj");
		
		ortho = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ortho.setToOrtho(true);
		uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 
				ortho);
		
		ui = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		uiView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 
				ui);
		ui.update();
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		
		viewPortX = (Gdx.graphics.getDisplayMode().width/2)-(viewPortWidth/2);
		
		Gdx.input.setInputProcessor(this);
		
		initWidgets();
		
	}
	
	public void initWidgets() {
		buttons = new ArrayList();
		boxes = new ArrayList<>();
		fields = new ArrayList<>();
		labels = new ArrayList<>();
		
		/* Labels 
		Labels 0 - 2 are from PropertiesModifier 
		
		*/
		
		/* Fields 
		Fields 0 - 2 are from PropertiesModifier 
		
		*/
		
		new PropertiesModifierPart(this, 100, 60);
		
		tree = new Tree<JgNode, String>(skin);
		//tree.setPadding(10);
		//tree.setIndentSpacing(25);
		//tree.setIconSpacing(5, 0);
		tree.setPosition(200, 200);
		JgNode test = new JgNode("Test", skin);
		JgNode test2 = new JgNode("Test", skin);
		tree.add(test);
		test.add(test2);
		test2.add(new JgNode("Test2", skin));
		tree.add(new JgNode("Test3", skin));
		tree.add(new JgNode("Test4", skin));
		addActor(tree);
		
		TextButton file = new TextButton("File", skin);
		file.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boxes.get(0).setVisible(!boxes.get(0).isVisible());
			}
		});
		buttons.add(file);
		
		upperOptions = new Table(skin);
		upperOptions.setWidth(Gdx.graphics.getWidth());
		upperOptions.align(Align.center);
		upperOptions.setPosition(0, Gdx.graphics.getHeight()-file.getHeight());
		upperOptions.add(file);
		
		HorizontalGroup hor = new HorizontalGroup();
		
		//upperOptions.add(tree).fill().expand();
		
		final ListButton<String> box = new ListButton<>(skin, "File");
		box.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(box.getSelected().equals("Import")) {
					System.out.println("Importing");
				}
			}
		});
		box.setWidth(100);
		box.setItems("Save", "Export", "Import");
		boxes.add(box);
		//addActor(box);
		hor.addActor(box);
		
		ListButton<String> box2 = new ListButton<>(skin, "Config");
		box2.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(box.getSelected().equals("Import")) {
					System.out.println("Importing");
				}
			}
		});
		box2.setWidth(100);
		box2.setItems("Keyboard");
		box2.setAlignment(Align.right);
		boxes.add(box2);
		//addActor(box2);
		hor.addActor(box2);
		
		hor.setPosition(0, Gdx.graphics.getHeight()-(box.getHeight()/2));
		
		addActor(upperOptions);
		addActor(hor);
		
		jgTree = new JgTree();
		jgTree.setPos(200, 200);
		JgTreeNode node = new JgTreeNode("Hola");
		JgTreeNode seg = new JgTreeNode("segunda");
		JgTreeNode ase = new JgTreeNode("ase");
		jgTree.add(node);
		jgTree.add(ase);
		JgTreeNode child = new JgTreeNode("mi");
		JgTreeNode para = new JgTreeNode("para");
		ase.add(child);
		ase.add(seg);
		seg.add(new JgTreeNode("parte"));
		seg.add(para);
		para.add(new JgTreeNode("mama"));
		seg.add(new JgTreeNode("los"));
		node.add(new JgTreeNode("que"));
		jgTree.add(new JgTreeNode("buen"));
		jgTree.add(new JgTreeNode("compa"));
		jgTree.add(new JgTreeNode("nero"));
		child.add(new JgTreeNode("crack"));
	}
	
	public void tick() {
		angle++;
		act(Gdx.graphics.getDeltaTime());
		//System.out.println(model.getModel().meshParts.toString());
		model.getModel().meshParts.get(0).mesh.transform(new Matrix4().idt()
				.translate((float)Math.cos(angle)*0.1f, 0, 0));
		jgTree.tick();
	}
	
	public void render() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		modelViewport.setScreenBounds(0, 0, width, height);
		uiViewport.setScreenBounds(0, 0, width, height);
		uiView.setScreenBounds(0, 0, width, height);
		
		sr.setProjectionMatrix(ortho.combined);
		batch.setProjectionMatrix(ortho.combined);
		
		// Render model
		modelViewport.apply();
		model.render(cam);
		
		renderShapes();
		
		uiView.apply();
		draw();
		jgTree.render(sr, font, batch);
	}
	
	public void renderShapes() {
		sr.begin(ShapeType.Line);
		sr.rect(0, 0, 400, 400);
		sr.end();
	}
	
	public void dispose() {
		super.dispose();
		model.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		super.keyDown(keycode);
		keys[keycode] = true;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		super.keyUp(keycode);
		keys[keycode] = false;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		super.keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
		jgTree.mouseDown(screenX, screenY);
		Vector2 point = new Vector2(screenX, screenY);
		// Me quede haciendo que funcionara el tree widget
		for(JgNode btn : tree.getNodes()) {
			btn.mouseDown(screenX, screenY);
			//Vector2 mouse = new Vector2(screenX, Gdx.graphics.getHeight()-screenY);
			//btn.getActor().stageToLocalCoordinates(mouse);
			//Logger.getGlobal().info(mouse.toString());
		}
		for(Actor child : tree.getChildren().items) {
			if(child != null) {
				Vector2 mouse = new Vector2(screenX, Gdx.graphics.getHeight()-screenY);
				Vector2 newVec = child.parentToLocalCoordinates(point.set(screenX, 
						Gdx.graphics.getHeight()-screenY));
				//Logger.getGlobal().info("newVec: " + newVec.toString());
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		super.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		super.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		super.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		super.scrolled(amountX, amountY);
		return false;
	}

	public java.util.List<TextButton> getButtons() {
		return buttons;
	}

	public java.util.List<ListButton<String>> getBoxes() {
		return boxes;
	}

	public java.util.List<TextField> getFields() {
		return fields;
	}

	public java.util.List<Label> getLabels() {
		return labels;
	}

	public Skin getSkin() {
		return skin;
	}
	
}
