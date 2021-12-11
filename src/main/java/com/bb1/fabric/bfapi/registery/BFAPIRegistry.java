package com.bb1.fabric.bfapi.registery;

import static com.bb1.fabric.bfapi.Constants.ID;
import static net.minecraft.util.registry.RegistryKey.ofRegistry;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.gamerules.AbstractGamerule;
import com.bb1.fabric.bfapi.nbt.mark.INbtMarkListener;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.database.IPermissionDatabase;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Lifecycle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Type;

public class BFAPIRegistry<T> extends SimpleRegistry<T> {
	
	public static final BFAPIRegistry<INbtMarkListener> MARK_LISTENER = new BFAPIRegistry<INbtMarkListener>("mark_listeners");
	public static final BFAPIRegistry<Event<?>> EVENTS = new BFAPIRegistry<Event<?>>("events");
	public static final BFAPIRegistry<AbstractGamerule<?>> GAMERULES = new BFAPIRegistry<AbstractGamerule<?>>("gamerules", (i)->{
		Type<?> rule = WrappedGamerule.wrap(i.getSecond(), i.getSecond().getArgument());
		ExceptionWrapper.execute(DualInput.of(rule, i), (r)->{
			GameRules.register(i.getSecond().getName(), i.getSecond().getCategory(), r.get());
		}, (e)->{
			GameRules.register(i.getSecond().getOwner()+":"+i.getSecond().getName(), i.getSecond().getCategory(), rule);
		});
		return true;
	});
	public static final BFAPIRegistry<AbstractConfigSerializable<?>> CONFIG_SERIALIZER = new BFAPIRegistry<AbstractConfigSerializable<?>>("config_serializers", (i)->{
		return BFAPIRegistry.CONFIG_SERIALIZER.getEntries().stream().filter((config)->config.getValue().getSerializableClass().equals(i.getSecond().getClass())).toList().size()<=0;
	});
	public static final BFAPIRegistry<Permission> PERMISSIONS = new BFAPIRegistry<Permission>("permissions");
	public static final BFAPIRegistry<IPermissionDatabase> PERMISSION_DATABASES = new BFAPIRegistry<IPermissionDatabase>("permission_databases");
	
	private @NotNull Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake = (i)->true;
	
	private BFAPIRegistry(String name) {
		this(name, null);
	}
	
	private BFAPIRegistry(String name, @Nullable Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake) {
		this(name, onIntake, true);
	}
	
	private BFAPIRegistry(String name, @Nullable Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake, boolean stable) {
		this(ofRegistry(new Identifier(ID, name)), stable?Lifecycle.stable():Lifecycle.experimental(), onIntake);
	}
	
	private BFAPIRegistry(RegistryKey<? extends Registry<T>> key, Lifecycle cycle, @Nullable Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake) {
		super(key, cycle);
		if (onIntake!=null) { this.onIntake = onIntake; }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V extends T> @Nullable V add(RegistryKey<T> key, V entry, Lifecycle lifecycle) {
		if (this.contains(key)) { return (@Nullable V) this.get(key); }
		if (!this.onIntake.apply(TriInput.of(key, entry, lifecycle))) { return null; }
		return super.add(key, entry, lifecycle);
	}
	
	public static class MinecraftGameRule<T> extends GameRules.Rule<MinecraftGameRule<T>> {
		
		private AbstractGamerule<T> inner;
		
		static <T> GameRules.Type<MinecraftGameRule<T>> create(AbstractGamerule<T> gameRule) {
			
			return null;
		}
		
		public MinecraftGameRule(Type<MinecraftGameRule<T>> a, AbstractGamerule<T> gameRule) {
			super(a);
			this.inner = gameRule;
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
		protected MinecraftGameRule<T> getThis() { return this; }

		@Override
		protected MinecraftGameRule<T> copy() { return new MinecraftGameRule<T>(type, inner); }

		@Override
		public void setValue(MinecraftGameRule<T> rule, MinecraftServer server) { this.inner.setValue(rule.getInner().getValue()); }
		
		public AbstractGamerule<T> getInner() { return this.inner; }
		
	}
	
}
