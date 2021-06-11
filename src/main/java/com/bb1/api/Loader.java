package com.bb1.api;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.commands.CommandManager;
import com.bb1.api.config.command.ConfigCommand;
import com.bb1.api.events.Events;
import com.bb1.api.events.Events.LoadEvent;
import com.bb1.api.events.Events.ProviderRegistrationEvent;
import com.bb1.api.permissions.DefaultPermissions;
import com.bb1.api.permissions.PermissionManager;
import com.bb1.api.providers.PermissionProvider;
import com.bb1.api.providers.Provider;
import com.bb1.api.providers.TranslationProvider;
import com.bb1.api.translations.DefaultTranslations;
import com.bb1.api.translations.TranslationManager;
import com.bb1.api.translations.command.TranslationCommand;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
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
public class Loader implements DedicatedServerModInitializer {
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
	
	private static final Set<Provider> PROVIDERS = new HashSet<Provider>();
	
	public static void registerProvider(@NotNull Provider provider) {
		if (PROVIDERS.contains(provider)) return;
		PROVIDERS.add(provider);
		Events.PROVIDER_REGISTRATION_EVENT.onEvent(new ProviderRegistrationEvent(provider));
	}
	
	@Nullable
	public static <T extends Provider> T getProvider(@NotNull Class<T> providerClass) {
		for (Provider provider : PROVIDERS) {
			try {
				T t = providerClass.cast(provider);
				if (t!=null) return t;
			} catch (Throwable e) { }
		}
		return null;
	}
	/**
	 * Returns the servers root directory
	 */
	public static String getRootPath() {
		final String path = PlayerManager.OPERATORS_FILE.getAbsolutePath();
		return path.endsWith("/") ? path.substring(0, path.length()-10) : path.substring(0, path.length()-9);
	}
	
	public static ServerPlayerEntity getServerPlayerEntity(ServerCommandSource source) {
		try {
			return source.getPlayer();
		} catch (Exception e) {}
		return null;
	}
	
	public static final ApiConfig CONFIG = new ApiConfig();
	private final FabricLoader loader = FabricLoader.getInstance();
	
	@Override
	public void onInitializeServer() {
		CONFIG.load();
		// Load translations
		loadTranslations();
		// Load commands
		loadCommands();
		Events.LOAD_EVENT.onEvent(new LoadEvent());
		Events.UNLOAD_EVENT.register((e)->CONFIG.save());
	}
	
	protected void loadProviders() {
		// Load events that effect providers
		Events.LOAD_EVENT.register((event)->{
			for (Provider provider : PROVIDERS) {
				provider.load();
			}
		});
		Events.UNLOAD_EVENT.register((event)->{
			for (Provider provider : PROVIDERS) {
				provider.save();
			}
		});
	}
	
	protected void loadTranslations() {
		Events.PROVIDER_REGISTRATION_EVENT.register((event)->{
			if (event.getProvider() instanceof TranslationProvider) {
				TranslationProvider provider = (TranslationProvider) event.getProvider();
				for (Field field : DefaultTranslations.class.getDeclaredFields()) {
					try {
						TranslatableText text = (TranslatableText) field.get(null);
						provider.registerTranslation(text.getKey(), null);
					} catch (Throwable e) {}
				}
			}
		});
		if (loader.isModLoaded("server_translations") && CONFIG.loadTranslationModule) { registerProvider(TranslationManager.get()); }
		if (CONFIG.loadTranslationCommand) { new ConfigCommand().register(); }
	}
	
	protected void loadPermissions() {
		Events.PROVIDER_REGISTRATION_EVENT.register((event)->{
			if (event.getProvider() instanceof PermissionProvider) {
				PermissionProvider provider = (PermissionProvider) event.getProvider();
				for (Field field : DefaultPermissions.class.getDeclaredFields()) {
					try {
						provider.registerPermission((String) field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}
		});
		if (CONFIG.loadPermissionModule) { registerProvider(PermissionManager.get()); }
		if (loader.isModLoaded("fabric-permissions-api-v0")) {
			PermissionCheckEvent.EVENT.register(new PermissionCheckEvent() {
				
				@Override
				public @NotNull TriState onPermissionCheck(@NotNull CommandSource source, @NotNull String permission) {
					try {
						return getProvider(PermissionProvider.class).hasPermission(((ServerCommandSource)source).getPlayer(), permission) ? TriState.TRUE : TriState.DEFAULT;
					} catch (Throwable e) {
						return TriState.DEFAULT;
					}
				}
				
			});
		}
	}
	
	protected void loadCommands() {
		if (CONFIG.loadTranslationCommand) { new TranslationCommand().register(); }
		if (CONFIG.loadCommandModule) { registerProvider(CommandManager.get()); }
	}
	
}
