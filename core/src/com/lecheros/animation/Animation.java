package com.lecheros.animation;

import java.util.ArrayList;
import java.util.List;

import com.lecheros.models.ModelPart;

public class Animation {
	
	public int dur;
	public List<Keyframe> keyframes;
	public int startKeyframeIndex;
	public int startTick;
	
	public Animation() {
		this.keyframes = new ArrayList<>();
		this.startKeyframeIndex = -1;
	}
	
	public Animation startKeyframe(int dur) {
		this.startKeyframeIndex++;
		Keyframe kf = new Keyframe(dur);
		kf.startTick = this.startTick;
		kf.startVisualTick = this.startTick + dur;
		this.keyframes.add(kf);
		this.startTick += dur;
		return this;
	}
	
	public Animation translate(ModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).translations.put(part, 
				new float[] { x, y, z });
		return this;
	}
	
	public Animation rotate(ModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).rotations.put(part, 
				new float[] { x, y, z });
		return this;
	}
	
	public Animation end() {
		for(Keyframe kf : keyframes) {
			this.dur += kf.dur;
		}
		return this;
	}
}
