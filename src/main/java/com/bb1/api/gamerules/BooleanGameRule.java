package com.bb1.api.gamerules;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.GameRules.Category;

public class BooleanGameRule extends GameRule<Boolean> {

	public BooleanGameRule(@NotNull String name, boolean value, Category category) { super(name, value, category); }

	@Override
	public String serialize() { return toString(); }

	@Override
	public void deserialize(String serializedData) { setValue(parse(serializedData)); }

	@Override
	public Boolean parse(String string) { return Boolean.parseBoolean(string); }
	
	@Override
	public String toString() { return Boolean.toString(getValue()); }
	
	@Override
	public int toInt() { return getValue() ? 1 : 0; }

}
