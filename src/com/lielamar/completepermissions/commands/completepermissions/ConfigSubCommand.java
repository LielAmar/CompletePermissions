package com.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.command.CommandSender;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.commands.CommandPermissionManager;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.bukkit.commands.BukkitSubCommand;

public class ConfigSubCommand extends BukkitSubCommand {
	
	@Override
	public void onCommand(CommandSender cs, String[] args) {
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.config", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return;
		}
		
		if(args.length == 0) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		if(args[0].equalsIgnoreCase("reload")) {
			CompletePermissions.getInstance().reloadConfig();
			CompletePermissions.getInstance().setupManagers();
			System.gc();
			cs.sendMessage(Messages.reloadedConfigMessage());
		}
	}

	@Override
	public String getName() {
		return CompletePermissions.getInstance().getCompletePermissionsCommandManager().config;
	}

	@Override
	public String getInfo() {
		return "";
	}

	@Override
	public String[] aliases() {
		return new String[] {};
	}

}
