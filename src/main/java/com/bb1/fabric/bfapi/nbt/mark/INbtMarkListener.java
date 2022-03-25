package com.bb1.fabric.bfapi.nbt.mark;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.MARK_LISTENER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INbtMarkListener extends IRegisterable {
	
	public @NotNull String getMark();
	/**
	 * @return if to cancel the event
	 */
	public boolean onItemUse(ItemStack usedItemStack, World world, @Nullable BlockPos hitLocation, Field<Entity> usingEntity, boolean canceled);
	/**
	 * @return if to cancel the event
	 */
	public boolean onEntityHit(Field<Entity> hitEntity, @Nullable World world, @Nullable Field<Entity> attackingEntity, @Nullable ItemStack usedItemStack, boolean canceled);
	/**
	 * @return if to cancel the event
	 */
	public boolean onArmourUsed(ItemStack usedItemStack, World world, @Nullable BlockPos hitLocation, Field<Entity> usingEntity, boolean canceled);
	
	@Override
	default void register(@Nullable Identifier name) {
		MARK_LISTENER.add(name, this);
	}
	
}
