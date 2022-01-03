package com.bb1.fabric.bfapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Inputs.QuadInput;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/**
 * 
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
 * This mixin simply adds an event to the player being able to interact with a block
 * 
 * @author BradBot_1
 */
@Mixin(World.class)
public abstract class CanPlayerModifyMixin {
	
	@Inject(method = "canPlayerModifyAt", at = @At("RETURN"), cancellable = true)
	public void callEventAndValidate(PlayerEntity player, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
		Container<Boolean> container = new Container<Boolean>(true);
		GameObjects.GameEvents.PLAYER_ATTEMPT_MODIFICATION.emit(QuadInput.of(player, pos, (World)(Object)this, container));
		callback.setReturnValue(callback.getReturnValue() && container.getValue());
	}
	
}
