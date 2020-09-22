package net.lielamar.core.backend.storages.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.modules.BukkitUser;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.core.backend.storages.StoragePlayerGetterSetter;
import net.lielamar.core.interfaces.moduls.Group;
import net.lielamar.core.interfaces.moduls.User;

public class MySQLPlayerGetterSetter implements StoragePlayerGetterSetter {

	private Connection connection;
	public MySQLPlayerGetterSetter(Connection connection) { this.connection = connection; }
	public Connection getConnection() { return this.connection; }
	
	@Override
	public String saveUser(User user) {
		if (!userExists(user.getPlayer().getName())) {
			createUser(user.getPlayer().getUniqueId());
			return Messages.getSavedUserMessage();
		}
		
		String statement = "UPDATE " + MySQLManager.playersTable + " SET (name,prefix,suffix) VALUES (?,?) WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, user.getPlayer().getName());
			ps.setString(2, user.getPrefix());
			ps.setString(3, user.getSuffix());
			ps.setString(4, user.getPlayer().getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.getSavedGroupMessage();
	}
	
	@Override
	public User getUser(String playerName) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(playerName.toLowerCase())) return null;
		
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(playerName.toLowerCase()));
		if(!userExists(playerName)) {
			return createUser(u);
		}
		return getUser(u);
	}
	
	@Override
	public User getUser(Player player) {
		UUID u = player.getUniqueId();
		if(!userExists(player.getName())) {
			return createUser(u);
		}
		return getUser(u);
	}
	
	@Override
	public User getUser(UUID u) {
		Map<String, Group> groups = new HashMap<String, Group>();
		String statement = "SELECT * FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				groups.put(rs.getString("parent"), CompletePermissions.getInstance().getGroupManager().getGroup(rs.getString("parent")));
			}
		} catch (SQLException e) { e.printStackTrace(); }

		List<String> permissions = new ArrayList<String>();
		statement = "SELECT * FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				permissions.add(rs.getString("permission"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		statement = "SELECT * FROM " + "players" + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return new BukkitUser(Bukkit.getPlayer(u), groups, permissions, rs.getString("prefix"), rs.getString("suffix")).reloadPlayer();
			
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	@Override
	public List<String> getPlayerPermissions(Player p) {
		List<String> permissions = new ArrayList<String>();
		
		String statement = "SELECT * FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				permissions.add(rs.getString("permission"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return permissions;
	}
	
	@Override
	public List<String> getPlayerPermissions(String name) {
		List<String> permissions = new ArrayList<String>();
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		if (!userExists(name)) {
			if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
			createUser(u);
			return permissions;
		}
		
		String statement = "SELECT * FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				permissions.add(rs.getString("permission"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return permissions;
	}
	
	@Override
	public Map<String, Group> getPlayerGroups(Player p) {
		Map<String, Group> groups = new HashMap<String, Group>();
		
		String statement = "SELECT * FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				groups.put(rs.getString("parent"), CompletePermissions.getInstance().getGroupManager().getGroup(rs.getString("parent")));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return groups;
	}
	@Override
	public Map<String, Group> getPlayerGroups(String name) {
		Map<String, Group> groups = new HashMap<String, Group>();
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		if (!userExists(name)) {
			if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
			createUser(u);
			return groups;
		}
		
		
		String statement = "SELECT * FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				groups.put(rs.getString("parent"), CompletePermissions.getInstance().getGroupManager().getGroup(rs.getString("parent")));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return groups;
	}
	
	@Override
	public String addPermissionToPlayer(Player p, String permission) {
		String statement = "INSERT INTO " + MySQLManager.playersPermissionsTable + " (uuid,permission) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, permission);
			ps.executeUpdate();
			
			List<String> permissions = getPlayerPermissions(p);
			permissions.add(permission);
			if(p != null) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
			
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.addedPermissionToPlayerMessage();
	}
	@Override
	public String addPermissionToPlayer(String name, String permission) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "INSERT INTO " + MySQLManager.playersPermissionsTable + " (uuid,permission) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ps.setString(2, permission);
			ps.executeUpdate();
			
			List<String> permissions = getPlayerPermissions(name);
			permissions.add(permission);
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.addedPermissionToPlayerMessage();
	}
	
	@Override
	public String removePermissionFromPlayer(Player p, String permission) {
		String statement = "DELETE FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=? AND permission=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, permission);
			ps.executeUpdate();
			
			List<String> permissions = getPlayerPermissions(p);
			permissions.remove(permission);
			if(p != null) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
		} catch (SQLException e) { e.printStackTrace(); }
		return Messages.removedPermissionToPlayerMessage();
	}
	@Override
	public String removePermissionFromPlayer(String name, String permission) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "DELETE FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=? AND permission=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ps.setString(2, permission);
			ps.executeUpdate();
			
			List<String> permissions = getPlayerPermissions(name);
			permissions.remove(permission);
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
		} catch (SQLException e) { e.printStackTrace(); }
		return Messages.removedPermissionToPlayerMessage();
	}
	
	@Override
	public String addGroupToPlayer(Player p, Group group) {
		String statement = "INSERT INTO " + MySQLManager.playersGroupsTable + " (uuid,parent) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, group.getName());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(p);
			groups.put(group.getName(), group);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.addedGroupToPlayerMessage();
	}
	
	@Override
	public String addGroupToPlayer(String name, Group group) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "INSERT INTO " + MySQLManager.playersGroupsTable + " (uuid,parent) VALUES(?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ps.setString(2, group.getName());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(name);
			groups.put(group.getName(), group);
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.addedGroupToPlayerMessage();
	}
	
	@Override
	public String removeGroupFromPlayer(Player p, Group group) {
		String statement = "DELETE FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=? AND parent=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, group.getName());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(p);
			if(groups.containsKey(group.getName()))
				groups.remove(group.getName());
			
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		return Messages.removedGroupToPlayerMessage();
	}
	
	@Override
	public String removeGroupFromPlayer(String name, Group group) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "DELETE FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=? AND parent=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ps.setString(2, group.getName());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(name);
			if(groups.containsKey(group.getName()))
				groups.remove(group.getName());
			
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		return Messages.removedGroupToPlayerMessage();
	}
	
	@Override
	public String setGroupToPlayer(Player p, Group group) {
		
		String statement = "DELETE FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(p);
			groups.clear();
			
			addGroupToPlayer(p, group);
			groups.put(group.getName(), group);

			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setGroupToPlayerMessage();
	}
	
	@Override
	public String setGroupToPlayer(String name, Group group) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "DELETE FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());
			ps.executeUpdate();
			
			Map<String, Group> groups = getPlayerGroups(name);
			groups.clear();
			
			addGroupToPlayer(name, group);
			groups.put(group.getName(), group);
			
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setGroupToPlayerMessage();
	}
	
	@Override
	public String setPrefixForPlayer(Player p, String prefix) {
		String statement = "UPDATE " + MySQLManager.playersTable + " SET prefix=? WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, prefix);
			ps.setString(2, p.getUniqueId().toString());
			ps.executeUpdate();
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPrefix(prefix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPrefixForPlayerMessage();
	}
	
	@Override
	public String setPrefixForPlayer(String name, String prefix) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "UPDATE " + MySQLManager.playersTable + " SET prefix=? WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, prefix);
			ps.setString(2, u.toString());
			ps.executeUpdate();
			
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setPrefix(prefix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPrefixForPlayerMessage();
	}
	
	@Override
	public String setSuffixForPlayer(Player p, String suffix) {
		String statement = "UPDATE " + MySQLManager.playersTable + " SET suffix=? WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, suffix);
			ps.setString(2, p.getUniqueId().toString());
			ps.executeUpdate();
			
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setSuffix(suffix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPrefixForPlayerMessage();
	}
	
	@Override
	public String setSuffixForPlayer(String name, String suffix) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name.toLowerCase()));
		
		String statement = "UPDATE " + MySQLManager.playersTable + " SET suffix=? WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, suffix);
			ps.setString(2, u.toString());
			ps.executeUpdate();
			
			Player p = Bukkit.getPlayer(name);
			if(p != null && CompletePermissions.getInstance().getUserManager().getUsers().containsKey(p.getUniqueId())) CompletePermissions.getInstance().getUserManager().getPlayer(p).setSuffix(suffix);
		} catch (SQLException e) { e.printStackTrace(); }
		
		return Messages.setPrefixForPlayerMessage();
	}

	@Override
	public User createUser(UUID uuid) {
		String statement = "INSERT INTO " + MySQLManager.playersTable + " (uuid,name,prefix,suffix) VALUES(?,?,?,?)";
		try {
			Map<String, Group> groups = new HashMap<String, Group>();
			Group defaultGroup = CompletePermissions.getInstance().getGroupManager().getDefaultGroup();
			Player p = Bukkit.getPlayer(uuid);
			BukkitUser user = new BukkitUser(p, groups, new ArrayList<String>(), "", "").reloadPlayer();
			
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, uuid.toString());
			ps.setString(2, p.getName());
			ps.setString(3, "");
			ps.setString(4, "");
			ps.executeUpdate();
			addGroupToPlayer(p, defaultGroup);
			
			return user;
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	@Override
	public boolean userExists(String player) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(player.toLowerCase())) return false;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(player.toLowerCase()));

		String statement = "SELECT * FROM " + MySQLManager.playersTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) { e.printStackTrace(); }
		return false;
	}
	
}
