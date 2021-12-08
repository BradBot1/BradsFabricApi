package com.bb1.fabric.bfapi.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Either <A, B> {
	
	private final @Nullable A _a;
	
	private final @Nullable B _b;
	
	public Either(@Nullable A a, @Nullable B b) {
		_a = a;
		_b = b;
	}
	
	public final @NotNull Field<@Nullable Object> get() {
		if (_a!=null) { return Field.of(_a); }
		if (_b!=null) { return Field.of(_b); }
		return new Field<Object>(void.class);
	}
	
}
