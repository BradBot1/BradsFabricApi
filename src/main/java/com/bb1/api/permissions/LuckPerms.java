package com.bb1.api.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.adapters.AbstractAdapter;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.util.Tristate;

public final class LuckPerms extends AbstractAdapter implements PermissionProvider {

	private net.luckperms.api.LuckPerms luckPerms = LuckPermsProvider.get();
	private UserManager userManager = luckPerms.getUserManager();
	
	public LuckPerms() { super("luckperms"); }
	
	@Override
	public void load() { PermissionManager.getInstance().registerProvider(this); }

	@Override
	public void register(Permission permission) { }

	@Override
	public void givePermission(@NotNull UUID uuid, @NotNull String permission) {
		User user = getUser(uuid);
		user.data().add(PermissionNode.builder(permission).build());
		luckPerms.getUserManager().saveUser(user);
	}

	@Override
	public void takePermission(@NotNull UUID uuid, @NotNull String permission) {
		User user = getUser(uuid);
		user.data().remove(PermissionNode.builder(permission).build());
		luckPerms.getUserManager().saveUser(user);
	}

	@Override
	public Boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) {
		return convertTriState(getUser(uuid).getCachedData().getPermissionData().checkPermission(permission));
	}

	@Override
	public Map<String, Boolean> getPermissions(@NotNull UUID uuid) { return new HashMap<String, Boolean>(); }
	/** This method is blocking! */
	private User getUser(UUID uuid) { return (userManager.isLoaded(uuid)) ? userManager.getUser(uuid) : userManager.loadUser(uuid).join(); }
	
	private Boolean convertTriState(Tristate tristate) {
		return switch (tristate) {
			case TRUE -> true;
			case FALSE -> false;
			case UNDEFINED -> null;
		};
	}
	
}
