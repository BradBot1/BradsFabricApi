package com.bb1.api.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.providers.Provider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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
public final class Events {
	/** Events that relate to players */
	public static class PlayerEvents {
		/** Called when a message is sent to a client<br><i>(cancelling only stops the message being sent to the client as this event is called after the message has already been handled by the server)</i> */
		public static final Event<ChatEvent> MESSAGE_EVENT = new Event<ChatEvent>();
		/** Called when a minecraft loads data from the players .dat file */
		public static final Event<PlayerNBTReadEvent> PLAYER_NBT_READ_EVENT = new Event<PlayerNBTReadEvent>();
		
		public static final Event<PlayerNBTWriteEvent> PLAYER_NBT_WRITE_EVENT = new Event<PlayerNBTWriteEvent>();
		
		private PlayerEvents() { }
		
		public static class PlayerNBTReadEvent implements PlayerEvent {
			
			private final ServerPlayerEntity player;
			
			private final NbtCompound nbt;
			
			public PlayerNBTReadEvent(@NotNull ServerPlayerEntity player, @NotNull NbtCompound nbt) { this.player = player; this.nbt = nbt; }

			@Override
			public PlayerEntity getPlayerEntity() { return player; }
			@Nullable
			public NbtElement getNBT(@NotNull String key) { return this.nbt.get(key); }
			
		}
		
		public static class PlayerNBTWriteEvent implements PlayerEvent {
			
			private final ServerPlayerEntity player;
			
			private final NbtCompound nbt;
			
			public PlayerNBTWriteEvent(@NotNull ServerPlayerEntity player, @NotNull NbtCompound nbt) { this.player = player; this.nbt = nbt; }

			@Override
			public ServerPlayerEntity getPlayerEntity() { return player; }
			
			public void addNBT(@NotNull String key, @NotNull NbtElement nbt) { this.nbt.put(key, nbt); }
			
		}
		
		public static class ChatEvent implements CancellableEvent {
			
			@NotNull private final GameMessageS2CPacket packet;
			@Nullable private final ServerPlayerEntity reciever;
			private boolean cancel = false;
			
			public ChatEvent(@NotNull GameMessageS2CPacket packet, @Nullable ServerPlayerEntity reciever) { this.packet = packet; this.reciever = reciever; }
			
			@Nullable
			public UUID getSender() { return this.packet.getSender(); }
			
			@NotNull
			public MutableText getMessage() { return this.packet.getMessage().shallowCopy(); }
			
			public void setMessage(@NotNull Text text) { this.packet.message = text; }
			
			@NotNull
			public MessageType getMessageType() { return this.packet.getLocation(); }
			
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
	/** Events that minecraft throws */
	public static class GameEvents {
		/** Called when mc autosaves */
		public static final Event<MinecraftServer> AUTOSAVE_EVENT = new Event<MinecraftServer>();

		public static final Event<MinecraftServer> TICK_EVENT = new Event<MinecraftServer>();
		/** Called when minecraft performs a reload */
		public static final Event<MinecraftServer> RELOAD_EVENT = new Event<MinecraftServer>();
		/** Called after minecraft loads fully */
		public static final Event<MinecraftServer> START_EVENT = new Event<MinecraftServer>();
		/** Called when minecraft beings to stop */
		public static final Event<MinecraftServer> STOP_EVENT = new Event<MinecraftServer>();
		
		private GameEvents() { }
		
		
	}
	/** Events specifically tied to the API */
	public static class APIEvents {
		/** Called when a provider registers itself */
		public static final Event<Provider> PROVIDER_REGISTRATION_EVENT = new Event<Provider>();
		/** Called when a provider requests information<br><i>(like when the permission provider wants permissions)</i> */
		public static final Event<ProviderInformationEvent> PROVIDER_INFO_EVENT = new Event<ProviderInformationEvent>();
		
		private APIEvents() { }
		
		public static class ProviderInformationEvent {
			
			private final Provider provider;
			
			private final List<Object> objects = new ArrayList<Object>();
			
			public ProviderInformationEvent(@NotNull Provider provider) { this.provider = provider; }
			
			public Provider getProvider() { return this.provider; }
			
			public void give(Object object) { this.objects.add(object); }
			
			public <T> Collection<T> get(Class<T> type) {
				List<T> list = new ArrayList<T>();
				for (Object object : objects) {
					try {
						list.add(type.cast(object));
					} catch (Throwable t) {}
				}
				return list;
			}
			
		}
		
	}

	private Events() { }
	
	
}
