package com.bb1.api.events;

import net.minecraft.entity.player.PlayerEntity;

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
/**
 * For showing that the event involves a player
 */
public interface PlayerEvent {
	/**
	 * Returns the player involved in the event
	 */
	public PlayerEntity getPlayerEntity();
}
