package com.lecheros.animation;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.lecheros.models.JgModel;
import com.lecheros.models.ModelPart;

public class Animator {
	
	public int prevStartTick;
	public int prevDur;
	public int current;
	public float tick;
	public float prog;
	public float prevTick;
	
	public float pro;
	public float pre;
	public float MAX;
	
	public boolean test;
	public boolean play;
	
	public Animation animation;
	
	public JgModel model;
	
	Map<ModelPart, float[]> currentTranslations;
	Map<ModelPart, float[]> currentRotations;
	Map<ModelPart, float[]> prevTranslations;
	Map<ModelPart, float[]> prevRotations;
	
	public Animator(JgModel model, boolean test) {
		this.model = model;
		currentTranslations = new HashMap<>();
		currentRotations = new HashMap<>();
		prevTranslations = new HashMap<>();
		prevRotations = new HashMap<>();
		for(int i = 0; i < model.getModelParts().size(); i++){
            this.prevTranslations.put(model.getModelParts().get(i), 
            		new float[] { 0, 0, 0 });
            this.prevRotations.put(model.getModelParts().get(i), 
                new float[] { 0, 0, 0 });
        }
		animation = null;
		this.test = test;
	}

	public void setAnimation(Animation animation) {
		current = 0;
		this.animation = animation;
		this.tick = 0;
		this.prevTick = 0;
		this.prog = 0;
		if(!animation.keyframes.isEmpty()) {
			currentTranslations = animation.keyframes.get(current).translations;
			currentRotations = animation.keyframes.get(current).rotations;
			this.prevDur = 0;
		}
	}
	
	public void playAnim() {
		this.play = !play;
	}
	
	public void update() {
		if(this.animation != null && play){
            this.prevTick = this.tick;
            this.tick += Gdx.graphics.getDeltaTime();
            Keyframe kf = this.animation.keyframes.get(current);
            if(this.tick-kf.startTick > kf.dur){
                this.tick = kf.startTick + kf.dur;
                this.currentTranslations = kf.translations;
                this.currentRotations = kf.rotations;
                this.prevStartTick = kf.startTick;
            }
            float prosTick = this.tick - kf.startTick;
            float prosPrevTick = this.prevTick - kf.startTick;
            this.prog = (prosPrevTick + (prosTick - prosPrevTick) * Gdx.graphics.getDeltaTime()) / kf.dur;
            this.pro = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
            if(this.tick-kf.startTick == kf.dur){
                this.prog = 1.0f;
            }
            for(ModelPart part : currentTranslations.keySet()) {
            	if(!prevTranslations.containsKey(part)) {
            		prevTranslations.put(part, new float[] { part.getTransform().offsetX,
            				part.getTransform().offsetY, part.getTransform().offsetZ });
            	}
            	float[] part1 = this.prevTranslations.get(part);
            	float[] part2 = this.currentTranslations.get(part);
            	try {
            	part.getTransform().offsetX = MathUtils.lerp(part1[0], 
            			part2[0], this.prog);
            	part.getTransform().offsetY = MathUtils.lerp(this.prevTranslations
            			.get(part)[1], this.currentTranslations.get(part)[1], this.prog);
            	part.getTransform().offsetZ = MathUtils.lerp(this.prevTranslations
            			.get(part)[2], this.currentTranslations.get(part)[2], this.prog);
            	} catch (NullPointerException e) {
					e.printStackTrace();
				}
            }
            for(ModelPart part : currentRotations.keySet()) {
            	if(!prevRotations.containsKey(part)) {
            		prevRotations.put(part, new float[] { part.getTransform().rotationX,
            				part.getTransform().rotationY, part.getTransform().rotationZ });
            	}
            	float[] part1 = this.prevRotations.get(part);
            	float[] part2 = this.currentRotations.get(part);
            	try {
            	part.getTransform().rotationX = MathUtils.lerpAngle(part1[0], 
            			part2[0], this.prog);
            	part.getTransform().rotationY = MathUtils.lerpAngle(this.prevRotations
            			.get(part)[1], this.currentRotations.get(part)[1], this.prog);
            	part.getTransform().rotationZ = MathUtils.lerpAngle(this.prevRotations
            			.get(part)[2], this.currentRotations.get(part)[2], this.prog);
            	} catch (NullPointerException e) {
					e.printStackTrace();
				}
            }
            if(this.tick-kf.startTick >= kf.dur){
                this.current++;
                if(this.current+1 > this.animation.keyframes.size()){
                    this.animation = null;
                    this.play = false;
                    return;
                }
                this.updateCurrentMaps();
            }
        } else if(animation != null && !play){
        	Keyframe kf = this.animation.keyframes.get(current);
        	if(this.tick-kf.startTick > kf.dur){
                this.currentTranslations = kf.translations;
                this.currentRotations = kf.rotations;
                this.prevStartTick = kf.startTick;
            }
        	float prosTick = this.tick - kf.startTick;
            float prosPrevTick = this.prevTick - kf.startTick;
        	this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
        	if(this.tick-kf.startTick == kf.dur){
                this.prog = 1.0f;
            }
            for(ModelPart part : currentTranslations.keySet()) {
            	if(!prevTranslations.containsKey(part)) {
            		prevTranslations.put(part, new float[] { part.getTransform().offsetX,
            				part.getTransform().offsetY, part.getTransform().offsetZ });
            	}
            	float[] part1 = this.prevTranslations.get(part);
            	float[] part2 = this.currentTranslations.get(part);
            	try {
            	part.getTransform().offsetX = MathUtils.lerp(part1[0], 
            			part2[0], this.prog);
            	part.getTransform().offsetY = MathUtils.lerp(this.prevTranslations
            			.get(part)[1], this.currentTranslations.get(part)[1], this.prog);
            	part.getTransform().offsetZ = MathUtils.lerp(this.prevTranslations
            			.get(part)[2], this.currentTranslations.get(part)[2], this.prog);
            	} catch (NullPointerException e) {
					e.printStackTrace();
				}
            }
            for(ModelPart part : currentRotations.keySet()) {
            	if(!prevRotations.containsKey(part)) {
            		prevRotations.put(part, new float[] { part.getTransform().rotationX,
            				part.getTransform().rotationY, part.getTransform().rotationZ });
            	}
            	float[] part1 = this.prevRotations.get(part);
            	float[] part2 = this.currentRotations.get(part);
            	try {
            	part.getTransform().rotationX = MathUtils.lerpAngle(part1[0], 
            			part2[0], this.prog);
            	part.getTransform().rotationY = MathUtils.lerpAngle(this.prevRotations
            			.get(part)[1], this.currentRotations.get(part)[1], this.prog);
            	part.getTransform().rotationZ = MathUtils.lerpAngle(this.prevRotations
            			.get(part)[2], this.currentRotations.get(part)[2], this.prog);
            	} catch (NullPointerException e) {
					e.printStackTrace();
				}
            }
            if(this.tick-kf.startTick >= kf.dur){
                this.current++;
                if(this.current+1 > this.animation.keyframes.size()){
                    this.animation = null;
                    this.play = false;
                    return;
                }
                this.updateCurrentMaps();
            } else if(this.tick-kf.startTick <= -1){
            	this.current--;
            	if(this.current < 0) {
            		this.current = 0;
            	}
            	this.updateCurrentMaps();
            }
        }
	}
	
	public void updateCurrentMaps() {
		this.prevTranslations = this.currentTranslations;
        this.prevRotations = this.currentRotations;
        this.currentTranslations = this.animation.keyframes.get(current)
            .translations;
        this.currentRotations = this.animation.keyframes.get(current)
            .rotations;
	}
	
	public void nextTick() {
		this.prevTick = this.tick;
        this.tick += 1;
		Keyframe kf = this.animation.keyframes.get(current);
    	if(this.tick-kf.startTick > kf.dur){
            this.currentTranslations = kf.translations;
            this.currentRotations = kf.rotations;
            this.prevStartTick = kf.startTick;
        }
    	float prosTick = this.tick - kf.startTick;
        float prosPrevTick = this.prevTick - kf.startTick;
    	this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
    	if(this.tick-kf.startTick == kf.dur){
            this.prog = 1.0f;
        }
        for(ModelPart part : currentTranslations.keySet()) {
        	if(!prevTranslations.containsKey(part)) {
        		prevTranslations.put(part, new float[] { part.getTransform().offsetX,
        				part.getTransform().offsetY, part.getTransform().offsetZ });
        	}
        	float[] part1 = this.prevTranslations.get(part);
        	float[] part2 = this.currentTranslations.get(part);
        	try {
        	part.getTransform().offsetX = MathUtils.lerp(part1[0], 
        			part2[0], this.prog);
        	part.getTransform().offsetY = MathUtils.lerp(this.prevTranslations
        			.get(part)[1], this.currentTranslations.get(part)[1], this.prog);
        	part.getTransform().offsetZ = MathUtils.lerp(this.prevTranslations
        			.get(part)[2], this.currentTranslations.get(part)[2], this.prog);
        	} catch (NullPointerException e) {
				e.printStackTrace();
			}
        }
        for(ModelPart part : currentRotations.keySet()) {
        	if(!prevRotations.containsKey(part)) {
        		prevRotations.put(part, new float[] { part.getTransform().rotationX,
        				part.getTransform().rotationY, part.getTransform().rotationZ });
        	}
        	float[] part1 = this.prevRotations.get(part);
        	float[] part2 = this.currentRotations.get(part);
        	try {
        	part.getTransform().rotationX = MathUtils.lerpAngle(part1[0], 
        			part2[0], this.prog);
        	part.getTransform().rotationY = MathUtils.lerpAngle(this.prevRotations
        			.get(part)[1], this.currentRotations.get(part)[1], this.prog);
        	part.getTransform().rotationZ = MathUtils.lerpAngle(this.prevRotations
        			.get(part)[2], this.currentRotations.get(part)[2], this.prog);
        	} catch (NullPointerException e) {
				e.printStackTrace();
			}
        }
        for(int i = 0; i < model.getAnimator().animation.keyframes.size(); i++) {
        	Keyframe kfi = model.getAnimator().animation.keyframes.get(i);
        	if(tick >= kfi.startTick && tick <= kfi.startTick + kf.dur) {
        		this.current = i;
        	}
        }
	}
	
	public void prevTick() {
		this.prevTick = this.tick;
        this.tick -= 1;
		Keyframe kf = this.animation.keyframes.get(current);
    	if(this.tick-kf.startTick > kf.dur){
            this.currentTranslations = kf.translations;
            this.currentRotations = kf.rotations;
            this.prevStartTick = kf.startTick;
        }
    	if(tick < 0) {
    		tick = 0;
    	}
    	float prosTick = this.tick - kf.startTick;
        float prosPrevTick = this.prevTick - kf.startTick;
    	this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
    	if(this.tick-kf.startTick == kf.dur){
            this.prog = 1.0f;
        }
        for(ModelPart part : currentTranslations.keySet()) {
        	if(!prevTranslations.containsKey(part)) {
        		prevTranslations.put(part, new float[] { part.getTransform().offsetX,
        				part.getTransform().offsetY, part.getTransform().offsetZ });
        	}
        	float[] part1 = this.prevTranslations.get(part);
        	float[] part2 = this.currentTranslations.get(part);
        	try {
        	part.getTransform().offsetX = MathUtils.lerp(part1[0], 
        			part2[0], this.prog);
        	part.getTransform().offsetY = MathUtils.lerp(this.prevTranslations
        			.get(part)[1], this.currentTranslations.get(part)[1], this.prog);
        	part.getTransform().offsetZ = MathUtils.lerp(this.prevTranslations
        			.get(part)[2], this.currentTranslations.get(part)[2], this.prog);
        	} catch (NullPointerException e) {
				e.printStackTrace();
			}
        }
        for(ModelPart part : currentRotations.keySet()) {
        	if(!prevRotations.containsKey(part)) {
        		prevRotations.put(part, new float[] { part.getTransform().rotationX,
        				part.getTransform().rotationY, part.getTransform().rotationZ });
        	}
        	float[] part1 = this.prevRotations.get(part);
        	float[] part2 = this.currentRotations.get(part);
        	try {
        	part.getTransform().rotationX = MathUtils.lerpAngle(part1[0], 
        			part2[0], this.prog);
        	part.getTransform().rotationY = MathUtils.lerpAngle(this.prevRotations
        			.get(part)[1], this.currentRotations.get(part)[1], this.prog);
        	part.getTransform().rotationZ = MathUtils.lerpAngle(this.prevRotations
        			.get(part)[2], this.currentRotations.get(part)[2], this.prog);
        	} catch (NullPointerException e) {
				e.printStackTrace();
			}
        }
        for(int i = 0; i < model.getAnimator().animation.keyframes.size(); i++) {
        	Keyframe kfi = model.getAnimator().animation.keyframes.get(i);
        	if(tick >= kfi.startTick && tick <= kfi.startTick + kf.dur) {
        		this.current = i;
        	}
        }
	}
}
