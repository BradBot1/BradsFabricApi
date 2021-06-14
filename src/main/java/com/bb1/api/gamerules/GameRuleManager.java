package com.bb1.api.gamerules;

import com.bb1.api.Loader;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ProviderInformationEvent;
import com.bb1.api.providers.GameRuleProvider;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Type;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class GameRuleManager implements GameRuleProvider {
	
	private static final GameRuleManager INSTANCE = new GameRuleManager();
	
	public static GameRuleManager get() { return INSTANCE; }
	
	private GameRuleManager() {
		if (!Loader.CONFIG.loadGameRuleProvider) return;
		ProviderInformationEvent event = new ProviderInformationEvent(this);
		Events.PROVIDER_INFO_EVENT.onEvent(event);
		for (GameRule<?> gameRule : event.get(GameRule.class)) {
			register(gameRule);
		}
	}
	
	private boolean debug = Loader.CONFIG.debugMode;
	
	@Override
	public void register(GameRule<?> gameRule) {
		GameRules.register(gameRule.getName(), gameRule.getCategory(), MinecraftGameRule.create(gameRule));
		if (debug) getProviderLogger().info("Registered the GameRule "+gameRule.getName());
	}
	
	@Override
	public String getProviderName() { return "GameRuleManager"; }
		
	public static class MinecraftGameRule<T> extends GameRules.Rule<MinecraftGameRule<T>> {
		
		private GameRule<T> inner;
		
		static <T> GameRules.Type<MinecraftGameRule<T>> create(GameRule<T> gameRule) {
			return new Type<MinecraftGameRule<T>>(StringArgumentType::greedyString, (a)->new MinecraftGameRule<T>(a, gameRule), (b,c)->{
				if (get().debug) get().getProviderLogger().info("The custom gamerule \""+c.getInner().getName()+"\" has been changed to "+c.getInner().getValue());
			}, (d,e,f)->{});
		}
		
		public MinecraftGameRule(Type<MinecraftGameRule<T>> a, GameRule<T> gameRule) {
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
		
		public GameRule<T> getInner() { return this.inner; }
		
	}
	
}
