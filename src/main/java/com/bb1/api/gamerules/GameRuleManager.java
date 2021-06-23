package com.bb1.api.gamerules;

import java.util.HashSet;
import java.util.Set;

import com.bb1.api.managers.AbstractManager;

public class GameRuleManager extends AbstractManager<GameRuleProvider> {
	
	private static final GameRuleManager INSTANCE = new GameRuleManager();
	
	public static GameRuleManager getInstance() { return INSTANCE; }
	
	private GameRuleManager() { }
	
	private Set<GameRule<?>> gameRules = new HashSet<GameRule<?>>();
	
	public void registerGameRule(GameRule<?> gameRule) {
		gameRules.add(gameRule);
		for (GameRuleProvider provider : getProviders()) {
			provider.registerGameRule(gameRule);
		}
	}
	
	@Override
	public void registerProvider(GameRuleProvider provider) {
		for (GameRule<?> gameRule : gameRules) {
			provider.registerGameRule(gameRule);
		}
		super.registerProvider(provider);
	}
	
}
