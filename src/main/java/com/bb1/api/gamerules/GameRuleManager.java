package com.bb1.api.gamerules;

import com.bb1.api.managers.AbstractInputtableManager;

public class GameRuleManager extends AbstractInputtableManager<GameRuleProvider, GameRule<?>> {
	
	private static final GameRuleManager INSTANCE = new GameRuleManager();
	
	public static GameRuleManager getInstance() { return INSTANCE; }
	
	private GameRuleManager() { }
	
	@Override
	protected void onRegister(GameRuleProvider provider) {
		for (GameRule<?> gameRule : getInput()) {
			provider.registerGameRule(gameRule);
		}
	}
	
	@Override
	protected void onInput(GameRule<?> input) {
		for (GameRuleProvider provider : getProviders()) {
			provider.registerGameRule(input);
		}
	}
	
}
