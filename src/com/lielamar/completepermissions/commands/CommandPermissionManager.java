package com.lielamar.completepermissions.commands;

import org.bukkit.command.CommandSender;

public class CommandPermissionManager {
	
	/**
	 * Checking whether the sender has permissions (multiple checks at once)
	 * 
	 * @param cs            Command Sender
	 * @param permissions   Permission to check
	 * @return              Whether or not CommandSender has a permission
	 */
	public static boolean hasPermission(CommandSender cs, String... permissions) {
		for(String permission : permissions) {
			if(cs.hasPermission(permission)) return true;
		}
		return false;
	}
}
