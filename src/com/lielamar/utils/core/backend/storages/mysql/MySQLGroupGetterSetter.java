package com.lielamar.utils.core.backend.storages.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.modules.BukkitGroup;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.core.backend.storages.StorageGroupGetterSetter;
import com.lielamar.utils.core.interfaces.modules.Group;

public class MySQLGroupGetterSetter implements StorageGroupGetterSetter {

	private Connection connection;
	public MySQLGroupGetterSetter(Connection connection) { this.connection = connection; }
	public Connection getConnection() { return this.connection; }
	
	@Override
	public String saveGroup(Group group) {
		if (!groupExists(group.getName()))
			return createGroup(group.getName());
		
		String statement = "UPDATE `" + MySQLManager.groupsTable + "` SET `prefix`=?,`suffix`=?,`isdefault`=?,`priority`=? WHERE name=?;";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getPrefix());
			ps.setString(2, group.getSuffix());
			String isDefault = "false";
			if(group.isDefault())
				isDefault = "true";
			ps.setString(3, isDefault);
			ps.setString(4, group.getPriority() + "");
			ps.setString(5, group.getName());
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.getSavedGroupMessage();
	}
	
	@Override
	public String createGroup(String groupName) {
		if (groupExists(groupName)) return Messages.getGroupExistsMessage();
		
		BukkitGroup group = new BukkitGroup(groupName, new ArrayList<String>(), new ArrayList<String>(), "", "", false, 'z');
		String statement = "INSERT INTO `" + MySQLManager.groupsTable + "` (`name`,`prefix`,`suffix`,`isdefault`,`priority`) VALUES (?,?,?,?,?);";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ps.setString(2, group.getPrefix());
			ps.setString(3, group.getSuffix());
			ps.setString(4, group.isDefault() ? "true" : "false");
			ps.setString(5, group.getPriority() + "");
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		CompletePermissions.getInstance().getGroupManager().addGroup(group);
		return Messages.getCreatedGroupMessage();
	}
	
	@Override
	public String deleteGroup(String groupName) {
		if(!groupExists(groupName)) return Messages.groupNotFoundMessage();
		
		String statement = "DELETE FROM " + MySQLManager.groupsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, groupName);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }

		statement = "DELETE FROM " + MySQLManager.groupsPermissionsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, groupName);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }

		statement = "DELETE FROM " + MySQLManager.groupsParentsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, groupName);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
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

		List<String> parents = null;
		List<String> permissions = null;
		
		String statement = "SELECT * FROM " + MySQLManager.groupsTable;
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				parents = new ArrayList<String>();
				permissions = new ArrayList<String>();
				
				String statement_permissions = "SELECT * FROM " + MySQLManager.groupsPermissionsTable + " WHERE name=?";
				try {
					PreparedStatement ps_permissions = getConnection().prepareStatement(statement_permissions);
					ps_permissions.setString(1, rs.getString("name"));
					ResultSet rs_permissions = ps_permissions.executeQuery();
					while (rs_permissions.next())
						permissions.add(rs_permissions.getString("permission"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				String statement_parents = "SELECT * FROM " + MySQLManager.groupsParentsTable + " WHERE name=?";
				try {
					PreparedStatement ps_parents = getConnection().prepareStatement(statement_parents);
					ps_parents.setString(1, rs.getString("name"));
					ResultSet rs_parents = ps_parents.executeQuery();
					while (rs_parents.next())
						parents.add(rs_parents.getString("parent"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				groups.put(rs.getString("name"), new BukkitGroup(rs.getString("name"), parents, permissions, rs.getString("prefix"), rs.getString("suffix"), Boolean.parseBoolean(rs.getString("isdefault")), rs.getString("priority").charAt(0)));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		return groups;
	}
	
	@Override
	public List<String> getGroupPermissions(Group group) {
		List<String> permissions = new ArrayList<String>();
		
		String statement = "SELECT * FROM " + MySQLManager.groupsPermissionsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				permissions.add(rs.getString("permission"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return permissions;
	}
	
	@Override
	public HashMap<String, Group> getGroupParents(Group group) {
		List<String> parentsRaw = new ArrayList<String>();
		
		String statement = "SELECT * FROM " + MySQLManager.groupsParentsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				parentsRaw.add(rs.getString("parent"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		HashMap<String, Group> parents = new HashMap<String, Group>();
		for(Group g : CompletePermissions.getInstance().getGroupManager().getGroups().values())
			if(parentsRaw.contains(g.getName()))
				parents.put(g.getName(), g);
		return parents;
	}
	
	@Override
	public String addPermissionToGroup(Group group, String permission) {
		List<String> permissions = getGroupPermissions(group);
		permissions.add(permission);
		
		String statement = "INSERT INTO " + MySQLManager.groupsPermissionsTable + " (name,permission) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ps.setString(2, permission);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		group.setPermissions(permissions);
		return Messages.addedPermissionToGroupMessage();
	}
	
	@Override
	public String removePermissionFromGroup(Group group, String permission) {
		List<String> permissions = getGroupPermissions(group);
		permissions.remove(permission);
		
		String statement = "DELETE FROM " + MySQLManager.groupsPermissionsTable + " WHERE name=? AND permission=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ps.setString(2, permission);
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		group.setPermissions(permissions);
		return Messages.removedPermissionToGroupMessage();
	}
	
	@Override
	public String addParentToGroup(Group group, Group parent) {
		HashMap<String, Group> parents = getGroupParents(group);
		parents.put(parent.getName(), parent);
		
		String statement = "INSERT INTO " + MySQLManager.groupsParentsTable + " (name,parent) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ps.setString(2, parent.getName());
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		group.setParents(parents);
		return Messages.addedParentToGroupMessage();
	}
	
	@Override
	public String removeParentFromGroup(Group group, Group parent) {
		HashMap<String, Group> parents = getGroupParents(group);
		if(parents.containsKey(parent.getName()))
			parents.remove(parent.getName());
		
		String statement = "DELETE FROM " + MySQLManager.groupsParentsTable + " WHERE name=? AND permission=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group.getName());
			ps.setString(2, parent.getName());
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		group.setParents(parents);
		return Messages.removedPermissionToGroupMessage();
	}
	
	@Override
	public String setPrefixForGroup(Group group, String prefix) {
		String statement = "UPDATE " + MySQLManager.groupsTable + " SET prefix=? WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, prefix);
			ps.setString(2, group.getName());
			ps.executeUpdate();
			group.setPrefix(prefix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPrefixForGroupMessage();
	}
	@Override
	public String setSuffixForGroup(Group group, String suffix) {
		String statement = "UPDATE " + MySQLManager.groupsTable + " SET suffix=? WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, suffix);
			ps.setString(2, group.getName());
			ps.executeUpdate();
			group.setSuffix(suffix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setSuffixForGroupMessage();
	}
	
	@Override
	public String setPriorityForGroup(Group group, char priority) {
		String statement = "UPDATE " + MySQLManager.groupsTable + " SET priority=? WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, priority + "");
			ps.setString(2, group.getName());
			ps.executeUpdate();
			group.setPriority(priority);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPriorityForGroupMessage();
	}
	
	@Override
	public boolean groupExists(String groupName) {
		String statement = "SELECT * FROM " + MySQLManager.groupsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, groupName);

			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) { e.printStackTrace(); }
		
		return false;
	}
}
