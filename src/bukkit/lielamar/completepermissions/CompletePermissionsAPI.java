package bukkit.lielamar.completepermissions;

import java.util.Map;

import org.bukkit.entity.Player;

import net.lielamar.core.interfaces.moduls.Group;
import net.lielamar.core.interfaces.moduls.User;

public class CompletePermissionsAPI {

	public static User getUser(Player p) {
		return CompletePermissions.getInstance().getUserManager().getUsers().get(p.getUniqueId());
	}
	
	public static String getUserPrefix(Player p) {
		return getUser(p).getFinalPrefix(getUser(p).getGroupFormat());
	}
	
	public static String getUserSuffix(Player p) {
		return getUser(p).getFinalSuffix(getUser(p).getGroupFormat());
	}
	
	public static Group getUserGroup(Player p) {
		return getUser(p).getGroupFormat();
	}
	
	public static Map<String, Group> getUserGroups(Player p) {
		return getUser(p).getGroups();
	}
}
