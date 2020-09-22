
package bukkit.lielamar.completepermissions.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import bukkit.lielamar.completepermissions.CompletePermissions;
import net.lielamar.bukkit.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;

public class BukkitPlayerConnectionsEvent implements Listener {

	@EventHandler (priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		CompletePermissions.getInstance().getUserManager().injectPlayer(e.getPlayer());
		e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', 
				((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("config.yml").get("JoinMessage"))
				.replaceAll("@nick", e.getPlayer().getName()).replaceAll("@ign", UUIDFetcher.getName(e.getPlayer().getUniqueId()))));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', 
				((String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("config.yml").get("QuitMessage"))
				.replaceAll("@nick", e.getPlayer().getName()).replaceAll("@ign", UUIDFetcher.getName(e.getPlayer().getUniqueId()))));
		CompletePermissions.getInstance().getUserManager().ejectPlayer(e.getPlayer());
	}
}
