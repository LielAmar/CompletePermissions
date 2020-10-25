package com.lielamar.utils.core.interfaces.managers;

import java.util.Map;

import com.lielamar.utils.core.interfaces.modules.Group;

public interface GroupManager {
	
	Group getGroup(String group);
	Map<String, Group> getGroups();
	
	Group getDefaultGroup();
	void setDefaultGroup(Group defaultGroup);
	void addGroup(Group group);
	void removeGroup(String groupName);
	void setup();
}
