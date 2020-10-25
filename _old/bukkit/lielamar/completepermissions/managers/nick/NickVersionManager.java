package bukkit.lielamar.completepermissions.managers.nick;

import org.bukkit.entity.Player;

import net.lielamar.core.interfaces.moduls.Group;

public interface NickVersionManager {

	public void nickPlayer(Player p, String nick, Group g);
	
	public void skinPlayer(Player p, String skin);
	
	public void unnickPlayer(Player p);
	
}
