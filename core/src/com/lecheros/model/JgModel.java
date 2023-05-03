package com.lecheros.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.lecheros.animation.Animation;
import com.lecheros.animation.Animator;
import com.lecheros.animation.Keyframe;
import com.lecheros.animator.Engine;
import com.lecheros.display.widgets.JgTypedList.JgTypedListKey;

public class JgModel {

	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private Animator animator;
	private String modelFilePath;
	private String name;
	private List<ModelPart> parts;
	private LinkedHashMap<String, ModelPart> modelParts;
	private List<Animation> animations;

	public JgModel(String path, String name, boolean fillModelParts) {
		modelBatch = new ModelBatch();
		ObjLoader loader = new ObjLoader();
		modelFilePath = path;
		this.name = name;
		Logger.getGlobal().info("Path: " + path);
		model = loader.loadModel(Gdx.files.internal(path));
		instance = new ModelInstance(model);
		animator = new Animator();
		parts = new ArrayList<>();
		modelParts = new LinkedHashMap<>();
		animations = new ArrayList<>();
		if (fillModelParts) {
			for (MeshPart part : model.meshParts) {
				// Logger.getGlobal().info("Model Part id: " + part.id);
				ModelPart modelPart = new ModelPart(part, part.id);
				modelParts.put(part.id, modelPart);
			}
			parts = new ArrayList<>(modelParts.values());
		}
	}

	public void tick() {
		animator.update();
		for(ModelPart part : parts) {
			part.synchronize();
		}
		/*
		 * for(Node part : model.nodes) {
		 * part.globalTransform.translate((float)Math.cos(angle)*0.1f, 0, 0); }
		 */
	}

	public void render(PerspectiveCamera cam) {
		modelBatch.begin(cam);
		modelBatch.render(instance);
		modelBatch.end();
	}

	public void dispose() {
		modelBatch.dispose();
	}
	
	public void updateParts() {
		parts = new ArrayList<>(modelParts.values());
	}
	
	public void addModelPart(ModelPart part) {
		parts.add(part);
		modelParts.put(part.getName(), part);
	}
	
	public ModelPart getModelPart(String name) {
		return modelParts.get(name);
	}

	public void updateKeyframesFromAnimation(Animation animation) {
		int dur = 0;
		for (Keyframe kf : animation.getKeyframes()) {
			kf.startTick = dur;
			kf.startVisualTick = kf.startTick + kf.dur;
			dur += kf.dur;
		}
		animation.dur = dur;
	}

	public void addAnimation(Animation animation) {
		animations.add(animation);
	}

	public void setAnimation(Animation animation) {
		animator.setAnimation(animation);
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public Model getModel() {
		return model;
	}

	public ModelInstance getInstance() {
		return instance;
	}

	public Animator getAnimator() {
		return animator;
	}

	public String getModelFilePath() {
		return modelFilePath;
	}

	public String getName() {
		return name;
	}

	public List<ModelPart> getModelParts() {
		return parts;
	}

	public Map<String, ModelPart> getModelPartsMap() {
		return modelParts;
	}

	public List<Animation> getAnimations() {
		return animations;
	}

	public void setAnimations(List<Animation> animations) {
		this.animations = animations;
	}

}
