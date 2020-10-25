package com.lielamar.completepermissions.permission;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.lielamar.completepermissions.CompletePermissions;

public class CustomPermissions extends PermissibleBase {

	private Player p;
    private Map<String, PermissionAttachmentInfo> permissions;
    private Permissible oldPermissible = new PermissibleBase(null);
	
	public CustomPermissions(Player p) {
		super(p);
		this.p = p;
		permissions = new LinkedHashMap<String, PermissionAttachmentInfo>() {

			private static final long serialVersionUID = -4680849121090785534L;

			@Override
			public PermissionAttachmentInfo put(String k, PermissionAttachmentInfo v) {
				PermissionAttachmentInfo exists = this.get(k);
				if(exists != null) 
					return exists;
				return super.put(k, v);
			}
		};
	}

	public Map<String, PermissionAttachmentInfo> getPermissions() {
		return permissions;
	}

	public void setPermissions(Map<String, PermissionAttachmentInfo> permissions) {
		this.permissions = permissions;
	}
	
	public Permissible getOldPermissible() {
		return oldPermissible;
	}

	public void setOldPermissible(Permissible oldPermissible) {
		this.oldPermissible = oldPermissible;
	}
	
	/**
	 * @param permission  Permission to check
	 * @return            Whether or not the player has the permission
	 */
	@Override
	public boolean hasPermission(String permission) {
		if(CompletePermissions.getInstance() == null) return false;
		if(CompletePermissions.getInstance().getUserManager() == null) return false;
		if(CompletePermissions.getInstance().getUserManager().getPlayer(p) == null) return false;
		if(CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement() == null) return false;
		if(CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions() == null) return false;
		
		boolean perm = (CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions().keySet().contains(permission) || children(permission)
				|| CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions().keySet().contains("*"));
		return perm;	
	}
	
	/**
	 * @param permission   Permission to check
	 * @return             Whether or not the player has the permission
	 */
	@Override
	public boolean hasPermission(Permission permission) {
		boolean perm = (CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions().keySet().contains(permission.getName()) || children(permission.getName())
				|| CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions().keySet().contains("*"));
		return perm;
	}

	/**
	 * @param permission   Permission to check
	 * @return             Whether or not the player has a parent relationship with the permission
	 */
	public boolean children(String permission) {
		String[] parts = permission.split("\\.");
		if(parts.length > 0) {
			String part = parts[0];
			for(int i = 0; i < parts.length; i++) {
				if(CompletePermissions.getInstance().getUserManager().getPlayer(p).getPermissionsAttachement().getPermissions().keySet().contains(part + ".*")) return true;
				if(i != 0)
					part += "." + parts[i];
			}
		}
		return false;
	}
}
