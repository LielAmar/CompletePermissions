package bukkit.lielamar.completepermissions;

import java.util.Map;

import org.bukkit.entity.Player;

import net.lielamar.core.interfaces.moduls.Group;
import net.lielamar.core.interfaces.moduls.User;

public class CompletePermissionsAPI {

	private static CompletePermissionsAPI instance = new CompletePermissionsAPI();
	public static CompletePermissionsAPI getInstance() { return instance; }
	private CompletePermissionsAPI() {}
	
	public void setGroupToUser(Player player, String groupName) {
		Group group = getGroup(groupName);
		if(group == null) return;
		
		CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter()
			.setGroupToPlayer(player, group);
	}
	
	public void addGroupToUser(Player player, String groupName) {
		Group group = getGroup(groupName);
		if(group == null) return;
		
		CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter()
			.addGroupToPlayer(player, group);
	}
	
	public void setUserPrefix(Player player, String prefix) {
		CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setPrefixForPlayer(player, prefix);
		getUser(player).reloadName(prefix, getUserSuffix(player));
		getUser(player).reloadAboveHeadName(prefix, getUserSuffix(player), "update");
	}
	
	
	public void setUserSuffix(Player player, String suffix) {
		CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setSuffixForPlayer(player, suffix);
		getUser(player).reloadName(getUserPrefix(player), suffix);
		getUser(player).reloadAboveHeadName(getUserPrefix(player), suffix, "update");
	}
	
	
	public String getUserPrefix(Player player) {
		return getUser(player).getFinalPrefix(getUser(player).getGroupFormat());
	}
	
	public String getUserSuffix(Player player) {
		return getUser(player).getFinalSuffix(getUser(player).getGroupFormat());
	}
	
	public String getUserDisplayName(Player player) {
		return getUserPrefix(player) + player.getName() + getUserSuffix(player);
	}
	
	public Group getGroup(String group) {
		return CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().getGroups().get(group);
	}
	
	public User getUser(Player p) {
		return CompletePermissions.getInstance().getUserManager().getUsers().get(p.getUniqueId());
	}
	
	public Group getUserGroup(Player p) {
		return getUser(p).getGroupFormat();
	}
	
	public Map<String, Group> getUserGroups(Player p) {
		return getUser(p).getGroups();
	}
}
