package com.lielamar.completepermissions.managers.nick.versions;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.managers.nick.NickManager;
import com.lielamar.completepermissions.managers.nick.NickVersionManager;
import com.lielamar.completepermissions.modules.BukkitNickedPlayer;
import com.lielamar.utils.bukkit.utils.GameProfileBuilder;
import com.lielamar.utils.bukkit.utils.UUIDFetcher;
import com.lielamar.utils.core.interfaces.modules.Group;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R1.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerInfo;

public class NMS_v1_8_R1 implements NickVersionManager {

	public static Field name;
	
	public NMS_v1_8_R1() {
		try {
			name = GameProfile.class.getDeclaredField("name");
			name.setAccessible(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void nickPlayer(Player p, String nick, Group g) {
		CraftPlayer cp = (CraftPlayer) p;
		
		try {
			name.set(cp.getProfile(), nick);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		skinPlayer(p, nick);
		
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPackets(destroy, p);
		PacketPlayOutPlayerInfo removeTab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacketsToEveryone(removeTab);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				PacketPlayOutPlayerInfo addTab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
				sendPacketsToEveryone(addTab);
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				sendPackets(spawn, p);
			}
		}.runTaskLater(CompletePermissions.getInstance(), 4L);
		
		NickManager.nicks.put(p.getUniqueId(), new BukkitNickedPlayer(p, nick, g));
		CompletePermissions.getInstance().getUserManager().getPlayer(p).reloadPlayer();
	}

	@Override
	public void skinPlayer(Player p, String skin) {
		CraftPlayer cp = (CraftPlayer) p;
		
		GameProfile gp = cp.getProfile();
		
		try {
			gp = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skin));
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		Collection<Property> props = gp.getProperties().get("textures");
        cp.getProfile().getProperties().removeAll("textures");
        cp.getProfile().getProperties().putAll("textures", props);
	}
	
	@Override
	public void unnickPlayer(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		
		try {
			name.set(cp.getProfile(), UUIDFetcher.getName(p.getUniqueId()));
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		skinPlayer(p, UUIDFetcher.getName(p.getUniqueId()));
		
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPackets(destroy, p);
		PacketPlayOutPlayerInfo removeTab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
		sendPacketsToEveryone(removeTab);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				PacketPlayOutPlayerInfo addTab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
				sendPacketsToEveryone(addTab);
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				sendPackets(spawn, p);
			}
		}.runTaskLater(CompletePermissions.getInstance(), 4L);
		
		if(NickManager.nicks.containsKey(p.getUniqueId()))
			NickManager.handleRemovenick(p);
		CompletePermissions.getInstance().getUserManager().getPlayer(p).reloadPlayer();
	}
	
	private static void sendPacketsToEveryone(Packet packet) {
		for(Player pl : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	private static void sendPackets(Packet packet, Player p) {
		for(Player pl : Bukkit.getOnlinePlayers()) {
			if(!(pl == p))
				((CraftPlayer)pl).getHandle().playerConnection.sendPacket(packet);
		}
	}	
}
