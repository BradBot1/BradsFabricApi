package com.bb1.fabric.bfapi.registery;

import static com.bb1.fabric.bfapi.Constants.ID;
import static net.minecraft.util.registry.RegistryKey.ofRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.config.AbstractConfigSerializable;
import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.gamerules.AbstractGamerule;
import com.bb1.fabric.bfapi.nbt.mark.INbtMarkListener;
import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.permissions.database.IPermissionDatabase;
import com.bb1.fabric.bfapi.recipe.AbstractRecipe;
import com.bb1.fabric.bfapi.text.parser.TextParserLookup;
import com.bb1.fabric.bfapi.utils.ExceptionWrapper;
import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.bb1.fabric.bfapi.utils.Inputs.TriInput;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Lifecycle;

import net.minecraft.recipe.Recipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Type;

public class Registry<T> {
	
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
		return BFAPIRegistry.CONFIG_SERIALIZER.getEntrySet().stream().filter((config)->config.getValue().getSerializableClass().equals(i.getSecond().getClass())).toList().size()<=0;
	});
	public static final BFAPIRegistry<Permission> PERMISSIONS = new BFAPIRegistry<Permission>("permissions");
	public static final BFAPIRegistry<IPermissionDatabase> PERMISSION_DATABASES = new BFAPIRegistry<IPermissionDatabase>("permission_databases");
	private static final Queue<Recipe<?>> RECIPES_TO_BE_ADDED = new LinkedList<>();	
	@SuppressWarnings("resource")
	public static final BFAPIRegistry<AbstractRecipe> RECIPES = new BFAPIRegistry<AbstractRecipe>("recipes", (i)->{
		Recipe<?> recipe = (Recipe<?>) (Object) i.getSecond().buildWrappedRecipe(i.get().getValue());
		if (GameObjects.getMinecraftServer()==null) {
			if (RECIPES_TO_BE_ADDED.isEmpty()) {
				GameObjects.GameEvents.SERVER_START.addHandler((i2)->{
					List<Recipe<?>> recipes = new ArrayList<Recipe<?>>(); // MAYBE: work out a good default length?
					for (Map<Identifier, Recipe<?>> map : GameObjects.getMinecraftServer().getRecipeManager().recipes.values()) {
						recipes.addAll(map.values());
					}
					Recipe<?> r = RECIPES_TO_BE_ADDED.poll();
					while (r!=null) {
						recipes.add(r);
						r = RECIPES_TO_BE_ADDED.poll();
					}
					GameObjects.getMinecraftServer().getRecipeManager().setRecipes(recipes);
				});
			}
			RECIPES_TO_BE_ADDED.add(recipe);
			return true;
		}
		List<Recipe<?>> recipes = new ArrayList<Recipe<?>>(); // MAYBE: work out a good default length?
		for (Map<Identifier, Recipe<?>> map : GameObjects.getMinecraftServer().getRecipeManager().recipes.values()) {
			recipes.addAll(map.values());
		}
		recipes.add(recipe);
		GameObjects.getMinecraftServer().getRecipeManager().setRecipes(recipes);
		return true;
	});
	public static final BFAPIRegistry<TextParserLookup> TEXT_PARSER_LOOKUPS = new BFAPIRegistry<TextParserLookup>("text_parser_lookups");
	
	private @NotNull Function<TriInput<Identifier, T, Lifecycle>, Boolean> onIntake = (i)->true;
	private final Map<Identifier, T> map = new ConcurrentHashMap<Identifier, T>();
	
	private Registry(String name) {
		this(name, null);
	}
	
	private Registry(String name, @Nullable Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake) {
		this(name, onIntake, true);
	}
	
	private Registry(String name, @Nullable Function<TriInput<RegistryKey<T>, T, Lifecycle>, Boolean> onIntake, boolean stable) {
		this(ofRegistry(new Identifier(ID, name)), stable?Lifecycle.stable():Lifecycle.experimental(), onIntake);
	}
	
	public Registry(In) {
		if (onIntake!=null) { this.onIntake = onIntake; }
	}
	
	@Override
	public @Nullable RegistryEntry<T> add(RegistryKey<T> key, T entry, Lifecycle lifecycle) {
		if (this.contains(key)) { return this.getEntry(key).get(); }
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
