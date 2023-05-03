package com.lecheros.model;

import java.util.Arrays;
import java.util.logging.Logger;

import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.lecheros.animation.Transform;

public class ModelPart {

	protected String name;
	protected Transform transform;
	protected MeshPart mesh;
	protected Matrix4 matrix;
	protected float[] vertices;
	
	public ModelPart(MeshPart mesh, String name) {
		this.mesh = mesh;
		this.name = name;
		this.transform = new Transform();
		this.matrix = new Matrix4().idt();
		if(mesh != null) {
			final int numVertices = mesh.mesh.getNumVertices();
			final int vertexSize = mesh.mesh.getVertexSize() / 4;
	
			final float[] vertices = new float[numVertices * vertexSize];
			this.vertices = Arrays.copyOf(mesh.mesh.getVertices(vertices), 
					mesh.mesh.getVertices(vertices).length);
		}
	}

	// Class methods
	
	public void synchronize() {
		this.mesh.mesh.setVertices(Arrays.copyOf(vertices, vertices.length));
		this.matrix = matrix.idt();
		translate(transform.getOffsetX(), transform.getOffsetY(), transform.getOffsetZ());
		rotate(transform.getRotationX(), transform.getRotationY(), transform.getRotationZ());
	}
	
	public void translate(Vector3 vec) {
		mesh.mesh.transform(matrix.translate(vec.x, vec.y, vec.z));
	}
	
	public void translate(float x, float y, float z) {
		mesh.mesh.transform(matrix.translate(x, y, z));
	}
	
	public void rotate(Vector3 vec, float angle) {
		mesh.mesh.transform(matrix.rotate(new Quaternion(vec, angle)));
	}
	
	public void rotate(float x, float y, float z) {
		mesh.mesh.transform(matrix.rotate(new Quaternion(x, y, z, 1.0f)));
	}
	
	// Getters and setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

}
