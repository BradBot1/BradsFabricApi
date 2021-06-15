package com.bb1.api.providers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.Command;

import net.minecraft.entity.player.PlayerEntity;

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
public interface CommandProvider extends Provider {
	
	public void registerCommand(@NotNull Command command);
	
	@Nullable
	public Command getCommandByName(@NotNull String name);
	
	@Override
	public default Logger getProviderLogger() { return LogManager.getLogger("CommandProvider | "+getProviderName()); }
	
	public void disableCommand(PlayerEntity player, String commandName);
	
	public void enableCommand(PlayerEntity player, String commandName);
	
	public boolean isCommandEnabled(PlayerEntity player, String commandName);
	
}
