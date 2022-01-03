package com.bb1.fabric.bfapi.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Container<T> {
	
	public Container() { }
	
	public Container(@NotNull T defaultValue) {
		this._t = defaultValue;
	}
	
	private volatile @Nullable T _t;
	
	public @Nullable T getValue() {
		return this._t;
	}
	
	public void setValue(@NotNull T t) {
		this._t = t;
	}
	
}
