package com.lecheros.display.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.lecheros.animation.serializer.ModelSerializer;
import com.lecheros.animator.Engine;
import com.lecheros.display.ADisplayHandler;
import com.lecheros.display.handler.IDisplayHandlerEventListener;
import com.lecheros.display.widgets.JgTextField;
import com.lecheros.display.widgets.ListButton;
import com.lecheros.display.widgets.windows.TransformModifierWindow;
import com.lecheros.model.JgModel;
import com.lecheros.utils.FileUtils;
import com.lecheros.utils.WindowUtils;

public class TopOptionsDisplayHandler extends ADisplayHandler 
		implements IDisplayHandlerEventListener<TopOptionsDisplayHandler>{

	private Table upperOptions;
	private boolean importModel;
	private boolean isObj;
	private String path;
	private String name;
	private String data;
	private Engine engine;
	private Thread thread;

	public TopOptionsDisplayHandler(int zindex, Engine engine, Skin skin) {
		super(zindex);
		upperOptions = new Table(skin);
		upperOptions.setWidth(Gdx.graphics.getWidth());
		upperOptions.align(Align.center);

		this.engine = engine;

		HorizontalGroup hor = new HorizontalGroup();

		final ListButton<String> box = new ListButton<>(skin, "File");
		box.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (box.getSelected().equals("Import")) {
					System.out.println("Importing");
					importModel(engine);
					System.out.println("Finished Importing");
				} else if (box.isOpen() && box.getSelected().equals("Save")) {
					if (engine.getModelDisplay().getModel() != null) {
						Logger.getGlobal().info("Saving model");
						saveModel(engine);
					} else {
						Logger.getGlobal().info("No Model");
					}
				}
			}
		});

		box.setWidth(100);
		box.setItems("Save", "Export", "Import");
		engine.getBoxes().add(box);
		// addActor(box);
		hor.addActor(box);

		ListButton<String> box2 = new ListButton<>(skin, "Config");
		box2.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (box2.getSelected().equals("Import")) {
					System.out.println("Importing");
				}
			}
		});
		box2.setWidth(100);
		box2.setItems("Keyboard");
		box2.setAlignment(Align.right);
		engine.getBoxes().add(box2);
		hor.addActor(box2);
		
		ListButton<String> showWindows = new ListButton<>(skin, "Windows");
		showWindows.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (showWindows.isOpen() && showWindows.getSelected().equals("Transform Modifier")) {
					WindowUtils.createTransformWindow(engine, TopOptionsDisplayHandler.this, 
							200, 400);
					/*engine.getWindowHandler().addWindow(engine, 
							new TransformModifierWindow<TopOptionsDisplayHandler>
								(TopOptionsDisplayHandler.this, 200, 400, 300, 300));*/
					System.out.println("Showing transform modifier window");
				}
			}
		});
		showWindows.setWidth(100);
		showWindows.setItems("Transform Modifier");
		showWindows.setAlignment(Align.right);
		engine.getBoxes().add(showWindows);
		hor.addActor(showWindows);

		hor.setPosition(0, Gdx.graphics.getHeight() - (box.getHeight() / 2));

		engine.addActor(upperOptions);
		engine.addActor(hor);

		upperOptions.setPosition(0, Gdx.graphics.getHeight() - box2.getHeight());

		Logger.getGlobal().info("Loading creeper test model from TopOptionsDisplayerHandler constructor line 112");
		String data = FileUtils.readFile(new File("C:\\Users\\ivaan\\OneDrive\\Documentos\\creeper2.jgm.jgm"));
		TopOptionsDisplayHandler.this.data = data;
		TopOptionsDisplayHandler.this.isObj = false;
		TopOptionsDisplayHandler.this.importModel = true;
	}

	@Override
	public void tick() {
		if (importModel) {
			Logger.getGlobal().info("sdsdsdsd");
			JgModel model = null;
			if (isObj) {
				model = new JgModel(path, name, true);
			} else {
				model = ModelSerializer.deserialize(data);
			}
			engine.onNewModel(model);
			importModel = false;
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		Color color = sr.getColor();
		sr.setColor(0.3f, 0.3f, 0.3f, 1.0f);
		sr.begin(ShapeType.Filled);
		sr.rect(0, 0, Gdx.graphics.getWidth(), 32);
		sr.end();
		sr.setColor(color);
	}

	@Override
	public void dispose() {

	}

	public void importModel(Engine engine) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				JFrame f = new JFrame();
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = chooser.showOpenDialog(f);
				f.dispose();
				if (res == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();

					String[] fNameData = file.getName().split("\\.");

					if (fNameData[fNameData.length - 1].equals("obj")) {
						TopOptionsDisplayHandler.this.path = file.getAbsolutePath();
						TopOptionsDisplayHandler.this.name = file.getName();
						TopOptionsDisplayHandler.this.isObj = true;
						TopOptionsDisplayHandler.this.importModel = true;
						TopOptionsDisplayHandler.this.finishThread();
					} else if (fNameData[fNameData.length - 1].equals("jgm")) {
						String data = FileUtils.readFile(chooser.getSelectedFile());
						TopOptionsDisplayHandler.this.data = data;
						TopOptionsDisplayHandler.this.isObj = false;
						TopOptionsDisplayHandler.this.importModel = true;
						TopOptionsDisplayHandler.this.finishThread();
					} else {
						Logger.getGlobal().info("Incompatible file type");
					}
				}
			}
		});
		thread.start();
	}

	private void finishThread() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void saveModel(Engine engine) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				JFrame f = new JFrame();
				f.setVisible(true);
				f.toFront();
				f.setVisible(false);
				int res = chooser.showSaveDialog(f);
				f.dispose();
				if (res == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().getAbsolutePath() + ".jgm");

					String model = ModelSerializer.serialize(engine.getModelDisplay().getModel());
					if (!file.exists()) {
						BufferedWriter bfw;
						try {
							file.createNewFile();
							bfw = new BufferedWriter(new FileWriter(file));
							bfw.write(model);
							bfw.close();
							Logger.getGlobal().info("File Created Succesfully");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						BufferedWriter bfw;
						try {
							bfw = new BufferedWriter(new FileWriter(file));
							bfw.write(model);
							bfw.close();
							Logger.getGlobal().info("File Saved Succesfully");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					engine.onSaveModel(engine.getModelDisplay().getModel());
				} else if (res == JFileChooser.CANCEL_OPTION) {
					Logger.getGlobal().info("Canceling");
				}
			}
		}).start();
	}

	@Override
	public void onNewModel(JgModel model) {
		Logger.getGlobal().info("On New Model");
	}

	@Override
	public void onSaveModel(JgModel model) {

	}

	@Override
	public void onEventFired(Event e) {
		if(e instanceof ChangeEvent) {
			if(e.getTarget() instanceof ListButton || e.getTarget() instanceof JgTextField) {
				TransformModifierWindow<?> window = ((TransformModifierWindow<?>)engine
						.getWindowHandler().getWindow(TransformModifierWindow.ID));
				if(window != null) {
					window.onEventFired(e);
					Logger.getGlobal().info("Window On Event Fired");
				}
			}
		}
	}

}
