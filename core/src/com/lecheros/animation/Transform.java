package com.lecheros.animation;

public class Transform {
	
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	public float rotationX;
	public float rotationY;
	public float rotationZ;
	
	public Transform() {
		
	}
	
	public Transform(float offsetX, float offsetY, float offsetZ, float rotationX, 
			float rotationY, float rotationZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
	}

	public Transform copy() {
		return new Transform(offsetX, offsetY, offsetZ, rotationX, rotationY, rotationZ);
	}
	
	public void copyFrom(Transform other) {
		offsetX = other.offsetX;
		offsetY = other.offsetY;
		offsetZ = other.offsetZ;
		rotationX = other.rotationX;
		rotationY = other.rotationY;
		rotationZ = other.rotationZ;
	}
	
	@Override
	public String toString() {
		return "OffsetX: " + offsetX + " offsetY: " + offsetY + " offsetZ: " + offsetZ + 
				" RotationX: " + rotationX + " RotationY: " + rotationY + " RotationZ: " +
				rotationZ;
	}
}
