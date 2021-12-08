package com.bb1.fabric.bfapi.events;

import com.bb1.fabric.bfapi.utils.Inputs.DualInput;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class MinecraftEvents {
	
	public static final Event<DualInput<CommandDispatcher<ServerCommandSource>, CommandManager.RegistrationEnvironment>> COMMAND_REGISTRATION_EVENT = new Event<DualInput<CommandDispatcher<ServerCommandSource>, CommandManager.RegistrationEnvironment>>("minecraft:command_registration");
	
}
