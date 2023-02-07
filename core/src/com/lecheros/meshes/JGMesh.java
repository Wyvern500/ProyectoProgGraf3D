package com.lecheros.meshes;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;

public class JGMesh {

	private float w;
	private float h;
	private float d;
	
	public JGMesh(float w, float h, float d) {
		this.w = w;
		this.h = h;
		this.d = d;
	}
	
	public String serialize() {
		String data = "";
		float hw = w*0.5f;
		float hh = h*0.5f;
		float hd = d*0.5f;
		// Serializing vertices
		for(int i = 0; i < 8; i++) {
			switch(i) {
			case 0:
				data += "v " + serializeVector(hw, -hh, -hd) + "\n";
				break;
			case 1:
				data += "v " + serializeVector(hw, -hh, hd) + "\n";
				break;
			case 2:
				data += "v " + serializeVector(-hw, -hh, hd) + "\n";
				break;
			case 3:
				data += "v " + serializeVector(-hw, -hh, -hd) + "\n";
				break;
			case 4:
				data += "v " + serializeVector(hw, hh, -hd) + "\n";
				break;
			case 5:
				data += "v " + serializeVector(hw, hh, hd) + "\n";
				break;
			case 6:
				data += "v " + serializeVector(-hw, hh, hd) + "\n";
				break;
			case 7:
				data += "v " + serializeVector(-hw, hh, -hd) + "\n";
				break;
			}
		}
		// Adding normals and textures
		data += "vt 1.000000 0.333333\n"
				+ "vt 1.000000 0.666667\n"
				+ "vt 0.666667 0.666667\n"
				+ "vt 0.666667 0.333333\n"
				+ "vt 0.666667 0.000000\n"
				+ "vt 0.000000 0.333333\n"
				+ "vt 0.000000 0.000000\n"
				+ "vt 0.333333 0.000000\n"
				+ "vt 0.333333 1.000000\n"
				+ "vt 0.000000 1.000000\n"
				+ "vt 0.000000 0.666667\n"
				+ "vt 0.333333 0.333333\n"
				+ "vt 0.333333 0.666667\n"
				+ "vt 1.000000 0.000000\n"
				+ "vn 0.000000 -1.000000 0.000000\n"
				+ "vn 0.000000 1.000000 0.000000\n"
				+ "vn 1.000000 0.000000 0.000000\n"
				+ "vn -0.000000 0.000000 1.000000\n"
				+ "vn -1.000000 -0.000000 -0.000000\n"
				+ "vn 0.000000 0.000000 -1.000000\n";
		// Adding some stuff
		data += "usemtl Material\n";
		data +=	"s off\n";
		// Serializing faces
		data += "f 2/1/1 3/2/1 4/3/1\n"
				+ "f 8/1/2 7/4/2 6/5/2\n"
				+ "f 5/6/3 6/7/3 2/8/3\n"
				+ "f 6/8/4 7/5/4 3/4/4\n"
				+ "f 3/9/5 7/10/5 8/11/5\n"
				+ "f 1/12/6 4/13/6 8/11/6\n"
				+ "f 1/4/1 2/1/1 4/3/1\n"
				+ "f 5/14/2 8/1/2 6/5/2\n"
				+ "f 1/12/3 5/6/3 2/8/3\n"
				+ "f 2/12/4 6/8/4 3/4/4\n"
				+ "f 4/13/5 3/9/5 8/11/5\n"
				+ "f 5/6/6 1/12/6 8/11/6";
		return data;
	}
	
	public String serializeVector(float x, float y, float z) {
		return String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z);
	}
	
}
