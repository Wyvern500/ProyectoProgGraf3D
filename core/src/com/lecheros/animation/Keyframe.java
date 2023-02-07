package com.lecheros.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.lecheros.models.ModelPart;

public class Keyframe {

	public int dur;
	public int startTick;
	public int startVisualTick;
	public Map<ModelPart, float[]> translations;
	public Map<ModelPart, float[]> rotations;
	
	public Keyframe(int dur) {
		this.dur = dur;
		translations = new HashMap<>();
		rotations = new HashMap<>();
	}
	
	public void copyTransformFrom(Keyframe kf) {
		translations = copyMap(kf.translations);
		rotations = copyMap(kf.rotations);
	}
	
	public Keyframe copy() {
		Keyframe kf = new Keyframe(dur);
		kf.startTick = startTick;
		kf.startVisualTick = startVisualTick;
		kf.translations = copyMap(translations);
		kf.rotations = copyMap(rotations);
		return kf;
	}

	protected Map<ModelPart, float[]> copyMap(Map<ModelPart, float[]> other){
		Map<ModelPart, float[]> map = new HashMap<>();
		for(Entry<ModelPart, float[]> entry : other.entrySet()) {
			float[] temp = entry.getValue();
			map.put(entry.getKey(), new float[] { temp[0], temp[1], temp[2] });
		}
		return map;
	}
	
}
