package com.lielamar.completepermissions.modules;

import org.bukkit.entity.Player;

import com.lielamar.utils.core.interfaces.modules.Group;

public class BukkitNickedPlayer {

	private Player player;
	private String nick;
	private Group group;
	
	public BukkitNickedPlayer(Player p, String nick, Group group) {
		this.setPlayer(p);
		this.setNick(nick);
		this.setGroup(group);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}
