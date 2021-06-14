package com.bb1.api.gamerules;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.GameRules.Category;

public class StringGameRule extends GameRule<String> {

	public StringGameRule(@NotNull String name, String value, Category category) { super(name, value, category); }

	@Override
	public String serialize() { return value; }

	@Override
	public void deserialize(String serializedData) { setValue(serializedData); }

	@Override
	public String parse(String string) { return string; }
	
	@Override
	public String toString() { return value; }

}
