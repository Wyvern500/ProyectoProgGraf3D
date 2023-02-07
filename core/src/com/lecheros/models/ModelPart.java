package com.lecheros.models;

import com.lecheros.animation.Transform;

public class ModelPart {

	protected String name;
	protected Transform transform;
	
	public ModelPart(String name) {
		this.name = name;
		this.transform = new Transform();
	}

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
