package com.lecheros.animator;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lecheros.animation.AnimationHandler;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.DisplayLayersManager;
import com.lecheros.display.handler.IDisplayHandlerClickListener;
import com.lecheros.display.handler.IDisplayHandlerEventListener;
import com.lecheros.display.handlers.AnimationStuffDisplayHandler;
import com.lecheros.display.handlers.AnimationsDisplayerDisplayHandler;
import com.lecheros.display.handlers.BackgroundDisplayHandler;
import com.lecheros.display.handlers.ModelDisplayHandler;
import com.lecheros.display.handlers.ModelPropertiesDisplayHandler;
import com.lecheros.display.handlers.PartsSelectionDisplayHandler;
import com.lecheros.display.handlers.TopOptionsDisplayHandler;
import com.lecheros.display.handlers.WindowHandler;
import com.lecheros.display.parts.IDisplayPart;
import com.lecheros.display.widgets.ListButton;
import com.lecheros.model.JgModel;

public class Engine extends Stage implements EventListener {

	SpriteBatch batch;

	BitmapFont font;

	ShapeRenderer sr;

	PerspectiveCamera cam;

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

	// Widgets

	private java.util.List<ADisplayHandler> handlers;

	private java.util.List<TextButton> buttons;

	private java.util.List<ListButton<String>> boxes;

	private java.util.List<TextField> fields;

	private java.util.List<Label> labels;

	private java.util.List<IDisplayPart> parts;

	DisplayLayersManager layersManager;

	WindowHandler windowHandler;

	AnimationHandler animHandler;

	AnimationsDisplayerDisplayHandler animDisplay;
	AnimationStuffDisplayHandler animStuff;
	BackgroundDisplayHandler background;
	ModelDisplayHandler modelDisplay;
	ModelPropertiesDisplayHandler modelProperties;
	PartsSelectionDisplayHandler partsSelection;
	TopOptionsDisplayHandler topOptions;

	public static final boolean[] keys = new boolean[300];

	public Engine() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), true);
		sr = new ShapeRenderer();
		handlers = new ArrayList<>();

		ortho = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ortho.setToOrtho(true);
		uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ortho);

		ui = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		uiView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ui);
		ui.update();

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		viewPortX = (Gdx.graphics.getDisplayMode().width / 2) - (viewPortWidth / 2);

		Gdx.input.setInputProcessor(this);

		initWidgets();
		
		
	}

	public void initWidgets() {
		buttons = new ArrayList();
		boxes = new ArrayList<>();
		fields = new ArrayList<>();
		labels = new ArrayList<>();
		parts = new ArrayList<>();

		animHandler = new AnimationHandler();

		windowHandler = new WindowHandler();
		/*
		 * windowHandler.addWindow(new AnimationsWindow(200, 500, 300, 160));
		 * windowHandler.initWidets(this);
		 */

		layersManager = new DisplayLayersManager();

		int z = 0;
		layersManager.addPart(background = new BackgroundDisplayHandler(z++));
		layersManager.addPart(
				modelDisplay = new ModelDisplayHandler(z++, viewPortWidth, viewPortHeight, this));
		layersManager.addPart(partsSelection = new PartsSelectionDisplayHandler(z++, this));
		layersManager.addPart(animDisplay = new AnimationsDisplayerDisplayHandler(z++, this));
		layersManager.addPart(modelProperties = new ModelPropertiesDisplayHandler(z++, this));
		layersManager.addPart(topOptions = new TopOptionsDisplayHandler(z++, this, skin));
		layersManager.addPart(animStuff = new AnimationStuffDisplayHandler(z++, this));

		/*
		 * Labels Labels 0 - 2 are from PropertiesModifier
		 * 
		 */

		/*
		 * Fields Fields 0 - 2 are from PropertiesModifier
		 * 
		 */

		// parts.add(new PropertiesModifierPart(this, 100, 60));

		TextButton file = new TextButton("File", skin);
		file.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boxes.get(0).setVisible(!boxes.get(0).isVisible());
			}
		});
		buttons.add(file);

		//////////////////
		/*
		 * upperOptions = new Table(skin);
		 * upperOptions.setWidth(Gdx.graphics.getWidth());
		 * upperOptions.align(Align.center); upperOptions.setPosition(0,
		 * Gdx.graphics.getHeight()-file.getHeight()); upperOptions.add(file);
		 * 
		 * HorizontalGroup hor = new HorizontalGroup();
		 * 
		 * //upperOptions.add(tree).fill().expand();
		 * 
		 * final ListButton<String> box = new ListButton<>(skin, "File");
		 * box.addListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ChangeEvent event, Actor actor) {
		 * if(box.getSelected().equals("Import")) { System.out.println("Importing"); } }
		 * }); box.setWidth(100); box.setItems("Save", "Export", "Import");
		 * boxes.add(box); //addActor(box); hor.addActor(box);
		 * 
		 * ListButton<String> box2 = new ListButton<>(skin, "Config");
		 * box2.addListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ChangeEvent event, Actor actor) {
		 * if(box.getSelected().equals("Import")) { System.out.println("Importing"); } }
		 * }); box2.setWidth(100); box2.setItems("Keyboard");
		 * box2.setAlignment(Align.right); boxes.add(box2); //addActor(box2);
		 * hor.addActor(box2);
		 * 
		 * hor.setPosition(0, Gdx.graphics.getHeight()-(box.getHeight()/2));
		 * 
		 * addActor(upperOptions); addActor(hor);
		 */
		///////////////

		/*
		 * jgTree = new JgTree(); jgTree.setPos(200, 200); JgTreeNode node = new
		 * JgTreeNode("Hola"); JgTreeNode seg = new JgTreeNode("segunda"); JgTreeNode
		 * ase = new JgTreeNode("ase"); jgTree.add(node); jgTree.add(ase); JgTreeNode
		 * child = new JgTreeNode("mi"); JgTreeNode para = new JgTreeNode("para");
		 * ase.add(child); ase.add(seg); seg.add(new JgTreeNode("parte"));
		 * seg.add(para); para.add(new JgTreeNode("mama")); seg.add(new
		 * JgTreeNode("los")); node.add(new JgTreeNode("que")); jgTree.add(new
		 * JgTreeNode("buen")); jgTree.add(new JgTreeNode("compa")); jgTree.add(new
		 * JgTreeNode("nero")); child.add(new JgTreeNode("crack"));
		 */

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				handleClick(event, x, y);
			}
		});
		
		addListener(this);
	}

	@Override
	public boolean handle(Event e) {
		for (ADisplayHandler handler : layersManager.getLayers()) {
			if (handler instanceof IDisplayHandlerEventListener) {
				((IDisplayHandlerEventListener<?>) handler).onEventFired(e);
			}
		}
		return false;
	}
	
	public void handleClick(InputEvent e, float x, float y) {
		for (ADisplayHandler handler : layersManager.getLayers()) {
			if (handler instanceof IDisplayHandlerClickListener) {
				((IDisplayHandlerClickListener<?>) handler).handleClick(e, x, y);
			}
		}
	}

	public void onNewModel(JgModel model) {
		for (ADisplayHandler handler : layersManager.getLayers()) {
			handler.onNewModel(model);
		}
	}

	public void onSaveModel(JgModel model) {
		for (ADisplayHandler handler : layersManager.getLayers()) {
			handler.onSaveModel(model);
		}
	}

	public void tick() {
		// angle++;
		act(Gdx.graphics.getDeltaTime());
		// System.out.println(model.getModel().meshParts.toString());
		/*
		 * model.getModel().meshParts.get(0).mesh.transform(new Matrix4().idt()
		 * .translate((float)Math.cos(angle)*0.1f, 0, 0));
		 */
		layersManager.tick();
		windowHandler.tick();
		// jgTree.tick();
	}

	public void render() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		uiViewport.setScreenBounds(0, 0, width, height);
		uiView.setScreenBounds(0, 0, width, height);

		sr.setProjectionMatrix(ortho.combined);
		batch.setProjectionMatrix(ortho.combined);

		layersManager.render(sr, font, batch);

		windowHandler.render(sr, font, batch);

		// Render model
		// modelViewport.apply();
		// model.render(cam);

		renderShapes();

		uiView.apply();
		draw();
		for (IDisplayPart part : parts) {
			part.render(sr, font, batch);
		}
		// jgTree.render(sr, font, batch);
	}

	public void renderShapes() {
		/*
		 * sr.begin(ShapeType.Line); sr.rect(0, 0, 400, 400); sr.end();
		 */
	}

	public void dispose() {
		super.dispose();
		layersManager.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		super.keyDown(keycode);
		keys[keycode] = true;
		windowHandler.onKeyDown(keycode);
		layersManager.onKeyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		super.keyUp(keycode);
		keys[keycode] = false;
		if (keys[Input.Keys.R] && keys[Input.Keys.SHIFT_LEFT]) {
			windowHandler.reset(this);
		}
		windowHandler.onKeyUp(keycode);
		layersManager.onKeyUp(keycode);
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
		layersManager.mouseDown(screenX, screenY, button);
		windowHandler.mouseDown(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		super.touchUp(screenX, screenY, pointer, button);
		layersManager.mouseUp(screenX, screenY, button);
		windowHandler.mouseUp(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		super.touchDragged(screenX, screenY, pointer);
		windowHandler.mouseDragged(screenX, screenY);
		layersManager.mouseDragged(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		super.mouseMoved(screenX, screenY);
		layersManager.mouseMoved(screenX, screenY);
		windowHandler.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		super.scrolled(amountX, amountY);
		layersManager.onScroll(amountY);
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

	public DisplayLayersManager getDisplayLayersManager() {
		return layersManager;
	}

	public WindowHandler getWindowHandler() {
		return windowHandler;
	}

	public AnimationHandler getAnimationsHandler() {
		return animHandler;
	}

	public AnimationsDisplayerDisplayHandler getAnimDisplay() {
		return animDisplay;
	}

	public AnimationStuffDisplayHandler getAnimStuff() {
		return animStuff;
	}

	public BackgroundDisplayHandler getBackground() {
		return background;
	}

	public ModelDisplayHandler getModelDisplay() {
		return modelDisplay;
	}

	public ModelPropertiesDisplayHandler getModelProperties() {
		return modelProperties;
	}

	public PartsSelectionDisplayHandler getPartsSelection() {
		return partsSelection;
	}

	public TopOptionsDisplayHandler getTopOptions() {
		return topOptions;
	}

}
