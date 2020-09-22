package net.lielamar.core.interfaces.moduls;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import bukkit.lielamar.completepermissions.modules.BukkitUser;

public interface User {
	
	Map<String, Group> getGroups();
	List<String> getPermissions();
	String getPrefix();
	String getSuffix();
	Player getPlayer();
	
	void setGroups(Map<String, Group> groups);
	void setPermissions(List<String> permissions);
	void setPrefix(String prefix);
	void setSuffix(String suffix);
	
	Group getGroupFormat();
	String getFinalPrefix(Group groupFormat);
	String getFinalSuffix(Group groupFormat);
	
	BukkitUser reloadPlayer();
	BukkitUser reloadName(String prefix, String suffix);
	BukkitUser reloadAboveHeadName(String prefix, String suffix, String action);
	PermissionAttachment getPermissionsAttachement();
	void setPermissionsAttachement(PermissionAttachment permissionsAttachement);
	BukkitUser reloadPermissions();
	
	boolean isRelated(Group group);
	
	String toString();
}
