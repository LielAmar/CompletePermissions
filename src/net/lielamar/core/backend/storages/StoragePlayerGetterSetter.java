package net.lielamar.core.backend.storages;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.lielamar.core.interfaces.moduls.Group;
import net.lielamar.core.interfaces.moduls.User;

public interface StoragePlayerGetterSetter {

	String saveUser(User user);
	User getUser(String playerName);
	User getUser(Player player);
	User getUser(UUID playerUUID);
	
	List<String> getPlayerPermissions(Player p);
	List<String> getPlayerPermissions(String name);
	Map<String, Group> getPlayerGroups(Player p);
	Map<String, Group> getPlayerGroups(String name);
	
	String addPermissionToPlayer(Player p, String permission);
	String addPermissionToPlayer(String name, String permission);
	String removePermissionFromPlayer(Player p, String permission);
	String removePermissionFromPlayer(String name, String permission);
	String addGroupToPlayer(Player p, Group group);
	String addGroupToPlayer(String name, Group group);
	String removeGroupFromPlayer(Player p, Group group);
	String removeGroupFromPlayer(String name, Group group);
	String setGroupToPlayer(Player p, Group group);
	String setGroupToPlayer(String name, Group group);
	String setPrefixForPlayer(Player p, String prefix);
	String setPrefixForPlayer(String name, String prefix);
	String setSuffixForPlayer(Player p, String suffix);
	String setSuffixForPlayer(String name, String suffix);
	
	User createUser(UUID uuid);
	boolean userExists(String player);
}
