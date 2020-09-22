package net.lielamar.core.backend.storages.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.modules.BukkitUser;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.bukkit.utils.BukkitFileManager.Config;
import net.lielamar.core.backend.BackendUtils;
import net.lielamar.core.backend.storages.StoragePlayerGetterSetter;
import net.lielamar.core.interfaces.moduls.Group;
import net.lielamar.core.interfaces.moduls.User;

public class FilesPlayerGetterSetter implements StoragePlayerGetterSetter {
	
	private Config config;
	public FilesPlayerGetterSetter(Config config) { this.config = config; }
	public Config getConfig() { return this.config; }

	@Override
	public String saveUser(User user) {
		config.set("players." + user.getPlayer().getName() + ".groups", BackendUtils.toStringList(user.getGroups()));
		config.set("players." + user.getPlayer().getName() + ".permissions", user.getPermissions());
		config.set("players." + user.getPlayer().getName() + ".prefix", user.getPrefix());
		config.set("players." + user.getPlayer().getName() + ".suffix", user.getSuffix());
		config.save();
		return Messages.getSavedUserMessage();
	}

	
	@Override
	public User getUser(String playerName) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(playerName.toLowerCase())) return null;
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(playerName.toLowerCase()));

		return getUser(u);
	}

	@Override
	public User getUser(Player player) {
		UUID u = player.getUniqueId();
		
		return getUser(u);
	}
	
	@Override
	public User getUser(UUID playerUUID) {
		UUID u = playerUUID;
		
		Map<String, Group> groups = new HashMap<String, Group>();
		List<String> permissions = new ArrayList<String>();
		String prefix = "";
		String suffix = "";
		
		if(config.contains("players." + u.toString())) {
			suffix = (String) config.get("players." + u.toString() + ".suffix");
			prefix = (String) config.get("players." + u.toString() + ".prefix");
			permissions = config.getStringList("players." + u.toString() + ".permissions");
			groups = BackendUtils.toGroupList(config.getStringList("players." + u.toString() + ".groups"), CompletePermissions.getInstance().getGroupManager().getGroups());
			if(groups.size() == 0)
				groups.put(CompletePermissions.getInstance().getGroupManager().getDefaultGroup().getName(), CompletePermissions.getInstance().getGroupManager().getDefaultGroup());
			config.set("players." + u.toString() + ".groups", BackendUtils.toStringList(groups));
			config.save();
		} else {
			config.set("players." + u.toString() + ".prefix", prefix);
			config.set("players." + u.toString() + ".suffix", suffix);
			config.set("players." + u.toString() + ".permissions", permissions);
			groups.put(CompletePermissions.getInstance().getGroupManager().getDefaultGroup().getName(), CompletePermissions.getInstance().getGroupManager().getDefaultGroup());
			config.set("players." + u.toString() + ".groups", BackendUtils.toStringList(groups));
			config.save();
		}
		
		return new BukkitUser(Bukkit.getPlayer(u), groups, permissions, prefix, suffix).reloadPlayer();
	}
	
	@Override
	public List<String> getPlayerPermissions(Player p) {
		return config.getStringList("players." + p.getUniqueId().toString() + ".permissions");
	}

	@Override
	public List<String> getPlayerPermissions(String name) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name)) return null;
		
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return null;
		UUID u = UUID.fromString((String)uuid);
		
		return config.getStringList("players." + u.toString() + ".permissions");
	}
	
	@Override
	public Map<String, Group> getPlayerGroups(Player p) {
		return BackendUtils.toGroupList(config.getStringList("players." + p.getUniqueId().toString() + ".groups"), CompletePermissions.getInstance().getGroupManager().getGroups());
	}

	@Override
	public Map<String, Group> getPlayerGroups(String name) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name)) return null;
		
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return null;
		UUID u = UUID.fromString((String)uuid);
		
		return BackendUtils.toGroupList(config.getStringList("players." + u.toString() + ".groups"), CompletePermissions.getInstance().getGroupManager().getGroups());
	}
	
	@Override
	public String addPermissionToPlayer(Player p, String permission) {
		List<String> permissions = getPlayerPermissions(p);
		permissions.add(permission);
		config.set("players." + p.getUniqueId().toString() + ".permissions", permissions);
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
		return Messages.addedPermissionToPlayerMessage();
	}

	@Override
	public String addPermissionToPlayer(String name, String permission) {
		if(!CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").contains(name)) return Messages.playerIsNotOnlineMessage();
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return ChatColor.RED + "something went wrong";
		UUID u = UUID.fromString((String)uuid);

		List<String> permissions = getPlayerPermissions(name);
		permissions.add(permission);
		config.set("players." + u.toString() + ".permissions", permissions);
		config.save();

		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setPermissions(permissions);
		
		return Messages.addedPermissionToPlayerMessage();
	}
	
	@Override
	public String addGroupToPlayer(Player p, Group group) {
		Map<String, Group> groups = getPlayerGroups(p);
		groups.put(group.getName(), group);
		config.set("players." + p.getUniqueId().toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		return Messages.addedGroupToPlayerMessage();
	}
	
	@Override
	public String addGroupToPlayer(String name, Group group) {
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return ChatColor.RED + "something went wrong";
		UUID u = UUID.fromString((String)uuid);

		Map<String, Group> groups = getPlayerGroups(name);
		groups.put(group.getName(), group);
		config.set("players." + u.toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();

		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setGroups(groups);
		
		return Messages.addedGroupToPlayerMessage();
	}

	@Override
	public String removePermissionFromPlayer(Player p, String permission) {
		List<String> permissions = getPlayerPermissions(p);
		permissions.remove(permission);
		config.set("players." + p.getUniqueId().toString() + ".permissions", permissions);
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setPermissions(permissions);
		
		return Messages.removedPermissionToPlayerMessage();
	}
	
	@Override
	public String removePermissionFromPlayer(String name, String permission) {
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return ChatColor.RED + "something went wrong";
		UUID u = UUID.fromString((String)uuid);

		List<String> permissions = getPlayerPermissions(name);
		permissions.remove(permission);
		config.set("players." + u.toString() + ".permissions", permissions);
		config.save();

		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setPermissions(permissions);
		
		return Messages.removedPermissionToPlayerMessage();
	}

	@Override
	public String removeGroupFromPlayer(Player p, Group group) {
		Map<String, Group> groups = getPlayerGroups(p);
		if(groups.containsKey(group.getName()))
			groups.remove(group.getName());
		config.set("players." + p.getUniqueId().toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		return Messages.removedGroupToPlayerMessage();
	}
	
	@Override
	public String removeGroupFromPlayer(String name, Group group) {
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return ChatColor.RED + "something went wrong";
		UUID u = UUID.fromString((String)uuid);
		
		Map<String, Group> groups = getPlayerGroups(name);
		if(groups.containsKey(group.getName()))
			groups.remove(group.getName());
		config.set("players." + u.toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();

		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setGroups(groups);
		
		return Messages.removedGroupToPlayerMessage();
	}

	@Override
	public String setGroupToPlayer(Player p, Group group) {
		Map<String, Group> groups = getPlayerGroups(p);
		groups.clear();
		groups.put(group.getName(), group);
		config.set("players." + p.getUniqueId().toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();

		CompletePermissions.getInstance().getUserManager().getPlayer(p).setGroups(groups);
		return Messages.setGroupToPlayerMessage();
	}

	@Override
	public String setGroupToPlayer(String name, Group group) {
		Object uuid = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name);
		if(uuid == null) return ChatColor.RED + "something went wrong";
		UUID u = UUID.fromString((String)uuid);

		Map<String, Group> groups = getPlayerGroups(name);
		groups.clear();
		groups.put(group.getName(), group);
		config.set("players." + u.toString() + ".groups", BackendUtils.toStringList(groups));
		config.save();

		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setGroups(groups);
		
		return Messages.setGroupToPlayerMessage();
	}
	
	@Override
	public String setPrefixForPlayer(Player p, String prefix) {
		config.set("players." + p.getUniqueId().toString() + ".prefix", prefix);
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setPrefix(prefix);
		return Messages.setPrefixForPlayerMessage();
	}
	
	@Override
	public String setPrefixForPlayer(String name, String prefix) {
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name));

		config.set("players." + u.toString() + ".prefix", prefix);
		config.save();
		
		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setPrefix(prefix);

		return Messages.setPrefixForPlayerMessage();
	}

	@Override
	public String setSuffixForPlayer(Player p, String suffix) {
		config.set("players." + p.getUniqueId().toString() + ".suffix", suffix);
		config.save();
		
		CompletePermissions.getInstance().getUserManager().getPlayer(p).setSuffix(suffix);
		return Messages.setSuffixForPlayerMessage();
	}

	@Override
	public String setSuffixForPlayer(String name, String suffix) {
		UUID u = UUID.fromString((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("nameuuidfetcher").get(name));

		config.set("players." + u.toString() + ".suffix", suffix);
		config.save();
		
		Player player = Bukkit.getPlayer(name);
		if(player != null) CompletePermissions.getInstance().getUserManager().getPlayer(player).setSuffix(suffix);

		return Messages.setPrefixForPlayerMessage();
	}
	@Override
	public User createUser(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean userExists(String player) {
		// TODO Auto-generated method stub
		return false;
	}
}
