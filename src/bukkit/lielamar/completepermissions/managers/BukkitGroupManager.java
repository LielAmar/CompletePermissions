package bukkit.lielamar.completepermissions.managers;

import java.util.HashMap;
import java.util.Map;

import bukkit.lielamar.completepermissions.CompletePermissions;
import net.lielamar.core.interfaces.managers.GroupManager;
import net.lielamar.core.interfaces.moduls.Group;

public class BukkitGroupManager implements GroupManager {

	private static BukkitGroupManager instance = new BukkitGroupManager();
	private Map<String, Group> groups;
	private Group defaultGroup;
	
	private BukkitGroupManager() {}
	
	public static BukkitGroupManager getInstance() {
		return instance;
	}
	
	/**
	 * Sets up the Group Manager
	 */
	public void setup() {
		groups = CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().getGroups();
		if(groups == null)
			groups = new HashMap<String, Group>();
		
		for(Group group : groups.values()) {
			Map<String, Group> parents = new HashMap<String, Group>();
			for(Group parent : groups.values()) {
				if(group.getParentsRaw() == null) continue;
				if(group.getParentsRaw().contains(parent.getName().toLowerCase()))
					parents.put(parent.getName(), parent);
			}
			group.setParents(parents);
		}
		
		groups = CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().createDefaultGroup(groups);
	}

	@Override
	public Group getGroup(String group) {
		return (groups.containsKey(group.toLowerCase()) ? groups.get(group.toLowerCase()) : null);
	}

	@Override
	public Map<String, Group> getGroups() {
		return this.groups;
	}

	public Group getDefaultGroup() {
		if(defaultGroup == null)
			this.groups = CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().createDefaultGroup(this.groups);
		return defaultGroup;
	}

	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
	}
	
	public void addGroup(Group group) {
		groups.put(group.getName().toLowerCase(), group);
	}

	public void removeGroup(String parentName) {
		if(groups.containsKey(parentName.toLowerCase())) {
			Group parent = groups.get(parentName.toLowerCase());
			
			for(Group group : groups.values()) {
				if(group.getParents().containsKey(parentName.toLowerCase()))
					CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().removeParentFromGroup(group, parent);
			}
			
			groups.remove(parentName.toLowerCase());
			parent.reloadRelatedPlayers();
		}
	}
}
