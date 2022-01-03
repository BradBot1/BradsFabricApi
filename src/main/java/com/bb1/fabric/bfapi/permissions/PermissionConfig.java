package com.bb1.fabric.bfapi.permissions;

import static com.bb1.fabric.bfapi.Constants.ID;

import com.bb1.fabric.bfapi.config.Config;
import com.bb1.fabric.bfapi.config.ConfigComment;
import com.bb1.fabric.bfapi.config.ConfigName;

import net.minecraft.util.Identifier;

public class PermissionConfig extends Config {

	public PermissionConfig() {
		super(new Identifier(ID, "permissions"));
	}
	
	@ConfigComment(contents = "If the default permission handler should be registered, you may want to turn this off if you have a different handler registered")
	@ConfigName(name = "EnableDefaultPermissionHandler")
	public boolean enabled;
	
	@ConfigComment(contents = "The path the default permission handler should be registered under, should only use a-z0-9")
	@ConfigName(name = "DefaultPermissionHandlerID")
	public String dbName = "default_handler";

}
