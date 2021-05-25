package com.bb1.api.events;

import net.minecraft.entity.player.PlayerEntity;
/**
 * For showing that the event involves a player
 */
public interface PlayerEvent {
	/**
	 * Returns the player involved in the event
	 */
	public PlayerEntity getPlayerEntity();
}
