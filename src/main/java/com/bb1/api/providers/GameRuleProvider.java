package com.bb1.api.providers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.bb1.api.gamerules.GameRule;

public interface GameRuleProvider extends Provider {
	
	public void register(@NotNull GameRule<?> gameRule);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("GameRuleProvider | "+getProviderName()); }
	
}
