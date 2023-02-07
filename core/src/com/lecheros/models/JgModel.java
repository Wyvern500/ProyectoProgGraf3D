package com.lecheros.models;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.lecheros.animation.Animation;
import com.lecheros.animation.Animator;
import com.lecheros.animation.Keyframe;

public class JgModel {

	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private Animator animator;
	private List<ModelPart> parts;
	
	public JgModel(String path) {
		modelBatch = new ModelBatch();
		ObjLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal(path));
		instance = new ModelInstance(model);
		parts = new ArrayList<>();
		for(MeshPart part : model.meshParts) {
			parts.add(new ModelPart(part.id));
		}
		
	}
	
	public void tick() {
		animator.update();
		for(Node part : model.nodes) {
			//part.globalTransform.translate((float)Math.cos(angle)*0.1f, 0, 0);
		}
	}
	
	public void render(PerspectiveCamera cam) {
		modelBatch.begin(cam);
		modelBatch.render(instance);
		modelBatch.end();
	}
	
	public void dispose() {
		modelBatch.dispose();
	}
	
	public void updateKeyframesFromAnimation(Animation animation) {
		int dur = 0;
		for(Keyframe kf : animation.keyframes) {
			kf.startTick = dur;
			kf.startVisualTick = kf.startTick + kf.dur;
			dur += kf.dur;
		}
		animation.dur = dur;
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
	
	public List<ModelPart> getModelParts(){
		return parts;
	}
	
}
