package com.lecheros.animation.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.lecheros.animation.Animation;
import com.lecheros.animation.Keyframe;
import com.lecheros.model.JgModel;
import com.lecheros.model.ModelPart;

public class AnimationSerializer {

	public AnimationSerializer() {

	}

	// sk - start Keyframe | t - translation | r - rotation | ek - end

	public static String serialize(Animation animation) {
		String data = "";
		data += "an:" + animation.getName() + "\n";
		data += "mn:" + animation.getModelName() + "\n";
		for (Keyframe kf : animation.getKeyframes()) {
			data += "sk:" + kf.dur + "\n";
			for (Entry<ModelPart, float[]> e : kf.translations.entrySet()) {
				data += "t:" + e.getKey().getName() + "," + serializeFloatArray(e.getValue()) + "\n";
			}
			for (Entry<ModelPart, float[]> e : kf.rotations.entrySet()) {
				data += "r:" + e.getKey().getName() + "," + serializeFloatArray(e.getValue()) + "\n";
			}
		}
		data += "ek:\n";
		return data;
	}

	public static Animation deserialize(JgModel model, String data) {
		String[] splitData = data.split("\n");
		String name = getLineWichStartsWith("an", splitData).split(":")[1];
		String modelName = getLineWichStartsWith("mn", splitData).split(":")[1];
		List<Keyframe> keyframes = new ArrayList<>();
		List<Keyframe> queue = new ArrayList<>();
		int startTick = 0;
		for (int i = 2; i < splitData.length; i++) {
			String currentLine = splitData[i];
			if (currentLine.startsWith("sk")) {
				int dur = Integer.parseInt(currentLine.split(":")[1]);
				Keyframe kf = new Keyframe(dur);
				kf.startTick = startTick;
				kf.startVisualTick = startTick + dur;
				startTick += dur;
				if (queue.isEmpty()) {
					queue.add(kf);
				} else {
					keyframes.add(queue.get(0));
					queue.remove(0);
					queue.add(kf);
				}
			} else if (currentLine.startsWith("t:")) {
				String[] splitCurrent = currentLine.split(":")[1].split(",");
				queue.get(0).translations.put(getPartByName(model.getModelParts(), 
						splitCurrent[0]),
						deserializeFloatArray(splitCurrent));
			} else if (currentLine.startsWith("r:")) {
				String[] splitCurrent = currentLine.split(":")[1].split(",");
				queue.get(0).rotations.put(getPartByName(model.getModelParts(), 
						splitCurrent[0]),
						deserializeFloatArray(splitCurrent));
			}
		}

		// Añado el ultimo keyframe que resta en queue, porque debido al sistema que uso
		// asi
		// sucede, me da flojera cambiarlo asi que simplemente hago esto y se arregla

		if (!queue.isEmpty()) {
			keyframes.add(queue.get(0));
			queue.remove(0);
		}

		Animation animation = new Animation(name);
		animation.addkeyframes(keyframes);
		animation.end();
		animation.setModelName(modelName);
		return animation;
	}

	private static String serializeFloatArray(float[] arr) {
		return arr[0] + "," + arr[1] + "," + arr[2];
	}

	private static float[] deserializeFloatArray(String[] arr) {
		return new float[] { Float.parseFloat(arr[1]), Float.parseFloat(arr[2]), Float.parseFloat(arr[3]) };
	}

	private static ModelPart getPartByName(List<ModelPart> parts, String name) {
		for (ModelPart p : parts) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		Logger.getGlobal().info("No Model Part found with name " + name);
		return null;
	}

	private static String getLineWichStartsWith(String starts, String[] data) {
		for (String d : data) {
			if (d.startsWith(starts)) {
				return d;
			}
		}
		return null;
	}

}
