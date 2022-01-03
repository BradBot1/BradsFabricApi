package com.bb1.fabric.bfapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.events.Event;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Inputs.Input;
import com.bb1.fabric.bfapi.utils.Inputs.QuadInput;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class GameObjects {
	
	private static MinecraftServer minecraftServer;
	
	public static void setMinecraftServer(@NotNull MinecraftServer server) { minecraftServer = server; }
	
	public static @Nullable MinecraftServer getMinecraftServer() { return minecraftServer; }
	
	private static net.minecraft.server.command.CommandManager commandManager;
	
	public static void setCommandManager(net.minecraft.server.command.CommandManager commandManager2) { commandManager = commandManager2; }
	
	public static net.minecraft.server.command.CommandManager getCommandManager() { return commandManager; }
	
	
	public static final class GameEvents {
		
		public static final Event<Input<MinecraftServer>> TICK = new Event<Input<MinecraftServer>>("minecraft:tick");
		
		public static final Event<Input<MinecraftServer>> SERVER_START = new Event<Input<MinecraftServer>>("minecraft:server_start");
		
		public static final Event<Input<MinecraftServer>> SERVER_STOP = new Event<Input<MinecraftServer>>("minecraft:server_stop");
		
		public static final Event<Input<CommandDispatcher<ServerCommandSource>>> COMMAND_REGISTRATION = new Event<Input<CommandDispatcher<ServerCommandSource>>>("minecraft:command_registration");
		
		public static final Event<Input<Void>> SERVER_RELOAD = new Event<Input<Void>>("minecraft:server_reload");
		
		public static final Event<QuadInput<PlayerEntity, BlockPos, World, Container<Boolean>>> PLAYER_ATTEMPT_MODIFICATION = new Event<QuadInput<PlayerEntity, BlockPos, World, Container<Boolean>>>("minecraft:server_reload");
		
		public static final Event<QuintInput<ItemStack, World, @Nullable BlockPos, @Nullable Entity, Container<Boolean>>> ITEM_USE = new Event<QuintInput<ItemStack, World, @Nullable BlockPos, @Nullable Entity, Container<Boolean>>>("minecraft:item_use");
		
	}
	
}
