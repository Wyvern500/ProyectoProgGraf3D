package com.lecheros.utils;

import com.lecheros.animator.Engine;
import com.lecheros.display.widgets.windows.TransformModifierWindow;

public class WindowUtils {

	public static <T> void createTransformWindow(Engine engine, T obj, int x, int y) {
		engine.getWindowHandler().addWindow(engine, 
				new TransformModifierWindow<T>
					(obj, x, y, 300, 200));
	}
	
}
