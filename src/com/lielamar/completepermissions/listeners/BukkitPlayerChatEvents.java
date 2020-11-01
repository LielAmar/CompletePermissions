package com.lielamar.completepermissions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.utils.Utils;
import com.lielamar.utils.core.interfaces.modules.User;

public class BukkitPlayerChatEvents implements Listener {

	@EventHandler (priority=EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		String format = Utils.getInstance().getFormat();
		if(format == null) return;
		
		User u = CompletePermissions.getInstance().getUserManager().getUsers().get(p.getUniqueId());
		
		format = ChatColor.translateAlternateColorCodes('&', format);
		if(u.getPrefix() != null)
			format = format.replaceAll("%prefix%", u.getPrefix());
		if(u.getSuffix() != null)
			format = format.replaceAll("%suffix%", u.getSuffix());
		format = format.replaceAll("%name%", "%s");
		format = format.replaceAll("%message%", "%s");
		
		if(p.hasPermission("cp.chatcolor"))
			format = format.replace("@permissionscoloredmessage", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		else
			format = format.replace("@permissionscoloredmessage", e.getMessage());
		
		e.setFormat(format);
	}
}
