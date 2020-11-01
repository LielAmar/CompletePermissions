package com.lielamar.completepermissions.managers.nick;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_10_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_11_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_12_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_13_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_13_R2;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_14_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_15_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_16_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_16_R2;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_8_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_8_R2;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_8_R3;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_9_R1;
import com.lielamar.completepermissions.managers.nick.versions.NMS_v1_9_R2;
import com.lielamar.completepermissions.modules.BukkitNickedPlayer;
import com.lielamar.utils.bukkit.utils.BukkitFileManager.Config;
import com.lielamar.utils.core.interfaces.modules.Group;

public class NickManager {

	public static Map<UUID, BukkitNickedPlayer> nicks;
	
	public static NickVersionManager setup() {
		nicks = new LinkedHashMap<UUID, BukkitNickedPlayer>();
		return getNickVersionManager();
	}
	
	public static void reloadNicks() {
		Config nameCache = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml");
		NickVersionManager nm = CompletePermissions.getInstance().getNickManager();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(nameCache.contains(p.getUniqueId().toString())) {
				if(nm != null) {
					String groupName = ((String)nameCache.get(p.getUniqueId().toString())).split(";")[0];
					String nick = ((String)nameCache.get(p.getUniqueId().toString())).split(";")[1];
					Group group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
					nm.nickPlayer(p, nick, group);
				}
			} else {
				CompletePermissions.getInstance().getNickManager().unnickPlayer(p);
			}
		}
	}
	
	public static void export() {
		for(UUID u : nicks.keySet()) {
			BukkitNickedPlayer np = nicks.get(u);
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").set(u.toString(), np.getGroup().getName().toString() + ";" + np.getNick().toString());
		}
		CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").save();
	}
	
	public static void handleRenick(Player p) {
		Config nameCache = CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml");
		NickVersionManager nm = CompletePermissions.getInstance().getNickManager();
		if(nameCache.contains(p.getUniqueId().toString())) {
			if(nm != null) {
				String groupName = ((String)nameCache.get(p.getUniqueId().toString())).split(";")[0];
				String nick = ((String)nameCache.get(p.getUniqueId().toString())).split(";")[1];
				Group group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
				nm.nickPlayer(p, nick, group);
			}
		} else {
			if(nm != null)
				CompletePermissions.getInstance().getNickManager().unnickPlayer(p);
		}
	}
	
	public static void handleSavenick(Player p) {
		if(nicks.containsKey(p.getUniqueId())) {
			BukkitNickedPlayer np = nicks.get(p.getUniqueId());
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").set(p.getUniqueId().toString(), np.getGroup().getName().toString() + ";" + np.getNick().toString());
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").save();
			nicks.remove(p.getUniqueId());
		}
	}
	
	public static void handleRemovenick(Player p) {
		if(nicks.containsKey(p.getUniqueId())) {
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").set(p.getUniqueId().toString(), null);
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("nickcache.yml").save();
			nicks.remove(p.getUniqueId());
		}
	}
	
	private static NickVersionManager getNickVersionManager() {
		String version = null;
		
		try { version = Bukkit.getServer().getClass().getPackage().getName().toString().split("\\.")[3]; }
		catch (Exception e) { e.printStackTrace(); }
		
		switch(version) {
			case "v1_8_R1":
				return new NMS_v1_8_R1();
			case "v1_8_R2":
				return new NMS_v1_8_R2();
			case "v1_8_R3":
				return new NMS_v1_8_R3();
			case "v1_9_R1":
				return new NMS_v1_9_R1();
			case "v1_9_R2":
				return new NMS_v1_9_R2();
			case "v1_10_R1":
				return new NMS_v1_10_R1();
			case "v1_11_R1":
				return new NMS_v1_11_R1();
			case "v1_12_R1":
				return new NMS_v1_12_R1();
			case "v1_13_R1":
				return new NMS_v1_13_R1();
			case "v1_13_R2":
				return new NMS_v1_13_R2();
			case "v1_14_R1":
				return new NMS_v1_14_R1();
			case "v1_15_R1":
				return new NMS_v1_15_R1();
			case "v1_16_R1":
				return new NMS_v1_16_R1();
			case "v1_16_R2":
				return new NMS_v1_16_R2();
			default:
				CompletePermissions.getConsoleSender().sendMessage(ChatColor.RED + "You are using a unsupported version. Please use a supported version to use this plugin!");
				CompletePermissions.getInstance().getPluginLoader().disablePlugin(CompletePermissions.getInstance());
				return null;
		}
	}
}
