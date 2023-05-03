package com.lecheros.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.lecheros.model.JgModel;
import com.lecheros.model.ModelPart;

public class Animator {

	public int prevStartTick;
	public int prevDur;
	public int current;
	public int old;
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

	private Map<ModelPart, float[]> currentTranslations;
	private Map<ModelPart, float[]> currentRotations;
	private Map<ModelPart, float[]> prevTranslations;
	private Map<ModelPart, float[]> prevRotations;

	public Animator() {
		
	}

	public void setModel(JgModel model) {
		this.model = model;
		currentTranslations = new HashMap<>();
		currentRotations = new HashMap<>();
		prevTranslations = new HashMap<>();
		prevRotations = new HashMap<>();
		for (int i = 0; i < model.getModelParts().size(); i++) {
			this.prevTranslations.put(model.getModelParts().get(i), new float[] { 0, 0, 0 });
			this.prevRotations.put(model.getModelParts().get(i), new float[] { 0, 0, 0 });
		}
		animation = null;
	}

	public void setAnimation(Animation animation) {
		if (model == null)
			return;
		this.current = 0;
		this.animation = animation;
		this.tick = 0;
		this.prevTick = 0;
		this.prog = 0;
		if (!animation.getKeyframes().isEmpty()) {
			currentTranslations = animation.getKeyframes().get(current).translations;
			currentRotations = animation.getKeyframes().get(current).rotations;
			this.prevDur = 0;
		}
	}

	public void playAnim() {
		this.play = !play;
	}

	public void update() {
		if (this.animation != null && play && !animation.getKeyframes().isEmpty()) {
			Logger.getGlobal().info("1");
			this.prevTick = this.tick;
			this.tick += Gdx.graphics.getDeltaTime() * 10;
			Keyframe kf = this.animation.getKeyframes().get(current);
			if (this.tick - kf.startTick > kf.dur) {
				this.tick = kf.startTick + kf.dur;
				this.currentTranslations = kf.translations;
				this.currentRotations = kf.rotations;
				this.prevStartTick = kf.startTick;
			}
			float prosTick = this.tick - kf.startTick;
			float prosPrevTick = this.prevTick - kf.startTick;
			this.prog = (prosPrevTick + (prosTick - prosPrevTick) * Gdx.graphics.getDeltaTime()) / kf.dur;
			this.pro = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
			if (this.tick - kf.startTick == kf.dur) {
				this.prog = 1.0f;
			}
			for (ModelPart part : currentTranslations.keySet()) {
				if (!prevTranslations.containsKey(part)) {
					prevTranslations.put(part, new float[] { part.getTransform().getOffsetX(),
							part.getTransform().getOffsetY(), part.getTransform().getOffsetZ() });
				}
				float[] part1 = this.prevTranslations.get(part);
				float[] part2 = this.currentTranslations.get(part);
				try {
					part.getTransform().setOffsetX(MathUtils.lerp(part1[0], part2[0], this.prog));
					part.getTransform().setOffsetY(MathUtils.lerp(this.prevTranslations.get(part)[1],
							this.currentTranslations.get(part)[1], this.prog));
					part.getTransform().setOffsetZ(MathUtils.lerp(this.prevTranslations.get(part)[2],
							this.currentTranslations.get(part)[2], this.prog));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			for (ModelPart part : currentRotations.keySet()) {
				if (!prevRotations.containsKey(part)) {
					prevRotations.put(part, new float[] { part.getTransform().getRotationX(),
							part.getTransform().getRotationY(), part.getTransform().getRotationZ() });
				}
				float[] part1 = this.prevRotations.get(part);
				float[] part2 = this.currentRotations.get(part);
				try {
					part.getTransform().setRotationX(MathUtils.lerpAngle(part1[0], part2[0], this.prog));
					part.getTransform().setRotationY(MathUtils.lerpAngle(this.prevRotations.get(part)[1],
							this.currentRotations.get(part)[1], this.prog));
					part.getTransform().setRotationZ(MathUtils.lerpAngle(this.prevRotations.get(part)[2],
							this.currentRotations.get(part)[2], this.prog));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			if (this.tick - kf.startTick >= kf.dur) {
				this.current++;
				if (this.current + 1 > this.animation.getKeyframes().size()) {
					setAnimation(animation);
					// this.animation = null;
					/*this.tick = 0;
					this.prevTick = 0;
					this.current = 0;*/
					/*for (int i = 0; i < model.getModelParts().size(); i++) {
						this.prevTranslations.put(model.getModelParts().get(i), 
								new float[] { 0, 0, 0 });
						this.prevRotations.put(model.getModelParts().get(i), 
								new float[] { 0, 0, 0 });
					}*/
					//this.play = false;
					return;
				}
				this.updateCurrentMaps();
			}
		} /*else if (animation != null && !play && !animation.getKeyframes().isEmpty()) {
			Logger.getGlobal().info("2");
			Keyframe kf = this.animation.getKeyframes().get(current);
			if (this.tick - kf.startTick > kf.dur) {
				this.currentTranslations = kf.translations;
				this.currentRotations = kf.rotations;
				this.prevStartTick = kf.startTick;
			}
			float prosTick = this.tick - kf.startTick;
			float prosPrevTick = this.prevTick - kf.startTick;
			this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
			if (this.tick - kf.startTick == kf.dur) {
				this.prog = 1.0f;
			}
			for (ModelPart part : currentTranslations.keySet()) {
				if (!prevTranslations.containsKey(part)) {
					prevTranslations.put(part, new float[] { part.getTransform().getOffsetX(),
							part.getTransform().getOffsetY(), part.getTransform().getOffsetZ() });
				}
				float[] part1 = this.prevTranslations.get(part);
				float[] part2 = this.currentTranslations.get(part);
				try {
					part.getTransform().setOffsetX(MathUtils.lerp(part1[0], part2[0], this.prog));
					part.getTransform().setOffsetY(MathUtils.lerp(this.prevTranslations.get(part)[1],
							this.currentTranslations.get(part)[1], this.prog));
					part.getTransform().setOffsetZ(MathUtils.lerp(this.prevTranslations.get(part)[2],
							this.currentTranslations.get(part)[2], this.prog));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			for (ModelPart part : currentRotations.keySet()) {
				if (!prevRotations.containsKey(part)) {
					prevRotations.put(part, new float[] { part.getTransform().getRotationX(),
							part.getTransform().getRotationY(), part.getTransform().getRotationZ() });
				}
				float[] part1 = this.prevRotations.get(part);
				float[] part2 = this.currentRotations.get(part);
				try {
					part.getTransform().setRotationX(MathUtils.lerpAngle(part1[0], part2[0], this.prog));
					part.getTransform().setRotationY(MathUtils.lerpAngle(this.prevRotations.get(part)[1],
							this.currentRotations.get(part)[1], this.prog));
					part.getTransform().setRotationZ(MathUtils.lerpAngle(this.prevRotations.get(part)[2],
							this.currentRotations.get(part)[2], this.prog));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			if (this.tick - kf.startTick >= kf.dur) {
				this.current++;
				if (this.current + 1 > this.animation.getKeyframes().size()) {
					//this.animation = null;
					this.tick = 0;
					this.prevTick = 0;
					this.current = 0;
					//this.play = false;
					return;
				}
				this.updateCurrentMaps();
			} else if (this.tick - kf.startTick <= -1) {
				this.current--;
				if (this.current < 0) {
					this.current = 0;
				}
				this.updateCurrentMaps();
			}
		}*/
	}

	public void updateCurrentMaps() {
		if (model == null)
			return;
		this.prevTranslations = this.currentTranslations;
		this.prevRotations = this.currentRotations;
		this.currentTranslations = this.animation.getKeyframes().get(current).translations;
		this.currentRotations = this.animation.getKeyframes().get(current).rotations;
	}

	public void nextTick() {
		if (model == null)
			return;
		this.prevTick = this.tick;
		this.tick += 1;
		Keyframe kf = this.animation.getKeyframes().get(current);
		if (this.tick - kf.startTick > kf.dur) {
			this.currentTranslations = kf.translations;
			this.currentRotations = kf.rotations;
			this.prevStartTick = kf.startTick;
		}
		float prosTick = this.tick - kf.startTick;
		float prosPrevTick = this.prevTick - kf.startTick;
		this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
		if (this.tick - kf.startTick == kf.dur) {
			this.prog = 1.0f;
		}
		for (ModelPart part : currentTranslations.keySet()) {
			if (!prevTranslations.containsKey(part)) {
				prevTranslations.put(part, new float[] { part.getTransform().getOffsetX(),
						part.getTransform().getOffsetY(), part.getTransform().getOffsetZ() });
			}
			float[] part1 = this.prevTranslations.get(part);
			float[] part2 = this.currentTranslations.get(part);
			try {
				part.getTransform().setOffsetX(MathUtils.lerp(part1[0], part2[0], this.prog));
				part.getTransform().setOffsetY(MathUtils.lerp(this.prevTranslations.get(part)[1],
						this.currentTranslations.get(part)[1], this.prog));
				part.getTransform().setOffsetZ(MathUtils.lerp(this.prevTranslations.get(part)[2],
						this.currentTranslations.get(part)[2], this.prog));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (ModelPart part : currentRotations.keySet()) {
			if (!prevRotations.containsKey(part)) {
				prevRotations.put(part, new float[] { part.getTransform().getRotationX(),
						part.getTransform().getRotationY(), part.getTransform().getRotationZ() });
			}
			float[] part1 = this.prevRotations.get(part);
			float[] part2 = this.currentRotations.get(part);
			try {
				part.getTransform().setRotationX(MathUtils.lerpAngle(part1[0], part2[0], this.prog));
				part.getTransform().setRotationY(MathUtils.lerpAngle(this.prevRotations.get(part)[1],
						this.currentRotations.get(part)[1], this.prog));
				part.getTransform().setRotationZ(MathUtils.lerpAngle(this.prevRotations.get(part)[2],
						this.currentRotations.get(part)[2], this.prog));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < model.getAnimator().animation.getKeyframes().size(); i++) {
			Keyframe kfi = model.getAnimator().animation.getKeyframes().get(i);
			if (tick >= kfi.startTick && tick <= kfi.startTick + kf.dur) {
				this.current = i;
			}
		}
	}

	public void prevTick() {
		if (model == null)
			return;
		this.prevTick = this.tick;
		this.tick -= 1;
		Keyframe kf = this.animation.getKeyframes().get(current);
		if (this.tick - kf.startTick > kf.dur) {
			this.currentTranslations = kf.translations;
			this.currentRotations = kf.rotations;
			this.prevStartTick = kf.startTick;
		}
		if (tick < 0) {
			tick = 0;
		}
		float prosTick = this.tick - kf.startTick;
		float prosPrevTick = this.prevTick - kf.startTick;
		this.prog = (prosPrevTick + (prosTick - prosPrevTick)) / kf.dur;
		if (this.tick - kf.startTick == kf.dur) {
			this.prog = 1.0f;
		}
		for (ModelPart part : currentTranslations.keySet()) {
			if (!prevTranslations.containsKey(part)) {
				prevTranslations.put(part, new float[] { part.getTransform().getOffsetX(),
						part.getTransform().getOffsetY(), part.getTransform().getOffsetZ() });
			}
			float[] part1 = this.prevTranslations.get(part);
			float[] part2 = this.currentTranslations.get(part);
			try {
				part.getTransform().setOffsetX(MathUtils.lerp(part1[0], part2[0], this.prog));
				part.getTransform().setOffsetY(MathUtils.lerp(this.prevTranslations.get(part)[1],
						this.currentTranslations.get(part)[1], this.prog));
				part.getTransform().setOffsetZ(MathUtils.lerp(this.prevTranslations.get(part)[2],
						this.currentTranslations.get(part)[2], this.prog));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (ModelPart part : currentRotations.keySet()) {
			if (!prevRotations.containsKey(part)) {
				prevRotations.put(part, new float[] { part.getTransform().getRotationX(),
						part.getTransform().getRotationY(), part.getTransform().getRotationZ() });
			}
			float[] part1 = this.prevRotations.get(part);
			float[] part2 = this.currentRotations.get(part);
			try {
				part.getTransform().setRotationX(MathUtils.lerpAngle(part1[0], part2[0], this.prog));
				part.getTransform().setRotationY(MathUtils.lerpAngle(this.prevRotations.get(part)[1],
						this.currentRotations.get(part)[1], this.prog));
				part.getTransform().setRotationZ(MathUtils.lerpAngle(this.prevRotations.get(part)[2],
						this.currentRotations.get(part)[2], this.prog));
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < model.getAnimator().animation.getKeyframes().size(); i++) {
			Keyframe kfi = model.getAnimator().animation.getKeyframes().get(i);
			if (tick >= kfi.startTick && tick <= kfi.startTick + kf.dur) {
				this.current = i;
			}
		}
	}

	public int getPrevStartTick() {
		return prevStartTick;
	}

	public void setPrevStartTick(int prevStartTick) {
		this.prevStartTick = prevStartTick;
	}

	public int getPrevDur() {
		return prevDur;
	}

	public void setPrevDur(int prevDur) {
		this.prevDur = prevDur;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public float getTick() {
		return tick;
	}

	public void setTick(float tick) {
		this.tick = tick;
	}

	public float getProg() {
		return prog;
	}

	public void setProg(float prog) {
		this.prog = prog;
	}

	public float getPrevTick() {
		return prevTick;
	}

	public void setPrevTick(float prevTick) {
		this.prevTick = prevTick;
	}

	public float getPro() {
		return pro;
	}

	public void setPro(float pro) {
		this.pro = pro;
	}

	public float getPre() {
		return pre;
	}

	public void setPre(float pre) {
		this.pre = pre;
	}

	public float getMAX() {
		return MAX;
	}

	public void setMAX(float mAX) {
		MAX = mAX;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	public Map<ModelPart, float[]> getCurrentTranslations() {
		return currentTranslations;
	}

	public void setCurrentTranslations(Map<ModelPart, float[]> currentTranslations) {
		this.currentTranslations = currentTranslations;
	}

	public Map<ModelPart, float[]> getCurrentRotations() {
		return currentRotations;
	}

	public void setCurrentRotations(Map<ModelPart, float[]> currentRotations) {
		this.currentRotations = currentRotations;
	}

	public Map<ModelPart, float[]> getPrevTranslations() {
		return prevTranslations;
	}

	public void setPrevTranslations(Map<ModelPart, float[]> prevTranslations) {
		this.prevTranslations = prevTranslations;
	}

	public Map<ModelPart, float[]> getPrevRotations() {
		return prevRotations;
	}

	public void setPrevRotations(Map<ModelPart, float[]> prevRotations) {
		this.prevRotations = prevRotations;
	}

	public Animation getAnimation() {
		return animation;
	}

	public JgModel getModel() {
		return model;
	}

}
