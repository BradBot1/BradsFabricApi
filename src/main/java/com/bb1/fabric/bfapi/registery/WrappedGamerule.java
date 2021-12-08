package com.bb1.fabric.bfapi.registery;

import com.bb1.fabric.bfapi.gamerules.AbstractGamerule;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Type;

public class WrappedGamerule<T> extends GameRules.Rule<WrappedGamerule<T>> {
	
	private AbstractGamerule<T> inner;
	
	static <T> GameRules.Type<WrappedGamerule<T>> wrap(AbstractGamerule<T> gamerule, ArgumentType<?> type) {
		return new Type<WrappedGamerule<T>>(()->type, (a)->new WrappedGamerule<T>(a, gamerule), (b, c)->{}, (d,e,f)->{});
	}
	
	private WrappedGamerule(Type<WrappedGamerule<T>> a, AbstractGamerule<T> gamerule) {
		super(a);
		this.inner = gamerule;
	}
	
	@Override
	protected void setFromArgument(CommandContext<ServerCommandSource> context, String name) { this.inner.setValue(this.inner.parse(context.getArgument(name, String.class))); }

	@Override
	protected void deserialize(String value) { inner.deserialize(value); }

	@Override
	public String serialize() { return inner.serialize(); }

	@Override
	public int getCommandResult() { return this.inner.toInt(); }

	@Override
	protected WrappedGamerule<T> getThis() { return this; }

	@Override
	protected WrappedGamerule<T> copy() { return new WrappedGamerule<T>(type, inner); }

	@Override
	public void setValue(WrappedGamerule<T> rule, MinecraftServer server) { this.inner.setValue(rule.getInner().getValue()); }
	
	public AbstractGamerule<T> getInner() { return this.inner; }
	
}