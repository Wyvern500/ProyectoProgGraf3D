package com.lecheros.animation.serializer;

import java.util.logging.Logger;

import com.lecheros.animation.Animation;
import com.lecheros.model.JgModel;
import com.lecheros.model.ModelPart;
import com.lecheros.utils.Utils;

public class ModelSerializer {

	public static final int MODELPARTS = 0;
	public static final int ANIMATIONS = 1;

	public static String serialize(JgModel model) {
		String data = "";
		data += "mfp:" + model.getModelFilePath() + "\n";
		data += "mn:" + model.getName() + "\n";
		for (ModelPart part : model.getModelParts()) {
			data += ModelPartSerializer.serialize(part);
		}
		for (Animation anim : model.getAnimations()) {
			data += AnimationSerializer.serialize(anim);
		}
		return data;
	}

	public static JgModel deserialize(String data) {
		String[] spData = data.split("\n");
		String[] sPath = spData[0].split(":");
		String path = sPath[1] + ":";
		for (int i = 2; i < sPath.length; i++) {
			path += sPath[i];
		}
		String modelName = spData[1].split(":")[1];
		JgModel model = new JgModel(path, modelName, true);
		int[] modelPartsBounds = findStartAndEndOf(spData, MODELPARTS);

		for (String[] modelPartsData : ModelPartSerializer
				.divideOnParts(Utils.getFromToString(spData, modelPartsBounds[0], modelPartsBounds[1]))) {
			ModelPart part = ModelPartSerializer.deserialize(modelPartsData);
			model.getModelPart(part.getName()).setTransform(part.getTransform());;
		}

		int[] animationPartsBounds = findStartAndEndOf(spData, ANIMATIONS);
		
		if(animationPartsBounds[0] != -1) {
			String[] wholeAnimData = Utils.getFromToString(spData, animationPartsBounds[0], 
					animationPartsBounds[1]);
			String animData = "";
			boolean an = false;
			for(String s : wholeAnimData) {
				if(s.startsWith("an")) {
					if(an) {
						//System.out.println("an: \n" + animData);
					}
					an = true;
					animData = "";
				} 
				if(an) {
					animData += s + "\n";
				}
				if(s.startsWith("ek")) {
					/*System.out.println("Animation name: " + AnimationSerializer.deserialize(model, animData)
							.getName() + " keyframes size: " 
							+ AnimationSerializer.deserialize(model, animData).getKeyframes()
							.size());¨*/
					model.addAnimation(AnimationSerializer.deserialize(model, animData));
					//System.out.println("ek: \n" + animData);
				}
			}
		}
		/*
		 * int start = -1; int end = -1; for(int i = 2; i < spData.length; i++) { String
		 * d = spData[i]; if(d.startsWith("an") && start == -1) { start = i; } else
		 * if(d.startsWith("an") && start != -1) { end = i; } if(start != -1 && end !=
		 * -1) { for(int f = start; f < end; f++) { animationData += spData[f] + "\n"; }
		 * model.getAnimations().add(AnimationSerializer .deserialize(model,
		 * animationData)); start = -1; end = -1; animationData = ""; } }
		 */
		return model;
	}

	public static int[] findStartAndEndOf(String[] data, int type) {
		int[] startAndEnd = new int[] { -1, -1 };
		if (type == MODELPARTS) {
			for (int i = 0; i < data.length; i++) {
				if (data[i].startsWith("mp") && startAndEnd[0] == -1) {
					startAndEnd[0] = i;
					Logger.getGlobal().info("Start non -1");
				} else if (data[i].startsWith("an")) {
					Logger.getGlobal().info("End non -1 an");
					startAndEnd[1] = i - 1; // i - 1 por que si no estaria agarrando el inicio
					// del apartado de las animaciones
					break;
				}
			}
			if (startAndEnd[1] == -1) {
				Logger.getGlobal().info("End non -1");
				startAndEnd[1] = data.length;
			}
			return startAndEnd;
		} else if (type == ANIMATIONS) {
			for (int i = 0; i < data.length; i++) {
				if (data[i].startsWith("an") && startAndEnd[0] == -1) {
					startAndEnd[0] = i;
					break;
				}
			}
			startAndEnd[1] = data.length - 1;
			return startAndEnd;
		}
		return startAndEnd;
	}

}
