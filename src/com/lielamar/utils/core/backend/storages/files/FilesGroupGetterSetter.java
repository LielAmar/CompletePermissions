package com.lielamar.utils.core.backend.storages.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.modules.BukkitGroup;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.bukkit.utils.BukkitFileManager.Config;
import com.lielamar.utils.core.backend.BackendUtils;
import com.lielamar.utils.core.backend.storages.StorageGroupGetterSetter;
import com.lielamar.utils.core.interfaces.modules.Group;

public class FilesGroupGetterSetter implements StorageGroupGetterSetter {

	private Config config;
	public FilesGroupGetterSetter(Config config) { this.config = config; }
	public Config getConfig() { return this.config; }
	
	@Override
	public String saveGroup(Group group) {
		config.set("groups." + group.getName() + ".default", group.isDefault());
		config.set("groups." + group.getName() + ".parents", BackendUtils.toStringList(group.getParents()));
		config.set("groups." + group.getName() + ".permissions", group.getPermissions());
		config.set("groups." + group.getName() + ".prefix", group.getPrefix());
		config.set("groups." + group.getName() + ".prefix", group.getSuffix());
		config.set("groups." + group.getName() + ".priority", group.getPriority());
		config.save();
		return Messages.getSavedGroupMessage();
	}
	
	@Override
	public String createGroup(String groupName) {
		if(groupExists(groupName)) return Messages.getGroupExistsMessage();
		
		BukkitGroup group = new BukkitGroup(groupName, new ArrayList<String>(), new ArrayList<String>(), "", "", false, 'z');
		config.set("groups." + groupName + ".default", false);
		config.set("groups." + groupName + ".parents", new ArrayList<String>());
		config.set("groups." + groupName + ".permissions", new ArrayList<String>());
		config.set("groups." + groupName + ".prefix", "");
		config.set("groups." + groupName + ".prefix", "");
		config.set("groups." + groupName + ".priority", 'z');
		config.save();
		
		CompletePermissions.getInstance().getGroupManager().addGroup(group);
		return Messages.getCreatedGroupMessage();
	}

	@Override
	public String deleteGroup(String groupName) {
		config.set("groups." + groupName, null);
		config.save();
		CompletePermissions.getInstance().getGroupManager().removeGroup(groupName);
		return Messages.getDeletedGroupMessage();
	}
	
	@Override
	public Map<String, Group> createDefaultGroup(Map<String, Group> groups) {
		int defaultCounter = 0;
		
		for(Group g : groups.values()) {
			if(g.isDefault()) {
				CompletePermissions.getInstance().getGroupManager().setDefaultGroup(g);
				defaultCounter++;
			}
		}

		if (defaultCounter == 0) {
			Group groupNamedDefault = null;
			for(Group g : groups.values()) {
				if(g.getName().equalsIgnoreCase("default")) {
					groupNamedDefault = g;
				}
			}

			if(groupNamedDefault != null) {
				groupNamedDefault.setDefault(true);
				saveGroup(groupNamedDefault);

				CompletePermissions.getInstance().getGroupManager().setDefaultGroup(groupNamedDefault);
			} else {
				BukkitGroup defaultGroup = new BukkitGroup("default", new HashMap<String, Group>(), new ArrayList<String>(), "&s7", "", true, 'z');
				groups.put(defaultGroup.getName(), defaultGroup);
				saveGroup(defaultGroup);

				CompletePermissions.getInstance().getGroupManager().setDefaultGroup(groupNamedDefault);
			}
		} else {
			if (defaultCounter > 1) {
				boolean saveOneGroup = true;

				for(Group group : groups.values()) {
					if (group.isDefault()) {
						if(saveOneGroup) {
							saveOneGroup = false;
							CompletePermissions.getInstance().getGroupManager().setDefaultGroup(group);
						} else {
							group.setDefault(false);
							saveGroup(group);
						}
					}
				}
			}
		}
		return groups;
	}
	
	
	@Override
	public Map<String, Group> getGroups() {

		Map<String, Group> groups = new HashMap<String, Group>();
		
		boolean isDefault = false;
		List<String> parents = null;
		List<String> permissions = null;
		String prefix = null;
		String suffix = null;
		char priority = 'z';
		
		for (String name : config.getConfigurationSection("groups").getKeys(false)) {
			isDefault = false;
			parents = new ArrayList<String>();
			permissions = new ArrayList<String>();
			prefix = "";
			suffix = "";
			
			if (config.contains("groups." + name + ".default"))
				isDefault = (boolean)config.get("groups." + name + ".default");
			else
				config.set("groups." + name + ".default", Boolean.valueOf(isDefault));
		
			if (config.contains("groups." + name + ".parents"))
				parents = (ArrayList<String>) config.getStringList("groups." + name + ".parents");
			else
				config.set("groups." + name + ".parents", parents);
			
			if (config.contains("groups." + name + ".permissions"))
				permissions = (ArrayList<String>) config.getStringList("groups." + name + ".permissions");
			else
				config.set("groups." + name + ".permissions", permissions);
			
			if (config.contains("groups." + name + ".prefix"))
				prefix = (String)config.get("groups." + name + ".prefix");
			else
				config.set("groups." + name + ".prefix", prefix);
			
			if (config.contains("groups." + name + ".suffix"))
				suffix = (String)config.get("groups." + name + ".suffix");
			else
				config.set("groups." + name + ".suffix", suffix);

			if (config.contains("groups." + name + ".priority"))
				priority = ((String)config.get("groups." + name + ".priority")).charAt(0);
			else
				config.set("groups." + name + ".priority", 'z');
			
			
			List<String> updatedParents = new ArrayList<String>();
			for(String s : parents) {
				updatedParents.add(s.toLowerCase());
			}
			
			this.config.save();
			groups.put(name, new BukkitGroup(name, updatedParents, permissions, prefix, suffix, isDefault, priority));
		}

		return groups;
	}

	@Override
	public List<String> getGroupPermissions(Group group) {
		return config.getStringList("groups." + group.getName() + ".permissions");
	}

	@Override
	public HashMap<String, Group> getGroupParents(Group group) {
		List<String> list = config.getStringList("groups." + group.getName() + ".parents");
		
		HashMap<String, Group> parents = new HashMap<String, Group>();
		for(Group g : CompletePermissions.getInstance().getGroupManager().getGroups().values())
			if(list.contains(g.getName()))
				parents.put(g.getName(), g);
		
		return parents;
	}
	
	@Override
	public String addPermissionToGroup(Group group, String permission) {
		List<String> permissions = getGroupPermissions(group);
		permissions.add(permission);
		config.set("groups." + group.getName() + ".permissions", permissions);
		config.save();
		group.setPermissions(permissions);
		return Messages.addedPermissionToGroupMessage();
	}

	@Override
	public String removePermissionFromGroup(Group group, String permission) {
		List<String> permissions = getGroupPermissions(group);
		permissions.remove(permission);
		config.set("groups." + group.getName() + ".permissions", permissions);
		config.save();
		group.setPermissions(permissions);
		return Messages.removedPermissionToGroupMessage();
		
	}
	
	@Override
	public String addParentToGroup(Group group, Group parent) {
		HashMap<String, Group> parents = getGroupParents(group);
		parents.put(parent.getName(), parent);
		config.set("groups." + group.getName() + ".parents", BackendUtils.toStringList(parents));
		config.save();
		group.setParents(parents);
		return Messages.addedParentToGroupMessage();
	}

	@Override
	public String removeParentFromGroup(Group group, Group parent) {
		HashMap<String, Group> parents = getGroupParents(group);
		if(parents.containsKey(parent.getName()))
			parents.remove(parent.getName());
		config.set("groups." + group.getName() + ".parents", BackendUtils.toStringList(parents));
		config.save();
		group.setParents(parents);
		return Messages.removedParentToGroupMessage();
	}

	@Override
	public String setPrefixForGroup(Group group, String prefix) {
		config.set("groups." + group.getName() + ".prefix", prefix);
		config.save();
		group.setPrefix(prefix);
		return Messages.setPrefixForGroupMessage();
	}

	@Override
	public String setSuffixForGroup(Group group, String suffix) {
		config.set("groups." + group.getName() + ".prefix", suffix);
		config.save();
		group.setSuffix(suffix);
		return Messages.setSuffixForGroupMessage();
	}
	
	@Override
	public String setPriorityForGroup(Group group, char priority) {
		config.set("groups." + group.getName() + ".priority", priority);
		config.save();
		group.setPriority(priority);
		return Messages.setPriorityForGroupMessage();
	}
	
	@Override
	public boolean groupExists(String groupName) {
		return config.contains("groups." + groupName); 
	}
}
