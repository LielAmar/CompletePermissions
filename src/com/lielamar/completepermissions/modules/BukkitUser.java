package com.lielamar.completepermissions.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.managers.nick.NickManager;
import com.lielamar.utils.core.interfaces.modules.Group;
import com.lielamar.utils.core.interfaces.modules.User;

public class BukkitUser implements User {
	
	private Player p;
	private PermissionAttachment permissionsAttachement;
	private Map<String, Group> groups;
	private List<String> permissions;
	private String prefix;
	private String suffix;
	
	public BukkitUser(Player p, Map<String, Group> groups, List<String> permissions, String prefix, String suffix) {
		this.groups = groups;
		this.permissions = permissions;
		this.prefix = prefix;
		this.suffix = suffix;
		this.p = p;
		permissionsAttachement = this.p.addAttachment(CompletePermissions.getInstance());
		reloadPlayer();
	}
	
	@Override
	public Map<String, Group> getGroups() {
		return this.groups;
	}

	@Override
	public List<String> getPermissions() {
		return this.permissions;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String getSuffix() {
		return this.suffix;
	}

	@Override
	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
		reloadPlayer();
	}

	@Override
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
		reloadPlayer();
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		reloadPlayer();
	}

	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		reloadPlayer();
	}

	@Override
	public Player getPlayer() {
		return this.p;
	}
	
	@Override
	public PermissionAttachment getPermissionsAttachement() {
		return permissionsAttachement;
	}
	
	@Override
	public void setPermissionsAttachement(PermissionAttachment permissionsAttachement) {
		this.permissionsAttachement = permissionsAttachement;
	}
	
	/**
	 * @param group   A {@link com.lielamar.utils.core.interfaces.modules.lielamar.core.interfaces.moduls.Group} object
	 * @return        Whether or not the player is related to the group
	 */
	@Override
	public boolean isRelated(Group group) {
		for (Group g : getGroups().values()) {
			if (g.getName().equalsIgnoreCase(group.getName())) {
				return true;
			}
			for(Group parent : g.getParents().values()) {
				if (parent.getName().equalsIgnoreCase(group.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Group getGroupFormat() {
		Group groupFormat = null;

		if(getGroups().size() == 0)
			groupFormat = CompletePermissions.getInstance().getGroupManager().getDefaultGroup();
		else {
			for(Group group : getGroups().values()) {
				groupFormat = group;
				break;
			}
		}
		return groupFormat;
	}

	@Override
	public String getFinalPrefix(Group groupFormat) {
		String prefix = getPrefix();

		String finalPrefix = prefix;
		
		if(prefix.equalsIgnoreCase("")) {
			finalPrefix = groupFormat.getPrefix();
		}
		
		if(NickManager.nicks != null) {
			if(NickManager.nicks.containsKey(this.p.getUniqueId())) {
				finalPrefix = NickManager.nicks.get(this.p.getUniqueId()).getGroup().getPrefix();
			}
		}
		
		return ChatColor.translateAlternateColorCodes('&', finalPrefix);
	}

	@Override
	public String getFinalSuffix(Group groupFormat) {
		String suffix = getSuffix();

		String finalSuffix = suffix;
		
		if(suffix.equalsIgnoreCase("")) {
			finalSuffix = groupFormat.getSuffix();
		}
		
		if(NickManager.nicks != null) {
			if(NickManager.nicks.containsKey(this.p.getUniqueId()))
				finalSuffix = NickManager.nicks.get(this.p.getUniqueId()).getGroup().getSuffix();
		}
		
		return ChatColor.translateAlternateColorCodes('&', finalSuffix);
	}
	
	/**
	 * Reloads the player
	 * 
	 * @return       This instance
	 */
	@Override
	public BukkitUser reloadPlayer() {
		reloadPermissions();
		Group groupFormat = getGroupFormat();
		String prefix = getFinalPrefix(groupFormat);
		String suffix = getFinalSuffix(groupFormat);
		
		reloadAboveHeadName(prefix, suffix, "update");
		reloadName(prefix, suffix);
		return this;
	}

	/**
	 * Reloads the player's permissions
	 * 
	 * @return       This instance
	 */
	@Override
	public BukkitUser reloadPermissions() {
		this.p.removeAttachment(permissionsAttachement);
		permissionsAttachement = p.addAttachment(CompletePermissions.getInstance());
		
		List<String> playerPermissions = new ArrayList<String>();
		for(String permission : getPermissions())
			playerPermissions.add(permission);
		
		for(Group group : getGroups().values()) {
			List<String> permsToAdd = getPermissionsOfParents(new ArrayList<String>(), group);
			playerPermissions.addAll(permsToAdd);
		}

		for(String permission : playerPermissions) {
			if(permission.startsWith("- ")) {
				permission = permission.substring(2);
				getPermissionsAttachement().setPermission(permission, false);
			} else {
				getPermissionsAttachement().setPermission(permission, true);
			}
		}
		return this;
	}
	
	/**
	 * Reloads the player's display name
	 * 
	 * @return       This instance
	 */
	@Override
	public BukkitUser reloadName(String prefix, String suffix) {
		this.getPlayer().setDisplayName(prefix + this.getPlayer().getName() + suffix);
		this.getPlayer().setPlayerListName(prefix + this.getPlayer().getName() + suffix);
		return this;
	}

	/**
	 * Reloads the player's above-head name
	 * 
	 * @return       This instance
	 */
	@Override
	public BukkitUser reloadAboveHeadName(String prefix, String suffix, String action) {
		Scoreboard sb;
		if(p.getScoreboard() == null)
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
		else
			sb = p.getScoreboard();
			
		if(sb.getTeam(p.getName()) == null) 
			sb.registerNewTeam(p.getName());
		Team team = sb.getTeam(p.getName());
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setNameTagVisibility(NameTagVisibility.ALWAYS);
		
		switch (action) {
			case "create":
				team.addEntry(p.getName());
				break;
			case "update":
				team.unregister();
				sb.registerNewTeam(p.getName());
				team = sb.getTeam(p.getName());
				team.setPrefix(prefix);
				team.setSuffix(suffix);
				team.setNameTagVisibility(NameTagVisibility.ALWAYS);
				team.addEntry(p.getName());
				break;
			case "destroy":
				team.unregister();
				break;
		}
		return this;
	}
	
	@Override
	public String toString() {
		List<String> groups = new ArrayList<String>();
		List<String> permissions = new ArrayList<String>();
		for (Group group : getGroups().values()) {
			groups.add(group.getName());
		}
		for (String s : getPermissions()) {
			permissions.add(s);
		}
		return ChatColor.YELLOW + "=== " + this.p.getName() + "'s Info ===" + ChatColor.GRAY + "\nGroups: " + groups
				+ ChatColor.DARK_GRAY + "\nPermissions: " + permissions + "\nPrefix: " + getPrefix() + "\nSuffix: " + getSuffix();
	}
	
	public List<String> getPermissionsOfParents(List<String> permissions, Group group) {
		for(String s : group.getPermissions()) {
			permissions.add(s);
		}
		
		if(group.getParents().size() == 0) return permissions;
		
		for(Group parentGroup : group.getParents().values()) {
			List<String> permissionsToAdd = getPermissionsOfParents(new ArrayList<String>(), parentGroup);
			permissions.addAll(permissionsToAdd);
		}
		
		return permissions;
	}
}
