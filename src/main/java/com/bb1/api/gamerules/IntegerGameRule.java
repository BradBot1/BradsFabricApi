package com.bb1.api.gamerules;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.GameRules.Category;

public class IntegerGameRule extends GameRule<Integer> {

	public IntegerGameRule(@NotNull String name, int value, Category category) { super(name, value, category); }

	@Override
	public String serialize() { return toString(); }

	@Override
	public void deserialize(String serializedData) { setValue(parse(serializedData)); }

	@Override
	public Integer parse(String string) { return Integer.parseInt(string); }
	
	@Override
	public String toString() { return Integer.toString(value); }
	
	@Override
	public int toInt() { return getValue(); }

}
