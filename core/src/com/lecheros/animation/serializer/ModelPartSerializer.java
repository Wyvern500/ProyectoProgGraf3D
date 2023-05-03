package com.lecheros.animation.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.lecheros.animation.Transform;
import com.lecheros.model.ModelPart;
import com.lecheros.utils.Utils;

public class ModelPartSerializer {

	public static String serialize(ModelPart part) {
		String data = "mp:" + part.getName() + "\n";
		data += TransformSerializer.serialize(part.getTransform());
		Logger.getGlobal().info("Model Part serialized data: " + data);
		return data;
	}

	public static List<String[]> divideOnParts(String[] data) {
		List<String[]> parts = new ArrayList<>();
		String[] part = new String[8];
		System.out.println("DivideOnParts input data: " + Arrays.toString(data));
		for (int i = 0; i < data.length; i++) {
			part[i % 8] = data[i];
			/*
			 * if(data[i].startsWith("mp")) { part[i % 8] = data[i]; }
			 */
			if (i % 8 == 7) {
				parts.add(Arrays.copyOf(part, part.length));
				System.out.println(Arrays.toString(part));
				part = new String[8];
			}
		}
		return parts;
	}

	public static ModelPart deserialize(String data) {
		return deserialize(data.split("\n"));
	}

	public static ModelPart deserialize(String[] data) {
		String[] sData = data;
		String mp = sData[0].split(":")[1];
		Transform t = TransformSerializer.deserialize(Utils.getRestOfString(sData, 1));
		ModelPart modelPart = new ModelPart(null, mp);
		modelPart.setTransform(t);
		return modelPart;
	}

}
