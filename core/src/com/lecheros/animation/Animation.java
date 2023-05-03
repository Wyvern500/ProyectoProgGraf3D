package com.lecheros.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.lecheros.model.ModelPart;

public class Animation {

	public String name;
	public int dur;
	public TreeMap<Integer, Keyframe> keyframesMap;
	private List<Keyframe> keyframes;
	public int startKeyframeIndex;
	public int startTick;
	private String modelName;

	public Animation(String name) {
		this.name = name;
		this.keyframesMap = new TreeMap<>();
		this.keyframes = new ArrayList<>();
		this.startKeyframeIndex = -1;
	}
	
	public Animation(String name, int dur) {
		this.name = name;
		this.dur = dur;
		this.keyframesMap = new TreeMap<>();
		this.keyframes = new ArrayList<>();
		this.startKeyframeIndex = -1;
	}

	public Animation startKeyframe(int dur) {
		this.startKeyframeIndex++;
		Keyframe kf = new Keyframe(dur);
		kf.startTick = this.startTick;
		kf.startVisualTick = this.startTick + dur;
		this.keyframesMap.put(kf.startTick, kf);
		this.keyframes.add(kf);
		this.startTick += dur;
		return this;
	}

	public Animation translate(ModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).translations.put(part, new float[] { x, y, z });
		return this;
	}

	public Animation rotate(ModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).rotations.put(part, new float[] { x, y, z });
		return this;
	}

	public Animation end() {
		for (Keyframe kf : keyframes) {
			this.dur += kf.dur;
		}
		return this;
	}

	public void updateKeyframes() {
		keyframes = new ArrayList<>(keyframesMap.values());
		// Setting new duration and startTick
		for (int i = 0; i < keyframes.size() - 1; i++) {
			Keyframe currentKf = keyframes.get(i);
			Keyframe nextKf = keyframes.get(i + 1);
			currentKf.dur = nextKf.startTick - currentKf.startTick;
			currentKf.startVisualTick = currentKf.startTick + currentKf.dur;
		}
		if(keyframes.size() > 0) {
			// Set last keyframe duration
			Keyframe last = keyframes.get(keyframes.size() - 1); 
			last.dur = dur - last.startTick;
			last.startVisualTick = last.startTick + last.dur;	
		}
	}

	public void addKeyframe(Keyframe kf) {
		if(keyframesMap.containsKey(kf.startTick)) {
			Logger.getGlobal().info("Keyframe already exists");
		} else {
			this.keyframesMap.put(kf.startTick, kf);
			updateKeyframes();
			Logger.getGlobal().info("Keyframe added sucessfully with new Keyframes size of " + 
					keyframes.size());
		}
	}
	
	public void deleteKeyframe(Keyframe kf) {
		this.keyframesMap.remove(kf.startTick);
		updateKeyframes();
	}
	
	public void addkeyframes(List<Keyframe> keyframes) {
		for(Keyframe kf : keyframes) {
			keyframesMap.put(kf.startTick, kf);
		}
		this.keyframes = new ArrayList<>(keyframesMap.values());
	}
	
	public boolean hasKeyframe(Keyframe kf) {
		return keyframesMap.containsKey(kf.startTick);
	}
	
	// Getters and Setters

	public int getDuration() {
		return dur;
	}

	public void setDurationWithCheck(int dur) {
		int end = -1;
		for(int i = 0; i < keyframes.size(); i++) {
			Keyframe currentKf = keyframes.get(i);
			if(currentKf.startVisualTick > dur) {
				end = i;
				break;
			}
		}
		if(end != -1) {
			keyframes = keyframes.subList(0, end);
		}
		this.dur = dur;
	}
	
	public void setDuration(int dur) {
		this.dur = dur;
	}

	public List<Keyframe> getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(List<Keyframe> keyframes) {
		this.keyframes = keyframes;
	}

	public int getStartKeyframeIndex() {
		return startKeyframeIndex;
	}

	public void setStartKeyframeIndex(int startKeyframeIndex) {
		this.startKeyframeIndex = startKeyframeIndex;
	}

	public int getStartTick() {
		return startTick;
	}

	public void setStartTick(int startTick) {
		this.startTick = startTick;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getModelName() {
		return modelName;
	}

	public Animation setModelName(String name) {
		this.modelName = name;
		return this;
	}

	public void setDur(int dur) {
		this.dur = dur;
	}

}
