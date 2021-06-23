package com.bb1.api.gamerules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bb1.api.providers.Provider;

public interface GameRuleProvider extends Provider {
	
	public void registerGameRule(GameRule<?> gameRule);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("GameRuleProvider | "+getProviderName()); }
	
}
