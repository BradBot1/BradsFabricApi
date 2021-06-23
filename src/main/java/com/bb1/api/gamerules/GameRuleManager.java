package com.bb1.api.gamerules;

import com.bb1.api.managers.AbstractInputtableManager;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
