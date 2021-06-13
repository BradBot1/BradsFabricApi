package com.bb1.api.commands;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.permissions.Permission;
import com.bb1.api.commands.tab.ITabable;

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
public abstract class SubCommand implements CommandHandler {
	
	protected final String name;
	
	public SubCommand(@NotNull String name) { this.name = name; }
	
	public final String getName() { return this.name; }
	
	public String getCommandUsage() { return getName(); }
	
	@Nullable
	public Set<String> getAliases() { return null; }
	
	@Nullable
	public Permission getPermission() { return null; }
	
	@Nullable
	public ITabable[] getParams() { return null; }
	
}
