package com.bb1.api.mixins;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.api.events.Events;
import com.bb1.api.events.Events.ChatEvent;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ClientConnection.class)
public class ChatMixin {
	
	@Shadow public PacketListener packetListener;
	
	@Inject(method = "sendImmediately(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
	public void inject(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> listener, CallbackInfo callbackInfo) throws IOException {
		if (packet==null) return;
		if (packet instanceof GameMessageS2CPacket) {
			ChatEvent chatEvent = new ChatEvent((GameMessageS2CPacket) packet, get());
			Events.MESSAGE_EVENT.onEvent(chatEvent);
			if (chatEvent.isCancelled()) callbackInfo.cancel();
		}
	}
	
	private ServerPlayerEntity get() {
		try {
			return ((ServerPlayNetworkHandler)packetListener).player;
		} catch (Throwable e) {
			return null;
		}
	}
	
}
