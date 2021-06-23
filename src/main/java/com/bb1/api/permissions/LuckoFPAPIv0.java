package com.bb1.api.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.bb1.api.Loader;
import com.bb1.api.adapters.AbstractAdapter;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.PlayerManager;

public final class LuckoFPAPIv0 extends AbstractAdapter implements PermissionProvider {

	private final PlayerManager manager = Loader.getMinecraftServer().getPlayerManager();
	
	public LuckoFPAPIv0() { super("fabric-permissions-api-v0"); }
	
	@Override
	public void load() { PermissionManager.getInstance().registerProvider(this); }

	@Override
	public void register(Permission permission) { }

	@Override
	public void givePermission(@NotNull UUID uuid, @NotNull String permission) { }

	@Override
	public void takePermission(@NotNull UUID uuid, @NotNull String permission) { }

	@Override
	public Boolean hasPermission(@NotNull UUID uuid, @NotNull String permission) { return convertTriState(Permissions.getPermissionValue(manager.getPlayer(uuid), permission)); }

	@Override
	public Map<String, Boolean> getPermissions(@NotNull UUID uuid) { return new HashMap<String, Boolean>(); }
	
	private Boolean convertTriState(TriState triState) {
		return switch (triState) {
			case TRUE -> true;
			case FALSE -> false;
			case DEFAULT -> null;
		};
	}
	
}
