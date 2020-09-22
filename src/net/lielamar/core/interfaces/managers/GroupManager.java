package net.lielamar.core.interfaces.managers;

import java.util.Map;

import net.lielamar.core.interfaces.moduls.Group;

public interface GroupManager {
	
	Group getGroup(String group);
	Map<String, Group> getGroups();
	
	Group getDefaultGroup();
	void setDefaultGroup(Group defaultGroup);
	void addGroup(Group group);
	void removeGroup(String groupName);
	void setup();
}
