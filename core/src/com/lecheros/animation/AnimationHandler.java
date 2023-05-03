package com.lecheros.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationHandler {

	private Map<String, Animation> animations;

	public AnimationHandler() {
		animations = new HashMap<>();
	}

	public void addAnimation(Animation anim) {
		animations.put(anim.name, anim);
	}

	public Animation getAnimation(String name) {
		return animations.get(name);
	}

	public void removeAnimation(String name) {
		animations.remove(name);
	}

	public List<Animation> getAnimationsAsAList() {
		return new ArrayList<Animation>(animations.values());
	}

	public Map<String, Animation> getAnimations() {
		return animations;
	}

}
