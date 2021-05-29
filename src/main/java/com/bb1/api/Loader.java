package com.bb1.api;

import com.bb1.api.commands.Command;
import com.bb1.api.commands.SubCommand;
import com.bb1.api.commands.tab.ITabable;
import com.bb1.api.commands.tab.TabablePlayer;
import com.bb1.api.commands.tab.TabableString;
import com.bb1.api.commands.tab.TabableStrings;
import com.bb1.api.commands.tab.TabableSubCommand;
import com.bb1.api.translations.TranslationCommand;
import com.bb1.api.translations.TranslationManager;

import fr.catcore.server.translations.api.resource.language.TranslationsReloadListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
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
public class Loader implements DedicatedServerModInitializer, TranslationsReloadListener {
	/**
	 * Formats a {@link TranslatableText} in the format "bradsfabricapi." + <i>key</i>;
	 * 
	 * @param key The key for the text
	 */
	public static TranslatableText getTranslatableText(String key) {
		return new TranslatableText("bradsfabricapi."+key);
	}
	
	private static MinecraftServer minecraftServer;
	
	public static void setMinecraftServer(MinecraftServer server) {
		minecraftServer = server;
	}
	
	public static MinecraftServer getMinecraftServer() {
		return minecraftServer;
	}
	
	public static ServerPlayerEntity getServerPlayerEntity(ServerCommandSource source) {
		try {
			return source.getPlayer();
		} catch (Exception e) {}
		return null;
	}
	
	@Override
	public void onInitializeServer() {
		
		SubCommand a = new SubCommand() {
			
			@Override
			public int execute(ServerCommandSource source, String alias, String[] params) {
				source.sendFeedback(new LiteralText("a"), false);
				return 0;
			}
			
			@Override
			public String getCommandUsage() {
				return "";
			}
			
			@Override
			public String getCommandName() {
				return "a";
			}
			
			@Override
			public ITabable[] getParams() {
				return new ITabable[] {new TabableStrings("owo", "hi", "ayo", ":)"), new TabableString("q"), new TabableString("e")};
			}
			
		};
		
		SubCommand b = new SubCommand() {
			
			@Override
			public int execute(ServerCommandSource source, String alias, String[] params) {
				source.sendFeedback(new LiteralText("b"), false);
				return 0;
			}
			
			@Override
			public String getCommandUsage() {
				return "";
			}
			
			@Override
			public String getCommandName() {
				return "b";
			}
			
			@Override
			public ITabable[] getParams() {
				return new ITabable[] {new TabablePlayer()};
			}
			
		};
		
		new Command() {

			@Override
			public int execute(ServerCommandSource source, String alias, String[] params) {
				source.sendFeedback(new LiteralText("ayo!" + params.length), false);
				return 1;
			}

			@Override
			public String getName() {
				return "test";
			}

			@Override
			public String getDescription() {
				return "A test command";
			}
			
			public ITabable[] getParams() {
				return new ITabable[] {new TabableSubCommand(a,b)};
			}
			
		}.register();
		
		registerCommands();
	}
	
	protected void registerCommands() {
		new TranslationCommand().register();
	}
	/**
	 * Called when translations should be reloaded
	 */
	@Override
	public void reload() {
		TranslationManager.get().pushAllTranslations(true); // TODO: idfk
	}

}
