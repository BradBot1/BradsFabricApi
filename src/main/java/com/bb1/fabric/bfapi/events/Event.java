package com.bb1.fabric.bfapi.events;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.EVENTS;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Event<I extends Input<?>> implements IRegisterable {
	
	private Set<EventHandler<I>> _handlers = new HashSet<EventHandler<I>>();
	
	public Event(@NotNull String identifier) {
		this(new Identifier(identifier));
	}
	
	public Event(@NotNull Identifier identifier) {
		this(identifier, true);
	}
	
	public Event(@NotNull String identifier, boolean autoRegister) {
		this(new Identifier(identifier), autoRegister);
	}
	
	public Event(@NotNull Identifier identifier, boolean autoRegister) {
		if (autoRegister) { register(identifier); }
	}
	
	public void addHandler(@NotNull EventHandler<I> handler) {
		this._handlers.add(handler);
	}
	
	public void emit(@NotNull I input) {
		this._handlers.forEach((handler)->handler.handle(input));
	}
	
	@Override
	public void register(Identifier name) {
		Registry.register(EVENTS, name, this);
	}
	
}
