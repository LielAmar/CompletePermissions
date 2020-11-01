package com.lielamar.completepermissions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.utils.bukkit.utils.UUIDFetcher;

import net.md_5.bungee.api.ChatColor;

public class BukkitPlayerConnectionsEvent implements Listener {
	
	@EventHandler (priority=EventPriority.LOWEST)
	public void onJoin(PlayerLoginEvent e) {
//		PermissionAttachment attachment = e.getPlayer().addAttachment(CompletePermissions.getPlugin(CompletePermissions.class));
//		BukkitUserManager.getInstance().attachments.put(e.getPlayer().getUniqueId().toString(), attachment);
		
//		CompletePermissions.getInstance().getUserManager().injectPlayer(e.getPlayer());
//		CompletePermissions.getInstance().getUserManager().getPlayer(e.getPlayer()).setPermissionsAttachement(attachment);
	}
	
	@EventHandler (priority=EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		CompletePermissions.getInstance().getUserManager().injectPlayer(e.getPlayer());
//		CompletePermissions.getInstance().getUserManager().getPlayer(e.getPlayer()).reloadPlayer();
		
		String joinMessage = ChatColor.translateAlternateColorCodes('&', 
				((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("config.yml").get("JoinMessage"))
				.replaceAll("@nick", e.getPlayer().getName()).replaceAll("@ign", UUIDFetcher.getName(e.getPlayer().getUniqueId())));
		
		if(joinMessage.equalsIgnoreCase("")) e.setJoinMessage(null);
		else e.setJoinMessage(joinMessage);
	}
	
	
	@EventHandler (priority=EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e) {
		String quitMessage = ChatColor.translateAlternateColorCodes('&', 
				((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("config.yml").get("QuitMessage"))
				.replaceAll("@nick", e.getPlayer().getName()).replaceAll("@ign", UUIDFetcher.getName(e.getPlayer().getUniqueId())));
		
		if(quitMessage.equalsIgnoreCase("")) e.setQuitMessage(null);
		else e.setQuitMessage(quitMessage);
		
		CompletePermissions.getInstance().getUserManager().ejectPlayer(e.getPlayer());
	}
}
