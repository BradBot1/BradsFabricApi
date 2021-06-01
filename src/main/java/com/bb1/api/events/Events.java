package com.bb1.api.events;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.ApiConfig;
import com.bb1.api.Loader;
import com.bb1.api.commands.CommandManager;
import com.bb1.api.permissions.PermissionManager;
import com.bb1.api.translations.TranslationManager;

import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public final class Events {
	/** Called after the API loads fully */
	public static final Event<LoadEvent> LOAD_EVENT = new Event<LoadEvent>();
	/** Called when the API starts to unload */
	public static final Event<UnloadEvent> UNLOAD_EVENT = new Event<UnloadEvent>();
	/** Called when a message is sent to a client<br><i>(cancelling only stops the message being sent to the client as this event is called after the message has already been handled by the server)</i>*/
	public static final Event<ChatEvent> MESSAGE_EVENT = new Event<ChatEvent>();

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
	
	public static class ChatEvent implements CancellableEvent {
		
		@NotNull private final GameMessageS2CPacket packet;
		@Nullable private final ServerPlayerEntity reciever;
		private boolean cancel = false;
		
		public ChatEvent(@NotNull GameMessageS2CPacket packet, @Nullable ServerPlayerEntity reciever) { this.packet = packet; this.reciever = reciever; }
		
		@Nullable
		public UUID getSender() { return this.packet.senderUuid; }
		
		@NotNull
		public MutableText getMessage() { return this.packet.message.shallowCopy(); }
		
		public void setMessage(@NotNull Text text) { this.packet.message = text; }
		
		@NotNull
		public MessageType getMessageType() { return this.packet.location; }
		
		public void setMessageType(@NotNull MessageType messageType) { this.packet.location = messageType; }
		/** Returns the player the message is being sent to, may be null */
		@Nullable
		public ServerPlayerEntity getMessageReciever() { return this.reciever; }

		// Cancelling stuff

		@Override
		public boolean isCancelled() { return this.cancel; }

		@Override
		public void cancel() { this.cancel = true; }
		
	}
	
}
