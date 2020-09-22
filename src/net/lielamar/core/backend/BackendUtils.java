package net.lielamar.core.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lielamar.core.interfaces.moduls.Group;

public class BackendUtils {

	public static List<String> toStringList(Map<String, Group> collection) {
		List<String> list = new ArrayList<String>();
		for(Group group : collection.values())
			list.add(group.getName());
		return list;
	}

	public static Map<String, Group> toGroupList(List<String> collection, Map<String, Group> groups) {
		Map<String, Group> userGroups = new HashMap<String, Group>();
		
		for(Group g : groups.values())
			if(collection.contains(g.getName()))
				userGroups.put(g.getName(), g);
		
		return userGroups;
	}
	
}
