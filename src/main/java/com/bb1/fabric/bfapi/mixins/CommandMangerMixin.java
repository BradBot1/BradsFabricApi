package com.bb1.fabric.bfapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bb1.fabric.bfapi.GameObjects;
import com.bb1.fabric.bfapi.utils.Inputs.Input;

import net.minecraft.server.command.CommandManager;

/**
 * Copyright 2021 BradBot_1
 * 
 * Licensed under the Apache License, Version 2.0 (the "License", "");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http;//www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Mixin(CommandManager.class)
public class CommandMangerMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInstanceMade(CommandManager.RegistrationEnvironment environment, CallbackInfo callbackInfo) {
		CommandManager instance = (CommandManager)(Object)this;
		GameObjects.setCommandManager(instance);
		GameObjects.GameEvents.COMMAND_REGISTRATION.emit(Input.of(instance.getDispatcher()));
	}

}
