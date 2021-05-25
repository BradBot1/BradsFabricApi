package com.bb1.api.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
/**
 * The base command that all other Command's extend
 * 
 * @param <C> The context type the command expects
 */
public abstract class AbstractCommand<C extends CommandSource> {
	/**
	 * Returns the name of the command
	 */
	@NotNull
	public abstract String getName();
	/**
	 * Returns all aliases of the command <i>(alternate names)</i>
	 */
	@Nullable
	public abstract String[] getAliases();
	/**
	 * 
	 * Called when the command is called
	 * 
	 * @param context The {@link CommandContext} of the command
	 */
	public abstract int execute(CommandContext<C> context);
	/**
	 * If the command can only be ran by a player
	 */
	public boolean isPlayerOnly() { return false; }
}