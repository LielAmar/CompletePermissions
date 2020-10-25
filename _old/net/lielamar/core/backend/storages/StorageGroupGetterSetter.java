package net.lielamar.core.backend.storages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lielamar.core.interfaces.moduls.Group;

public interface StorageGroupGetterSetter {

	String saveGroup(Group group);
	String createGroup(String groupName);
	String deleteGroup(String groupName);
	Map<String, Group> createDefaultGroup(Map<String, Group> groups);
	
	Map<String, Group> getGroups();
	List<String> getGroupPermissions(Group group);
	HashMap<String, Group> getGroupParents(Group group);
	
	String addPermissionToGroup(Group group, String permission);
	String removePermissionFromGroup(Group group, String permission);
	String addParentToGroup(Group group, Group parent);
	String removeParentFromGroup(Group group, Group parent);
	String setPrefixForGroup(Group group, String prefix);
	String setSuffixForGroup(Group group, String suffix);
	String setPriorityForGroup(Group group, char priority);
	
	boolean groupExists(String groupName);
}
