package com.bb1.api.gamerules;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Type;

public class BFAPIGameRuleProvider implements GameRuleProvider {

	public BFAPIGameRuleProvider() { GameRuleManager.getInstance().registerProvider(this); }
	
	@Override
	public String getProviderName() { return "BFAPIGameRuleProvider"; }
	
	@Override
	public void registerGameRule(GameRule<?> gameRule) {
		GameRules.register(gameRule.getName(), gameRule.getCategory(), MinecraftGameRule.create(gameRule));
	}
	
	public static class MinecraftGameRule<T> extends GameRules.Rule<MinecraftGameRule<T>> {
		
		private GameRule<T> inner;
		
		static <T> GameRules.Type<MinecraftGameRule<T>> create(GameRule<T> gameRule) {
			return new Type<MinecraftGameRule<T>>(StringArgumentType::greedyString, (a)->new MinecraftGameRule<T>(a, gameRule), (b,c)->{
				// Put something here to log eventually
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
