package com.lecheros.display.handlers;

public enum FocusHandler {
	INSTANCE;
	
	Class<?> clazz;
	
	private FocusHandler() {
		
	}
	
	public void setLastFocus(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getLastFocusedClass() {
		return clazz;
	}
	
}
