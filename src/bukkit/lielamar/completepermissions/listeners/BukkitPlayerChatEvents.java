package bukkit.lielamar.completepermissions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import bukkit.lielamar.completepermissions.utils.Utils;

public class BukkitPlayerChatEvents implements Listener {

	@EventHandler (priority=EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		String format = Utils.getInstance().getFormat();
		if(format == null) return;
		
		format = ChatColor.translateAlternateColorCodes('&', format);
		format = format.replaceAll("@name", p.getName());
		format = format.replaceAll("@displayname", p.getDisplayName());
		format = format.replace("@message", e.getMessage());
		format = format.replace("@coloredmessage", ChatColor.translateAlternateColorCodes('&', e.getMessage()));

		if(p.hasPermission("cp.chatcolor"))
			format = format.replace("@permissionscoloredmessage", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		else
			format = format.replace("@permissionscoloredmessage", e.getMessage());
		
		e.setFormat(format);
	}
}
