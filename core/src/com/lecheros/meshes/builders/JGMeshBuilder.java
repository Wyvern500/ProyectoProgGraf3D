package com.lecheros.meshes.builders;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;

public class JGMeshBuilder {

	public static void build(MeshPartBuilder builder, float w, float h, float d) {
		float hw = w * 0.5f;
		float hh = h * 0.5f;
		float hd = d * 0.5f;
		builder.ensureVertices(8);
		buildVertices(builder, hw, hh, hd);
	}

	public static void buildVertices(MeshPartBuilder builder, float hw, float hh, float hd) {
		VertexInfo inf = new VertexInfo();
		for (int i = 0; i < 8; i++) {
			switch (i) {
			case 0:
				builder.vertex(inf.setPos(new Vector3(hw, -hh, -hd)));
				break;
			case 1:
				builder.vertex(inf.setPos(new Vector3(hw, -hh, hd)));
				break;
			case 2:
				builder.vertex(inf.setPos(new Vector3(-hw, -hh, hd)));
				break;
			case 3:
				builder.vertex(inf.setPos(new Vector3(-hw, -hh, -hd)));
				break;
			case 4:
				builder.vertex(inf.setPos(new Vector3(hw, hh, -hd)));
				break;
			case 5:
				builder.vertex(inf.setPos(new Vector3(hw, hh, hd)));
				break;
			case 6:
				builder.vertex(inf.setPos(new Vector3(-hw, hh, hd)));
				break;
			case 7:
				builder.vertex(inf.setPos(new Vector3(-hw, hh, -hd)));
				break;
			}
		}
	}

}
