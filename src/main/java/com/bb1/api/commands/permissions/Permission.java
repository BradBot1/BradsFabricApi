package com.bb1.api.commands.permissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.bb1.api.providers.CommandProvider;

import java.util.Objects;

/**
 * OpLevel interpretation is as follows:<br>
 * 1-4 -> as normal<br>
 * 0 -> default permission <i>(players will <b>always</b> be able to use the command)</i><br>
 * -1 -> not obtainable <i>(players will <b>never</b> be able to use the command)</i><br>
 * -2 -> undefined <i>(basically an optional permission)</i><br>
 * -3 -> external <i>(for commands that were never register to {@link CommandProvider}, when used the default requirement is used)</i>
 */
public final class Permission {

    private final String permission;

    private final int opLevel;

    public Permission(@NotNull String permission, @Nullable int opLevel) {
        this.permission = permission;
        this.opLevel = opLevel;
    }

    public final String permission() { return this.permission; }

    public final int opLevel() { return this.opLevel; }

    @Override
    public boolean equals(Object obj) {
        return obj!=null && obj instanceof Permission && ((Permission) obj).permission().equals(permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission, opLevel);
    }

    @Override
    public String toString() {
        return "Permission[permission="+permission+",opLevel="+opLevel+"]";
    }

}