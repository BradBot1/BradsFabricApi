package com.bb1.fabric.bfapi.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bb1.fabric.bfapi.Loader;
import com.bb1.fabric.bfapi.nbt.mark.Markable;
import com.bb1.fabric.bfapi.utils.Container;
import com.bb1.fabric.bfapi.utils.Field;
import com.bb1.fabric.bfapi.utils.Inputs.QuintInput;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public abstract class HitMixin {
	
	@Shadow public abstract Iterable<ItemStack> getArmorItems();
	
	@Inject(method = "damage", at = @At("HEAD"))
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
		Entity entity = (Entity) (Object) this;
		Container<Boolean> container = new Container<Boolean>();
		Entity s = source.getSource();
		if (s==null) {
			Loader.MARK_ENTITY_HIT.emit(QuintInput.of(Field.of(entity), entity.getWorld(), null, null, container));
		} else {
			Loader.MARK_ENTITY_HIT.emit(QuintInput.of(Field.of(entity), entity.getWorld(), Field.of(s), s instanceof PlayerEntity p ? p.getInventory().getMainHandStack() : s.getItemsHand().iterator().next(), container));
		}
		if (!container.getValue()) {
			callback.setReturnValue(false);
			callback.cancel();
		}
		for (ItemStack is : getArmorItems()) {
			Markable mark = Markable.getMarkable(is);
			if (mark.hasMarks()) {
				Loader.MARK_ARMOUR_USED.emit(QuintInput.of(is, entity.getWorld(), entity.getBlockPos(), Field.of(entity), container));
				if (!container.getValue()) {
					callback.setReturnValue(false);
					callback.cancel();
				}
			}
		}
	}
	
}
