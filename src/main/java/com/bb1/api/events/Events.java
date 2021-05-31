package com.bb1.api.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.ApiConfig;
import com.bb1.api.Loader;
import com.bb1.api.commands.CommandManager;
import com.bb1.api.permissions.PermissionManager;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.server.MinecraftServer;

public final class Events {
	/** Called after the API loads fully */
	public static final Event<LoadEvent> LOAD_EVENT = new Event<LoadEvent>();
	/** Called when the API starts to unload */
	public static final Event<UnloadEvent> UNLOAD_EVENT = new Event<UnloadEvent>();

	private Events() { }
	
	public static class LoadEvent {
		/** May be null if disabled in {@link ApiConfig} */
		@Nullable
		public MinecraftServer getMinecraftServer() { return Loader.getMinecraftServer(); }
		
		@NotNull
		public CommandManager getCommandManager() { return CommandManager.get(); }
		
		@NotNull
		public TranslationManager getTranslationManager() { return TranslationManager.get(); }
		/** May be null if disabled in {@link ApiConfig} */
		@Nullable
		public PermissionManager getPermissionManager() { return getConfig().loadPermissionModule ? PermissionManager.get() : null; }
		
		@NotNull
		public ApiConfig getConfig() { return Loader.CONFIG; }
		
	}
	/**
	 * Same as the load event but with a different name
	 */
	public static class UnloadEvent extends LoadEvent { }
	
}
