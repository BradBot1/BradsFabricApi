package com.bb1.api.gamerules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.events.Events;
import com.bb1.api.providers.GameRuleProvider;

import net.minecraft.world.GameRules.Category;

public abstract class GameRule<T> {
	
	protected final String name;
	
	protected final Category category;
	
	protected T value;
	
	protected GameRule(@NotNull String name, @NotNull T defaultValue, Category category) { this.name = name; this.value = defaultValue; this.category = category; }
	
	public final String getName() { return this.name; }
	
	public final void setValue(T value) { this.value = value; }
	
	public final T getValue() { return this.value; }
	
	public final Category getCategory() { return this.category; }
	
	public abstract String serialize();
	
	public abstract void deserialize(String serializedData);
	
	public int toInt() { return Objects.hashCode(value); }
	
	public abstract T parse(String string);
	
	public void register() {
		Events.PROVIDER_INFO_EVENT.register((event) -> {
			if (event.getProvider() instanceof GameRuleProvider) {
				event.give(this);
			}
		});
	}

}
