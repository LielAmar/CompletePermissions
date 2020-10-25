package com.lielamar.completepermissions.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.utils.core.interfaces.modules.Group;
import com.lielamar.utils.core.interfaces.modules.User;

public class BukkitGroup implements Group {

	private String name;
	private Map<String, Group> parents;
	private List<String> parentsRaw;
	private List<String> permissions;
	private String prefix, suffix;
	private boolean isDefault;
	private char prioritry;
	
	public BukkitGroup(String name, Map<String, Group> parents, List<String> permissions, String prefix, String suffix, boolean isDefault, char prioritry) {
		this.name = name;
		this.parents = parents;
		this.parentsRaw = null;
		this.permissions = permissions;
		this.prefix = prefix;
		this.suffix = suffix;
		this.isDefault = isDefault;
		this.prioritry = prioritry;
	}
	
	public BukkitGroup(String name, List<String> parents, List<String> permissions, String prefix, String suffix, boolean isDefault, char prioritry) {
		this.name = name;
		this.parentsRaw = parents;
		this.parents = new HashMap<String, Group>();
		this.permissions = permissions;
		this.prefix = prefix;
		this.suffix = suffix;
		this.isDefault = isDefault;
		this.prioritry = prioritry;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Map<String, Group> getParents() {
		return this.parents;
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
	public char getPriority() {
		return this.prioritry;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setParents(Map<String, Group> parents) {
		this.parents = parents;
		reloadRelatedPlayers();
	}

	@Override
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
		reloadRelatedPlayers();
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		reloadRelatedPlayers();
	}

	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		reloadRelatedPlayers();
	}

	@Override
	public boolean isDefault() {
		return isDefault;
	}

	@Override
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Override
	public List<String> getParentsRaw() {
		return this.parentsRaw;
	}

	@Override
	public void setParentsRaw(List<String> parentsRaw) {
		this.parentsRaw = parentsRaw;
	}
	
	@Override
	public void setPriority(char prioritry) {
		this.prioritry = prioritry;
	}
	
	/**
	 * Reloads all players related to this group
	 */
	@Override
	public void reloadRelatedPlayers() {
		for(Player pl : Bukkit.getOnlinePlayers()) {
			if(CompletePermissions.getInstance().getUserManager() == null) return;
			User user = CompletePermissions.getInstance().getUserManager().getPlayer(pl);
			if(user.isRelated(this)) {
				user.reloadPlayer();
			}
		}
	}
	
	@Override
	public String toString() {
		List<String> parents = new ArrayList<String>();
		List<String> permissions = new ArrayList<String>();
		for (Group group : getParents().values()) {
			parents.add(group.getName());
		}
		for (String s : getPermissions()) {
			permissions.add(s);
		}
		return ChatColor.AQUA + "=== " + this.getName() + "'s Info ===" + ChatColor.GRAY + "\nParents: " + parents
				+ ChatColor.DARK_GRAY + "\nPermissions: " + permissions + "\nPrefix: \""
				+ ChatColor.translateAlternateColorCodes('&', getPrefix()) + ChatColor.DARK_GRAY + "\"\nSuffix: \""
				+ ChatColor.translateAlternateColorCodes('&', getSuffix()) + ChatColor.DARK_GRAY + "\"";
	}
}
