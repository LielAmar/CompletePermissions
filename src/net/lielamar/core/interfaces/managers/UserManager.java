package net.lielamar.core.interfaces.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.lielamar.core.interfaces.moduls.User;

public interface UserManager {

	void setup();
	void ejectPlayer(Player p);
	User injectPlayer(Player p);
	User getPlayer(String name);
	User getPlayer(Player p);
	HashMap<UUID, User> getUsers();
}
