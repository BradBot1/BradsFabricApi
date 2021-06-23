package com.bb1.api;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.BFAPICommandProvider;
import com.bb1.api.datapacks.BFAPIDatapackProvider;
import com.bb1.api.events.Events;
import com.bb1.api.gamerules.BFAPIGameRuleProvider;
import com.bb1.api.permissions.LuckPerms;
import com.bb1.api.permissions.LuckoFPAPIv0;
import com.bb1.api.permissions.PatboxPermissionsAPI;
import com.bb1.api.text.BoxOfPlaceholders;
import com.bb1.api.translations.ServerTranslations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
public class Loader implements ModInitializer {
	/**
	 * Formats a {@link TranslatableText} in the format "bradsfabricapi." + <i>key</i>;
	 * 
	 * @param key The key for the text
	 */
	public static TranslatableText getTranslatableText(String key) { return new TranslatableText("bfapi."+key); }
	
	// Minecraft Server
	
	private static MinecraftServer minecraftServer;
	
	public static void setMinecraftServer(MinecraftServer server) { minecraftServer = server; }
	
	public static MinecraftServer getMinecraftServer() { return minecraftServer; }
	
	// CommandManager
	
	private static net.minecraft.server.command.CommandManager commandManager;
	
	public static void setCommandManager(net.minecraft.server.command.CommandManager commandManager2) { commandManager = commandManager2; }
	
	public static net.minecraft.server.command.CommandManager getCommandManager() { return commandManager; }
	
	// Logger
	
	private static final Logger LOGGER = LogManager.getLogger("BFAPI");
	
	// Reloaders
	
	private static final Set<ResourceReloader> RELOADERS = new HashSet<ResourceReloader>();
	
	public static void registerReloader(ResourceReloader reloader) { RELOADERS.add(reloader); }
	
	public static Set<ResourceReloader> getReloaders() { return RELOADERS; }
	
	/**
	 * Returns the servers root directory
	 */
	public static String getRootPath() {
		final String path = PlayerManager.OPERATORS_FILE.getAbsolutePath();
		return path.endsWith("/") ? path.substring(0, path.length()-10) : path.substring(0, path.length()-9);
	}
	
	@Nullable
	public static ServerPlayerEntity getServerPlayerEntity(ServerCommandSource source) {
		try {
			return source.getPlayer();
		} catch (Exception e) {}
		return null;
	}
	
	public static final ApiConfig CONFIG = new ApiConfig();
	
	@Override
	public void onInitialize() {
		CONFIG.load();
		if (CONFIG.debugMode) {
			LOGGER.warn(" ");
			LOGGER.warn(" ");
			LOGGER.warn(" ");
			LOGGER.warn("Debug mode is enabled, it is recommended that you disable debug mode unless it is needed to remove console clutter");
			LOGGER.warn(" ");
			LOGGER.warn(" ");
			LOGGER.warn(" ");
		}
		Events.GameEvents.STOP_EVENT.register((e)->CONFIG.save());
		// Permissions
		new LuckoFPAPIv0();
		new LuckPerms();
		new PatboxPermissionsAPI();
		// Translations
		new ServerTranslations();
		// GameRule
		new BFAPIGameRuleProvider();
		// Datapack
		new BFAPIDatapackProvider();
		// Text
		new BoxOfPlaceholders();
		// Command
		new BFAPICommandProvider();
	}
	
}
