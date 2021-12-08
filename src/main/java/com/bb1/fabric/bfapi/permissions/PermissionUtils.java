package com.bb1.fabric.bfapi.permissions;

import static com.bb1.fabric.bfapi.registery.BFAPIRegistry.PERMISSION_DATABASES;

import java.util.Map.Entry;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.bb1.fabric.bfapi.permissions.database.IPermissionDatabase;
import com.bb1.fabric.bfapi.registery.BFAPIRegistry;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

public final class PermissionUtils {
	
	public static final Predicate<Field<Entity>> buildPredicate(final String permission) { return (i)->hasPermission(i, permission); }
	
	public static final boolean canHavePermission(Field<Entity> field, String node) {
		final Permission permission = getPermission(node);
		for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
			if (!entry.getValue().canHavePermission(field, permission)) {
				return false;
			}
		}
		return true;
	}
	
	public static final int getPermissionExtension(Field<Entity> field, String node) {
		if (!canHavePermission(field, node)) { return 0; }
		final Permission permission = getPermission(node);
		int value = 0;
		for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
			int newValue = entry.getValue().getExtensionValue(field, permission);
			if (newValue>value) { value = newValue; }
		}
		return value;
	}
	
	public static final void setPermissionExtension(Field<Entity> field, String node, int level, boolean toAll) {
		if (!canHavePermission(field, node)) { return; }
		final Permission permission = getPermission(node);
		if (toAll) {
			for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
				entry.getValue().setPermission(field, permission, level);
			}
		} else {
			PERMISSION_DATABASES.get(0).setPermission(field, permission, level);
		}
	}
	
	public static final void givePermission(Field<Entity> field, String node, boolean toAll) {
		if (!canHavePermission(field, node)) { return; }
		final Permission permission = getPermission(node);
		if (toAll) {
			for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
				entry.getValue().setPermission(field, permission, true);
			}
		} else {
			PERMISSION_DATABASES.get(0).setPermission(field, permission, true);
		}
	}
	
	public static final void takePermission(Field<Entity> field, String node, boolean toAll) {
		final Permission permission = getPermission(node);
		if (toAll) {
			for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
				entry.getValue().setPermission(field, permission, false);
			}
		} else {
			PERMISSION_DATABASES.get(0).setPermission(field, permission, false);
		}
	}
	
	public static final boolean hasPermission(Field<Entity> field, String node) {
		final Permission permission = getPermission(node);
		for (Entry<RegistryKey<IPermissionDatabase>, IPermissionDatabase> entry : PERMISSION_DATABASES.getEntries()) {
			if (entry.getValue().hasPermission(field, permission)) {
				return true;
			}
		}
		return false;
	}
	
	public static final @Nullable Permission getPermission(String node) {
		for (Entry<RegistryKey<Permission>, Permission> entry : BFAPIRegistry.PERMISSIONS.getEntries()) {
			if (entry.getValue().node().equals(node)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static final @Nullable Identifier getPermissionIdentifer(String node) {
		for (Entry<RegistryKey<Permission>, Permission> entry : BFAPIRegistry.PERMISSIONS.getEntries()) {
			if (entry.getValue().node().equals(node)) {
				return entry.getKey().getValue();
			}
		}
		return null;
	}
	
}
