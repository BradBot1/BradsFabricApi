package com.bb1.fabric.bfapi.nbt.mark;

import static com.bb1.fabric.bfapi.Constants.ID;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.events.EventListener;
import com.bb1.fabric.bfapi.registery.BFAPIRegistry;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Field;

import static com.bb1.fabric.bfapi.nbt.mark.Markable.getMarkable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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
 * Sets up and enables all {@link INbtMarkListener}'s to receive events
 * 
 * @author BradBot_1
 */
public class INbtMarkListenerHandler implements EventListener {
	
	@EventHandler(eventIdentifier = ID+":marked_item_used", decomposeArguments = true, logOnFailedBinding = true)
	public void onItemUse(ItemStack usedItemStack, World world, @Nullable BlockPos hitLocation, Field<Entity> usingEntity, Container<Boolean> container) {
		final Markable markable = getMarkable(usedItemStack);
		for (final String mark : markable.getMarks()) {
			BFAPIRegistry.MARK_LISTENER.stream().filter((n)->n.getMark().equals(mark)).forEach((n)->{
				if (n.onItemUse(usedItemStack, world, hitLocation, usingEntity, container.getValue())) {
					container.setValue(true);
				}
			});
		}
	}
	
	@EventHandler(eventIdentifier = ID+":marked_entity_hit", decomposeArguments = true, logOnFailedBinding = true)
	public void onEntityHit(Field<Entity> hitEntity, @Nullable World world, @Nullable Field<Entity> attackingEntity, @Nullable ItemStack usedItemStack, Container<Boolean> container) {
		final Markable markable = hitEntity.get();
		for (final String mark : markable.getMarks()) {
			BFAPIRegistry.MARK_LISTENER.stream().filter((n)->n.getMark().equals(mark)).forEach((n)->{
				if (n.onEntityHit(hitEntity, world, attackingEntity, usedItemStack, container.getValue())) {
					container.setValue(true);
				}
			});
		}
	}
	
	@EventHandler(eventIdentifier = ID+":marked_item_used", decomposeArguments = true, logOnFailedBinding = true)
	public void onArmourUsed(ItemStack usedItemStack, World world, @Nullable BlockPos hitLocation, Field<Entity> usingEntity, Container<Boolean> container) {
		final Markable markable = getMarkable(usedItemStack);
		for (final String mark : markable.getMarks()) {
			BFAPIRegistry.MARK_LISTENER.stream().filter((n)->n.getMark().equals(mark)).forEach((n)->{
				if (n.onItemUse(usedItemStack, world, hitLocation, usingEntity, container.getValue())) {
					container.setValue(true);
				}
			});
		}
	}
	
}
