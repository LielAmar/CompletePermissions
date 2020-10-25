package com.lielamar.completepermissions.managers.nick;

import org.bukkit.entity.Player;

import com.lielamar.utils.core.interfaces.modules.Group;

public interface NickVersionManager {

	public void nickPlayer(Player p, String nick, Group g);
	
	public void skinPlayer(Player p, String skin);
	
	public void unnickPlayer(Player p);
	
}
