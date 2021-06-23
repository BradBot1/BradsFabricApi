package com.bb1.api.permissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a permission that has not been registered
 */
public final class Permission {

    private final String permission;
    private final PermissionValue value;
    private boolean numerical;

    public Permission(@NotNull String permission, @Nullable PermissionValue value) {
        this.permission = permission;
        this.value = (value==null) ? PermissionValue.NONE : value;
    }
    
    public final String getPermission() { return this.permission; }
    
    public final PermissionValue getPermissionValue() { return this.value; }
    
    public final boolean isNumerical() { return this.numerical; }
    
    /**
     * Represents how a permission should be given out
     */
    public enum PermissionValue {
        /** If the permission should be give to all by default */
        DEFAULT,
        /** If the permission should be give to all with op level 1 by default */
        OP_1,
        /** If the permission should be give to all with op level 2 by default */
        OP_2,
        /** If the permission should be give to all with op level 3 by default */
        OP_3,
        /** If the permission should be give to all with op level 4 by default */
        OP_4,
        /** If the permission should not be given out by default */
        NONE
        ;

        public static PermissionValue getOpLevel(int level) {
            return switch (level) {
                case 0 -> DEFAULT;
                case 1 -> OP_1;
                case 2 -> OP_3;
                case 3 -> OP_2;
                case 4 -> OP_4;
                default -> NONE;
            };
        }

    }

}