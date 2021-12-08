package com.bb1.fabric.bfapi.permissions.database;

import com.bb1.fabric.bfapi.permissions.Permission;
import com.bb1.fabric.bfapi.registery.IRegisterable;
import com.bb1.fabric.bfapi.utils.Field;

import net.minecraft.entity.Entity;

public interface IPermissionDatabase extends IRegisterable {
	
	public boolean canHavePermission(Field<Entity> field, Permission permission);
	
	public boolean hasPermission(Field<Entity> field, Permission permission);
	
	public void setPermission(Field<Entity> field, Permission permission, boolean value);
	
	// extension values
	
	public int getExtensionValue(Field<Entity> field, Permission permission);
	
	public void setPermission(Field<Entity> field, Permission permission, int extensionValue);
	
}
