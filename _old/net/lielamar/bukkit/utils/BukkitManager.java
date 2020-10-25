package net.lielamar.bukkit.utils;

import org.bukkit.command.ConsoleCommandSender;

public class BukkitManager {

	public static void sendConsoleMessage(ConsoleCommandSender cs, String... messages) {
		for(String s : messages)
			cs.sendMessage(s);
	}
}
