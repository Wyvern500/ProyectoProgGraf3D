package com.lecheros.animation.serializer;

import java.util.Arrays;
import java.util.logging.Logger;

import com.lecheros.animation.Transform;

public class TransformSerializer {

	public static String serialize(Transform t) {
		String data = "tr:\n";
		data += "x:" + String.valueOf(t.getOffsetX()) + "\n";
		data += "y:" + String.valueOf(t.getOffsetY()) + "\n";
		data += "z:" + String.valueOf(t.getOffsetZ()) + "\n";
		data += "rx:" + String.valueOf(t.getRotationX()) + "\n";
		data += "ry:" + String.valueOf(t.getRotationY()) + "\n";
		data += "rz:" + String.valueOf(t.getRotationZ()) + "\n";
		return data;
	}

	public static Transform deserialize(String data) {
		String[] sData = data.split("\n");
		Transform t = new Transform();
		Logger.getGlobal().info("sData: " + Arrays.toString(sData) + " length: " + sData.length);
		for (String line : sData) {
			if (line.startsWith("x")) {
				t.setOffsetX(Float.parseFloat(line.split(":")[1]));
			} else if (line.startsWith("y")) {
				t.setOffsetY(Float.parseFloat(line.split(":")[1]));
			} else if (line.startsWith("z")) {
				t.setOffsetZ(Float.parseFloat(line.split(":")[1]));
			} else if (line.startsWith("rx")) {
				t.setRotationX(Float.parseFloat(line.split(":")[1]));
			} else if (line.startsWith("ry")) {
				t.setRotationY(Float.parseFloat(line.split(":")[1]));
			} else if (line.startsWith("rz")) {
				t.setRotationZ(Float.parseFloat(line.split(":")[1]));
			}
		}
		Logger.getGlobal().info("Transform: " + t.toString());
		return t;
	}

}
