package com.lielamar.completepermissions.permission;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.lielamar.utils.bukkit.utils.BukkitReflectionManager;

public final class PermissibleInjector {

    private static final Field HUMAN_ENTITY_PERMISSIBLE_FIELD;
    private static final Field PERMISSIBLE_BASE_ATTATCHMENTS_FIELD;

    static {
        try {
            Field humanEntityPermissibleField;
            humanEntityPermissibleField = BukkitReflectionManager.getClass("org.bukkit.craftbukkit", "entity.CraftHumanEntity").getDeclaredField("perm");
            humanEntityPermissibleField.setAccessible(true);

            HUMAN_ENTITY_PERMISSIBLE_FIELD = humanEntityPermissibleField;

            PERMISSIBLE_BASE_ATTATCHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
            PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
	public static Permissible inject(Player p, Permissible newPermissible) {
        try {
            PermissibleBase oldPermissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);

            if (newPermissible instanceof PermissibleBase) {
				List<PermissionAttachmentInfo> listOfAttachments = (List<PermissionAttachmentInfo>) PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.get(newPermissible);
            	List<PermissionAttachmentInfo> listOfOldAttachements = (List<PermissionAttachmentInfo>) PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.get(oldPermissible);
                listOfAttachments.addAll(listOfOldAttachements);
            }

            HUMAN_ENTITY_PERMISSIBLE_FIELD.set(p, newPermissible);
            return oldPermissible;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Permissible eject(Player p) {
        try {
            Permissible permissible = (Permissible) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);
            
            if (permissible instanceof CustomPermissions) {
                HUMAN_ENTITY_PERMISSIBLE_FIELD.set(p, ((CustomPermissions) permissible).getOldPermissible());
                return (CustomPermissions) permissible;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PermissibleBase getPermissible(Player p) {
        try {
            PermissibleBase permissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);
            return permissible;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}