package com.bb1.api.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.Loader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
/**
 * An extension of {@link AbstractCommand} that only lets players call it
 */
public abstract class AbstractPlayerCommand extends AbstractCommand<ServerCommandSource> {
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
	 * Remaps to {@link #execute(PlayerEntity)}
	 */
	@Override
	public final int execute(CommandContext<ServerCommandSource> context) {
		try {
			ServerPlayerEntity player = context.getSource().getPlayer();
			if (player==null) throw new NullPointerException("catchme"); // Causes the catch block to be ran ;)
			return execute(player);
		} catch (CommandSyntaxException | NullPointerException e) {
			context.getSource().sendError(Loader.getTranslatableText("error.player_only_command"));
			return 0;
		}
	}
	
	public abstract int execute(PlayerEntity playerEntity);
	/**
	 * If the command can only be ran by a player
	 */
	@Override
	public boolean isPlayerOnly() { return true; }
}