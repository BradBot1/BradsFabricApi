package com.bb1.fabric.bfapi.registery;

import java.util.function.BiPredicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public class SimpleValidatedRegistry<T> extends SimpleRegistry<T> implements IValidatedRegistry<T> {
	
	protected final BiPredicate<T, Identifier> validate;
	
	public SimpleValidatedRegistry(Identifier identifier, BiPredicate<T, Identifier> validator) {
		super(identifier);
		this.validate = validator;
	}
	
	@Override
	public @Nullable T add(@NotNull Identifier identifier, @NotNull T registree) {
		if (!validate(identifier, registree)) return null;
		return super.add(identifier, registree);
	}

	@Override
	public boolean validate(@NotNull Identifier identifier, @NotNull T value) {
		return this.validate.test(value, identifier);
	}

}
