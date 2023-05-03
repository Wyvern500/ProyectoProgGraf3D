package com.lecheros.animation;

public class Transform {

	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private float rotationX;
	private float rotationY;
	private float rotationZ;

	public Transform() {

	}

	public Transform(float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ) {
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
	
	public float[] getTranslation() {
		return new float[] { offsetX, offsetY, offsetZ };
	}
	
	public float[] getRotation() {
		return new float[] { rotationX, rotationY, rotationZ };
	}

	public float getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getOffsetZ() {
		return offsetZ;
	}

	public void setOffsetZ(float offsetZ) {
		this.offsetZ = offsetZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	@Override
	public String toString() {
		return "OffsetX: " + offsetX + " offsetY: " + offsetY + " offsetZ: " + offsetZ + " RotationX: " + rotationX
				+ " RotationY: " + rotationY + " RotationZ: " + rotationZ;
	}
}
