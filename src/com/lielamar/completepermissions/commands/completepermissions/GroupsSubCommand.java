package com.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.commands.CommandPermissionManager;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.bukkit.commands.BukkitSubCommand;
import com.lielamar.utils.core.interfaces.modules.Group;

public class GroupsSubCommand extends BukkitSubCommand {

	@Override
	public void onCommand(CommandSender cs, String[] args) {
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.groups", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return;
		}
		
		ChatColor color = ChatColor.GRAY;
		for(Group group : CompletePermissions.getInstance().getGroupManager().getGroups().values()) {
			cs.sendMessage(color + group.toString());
			if(color == ChatColor.GRAY)
				color = ChatColor.YELLOW;
		}
	}

	@Override
	public String getName() {
		return CompletePermissions.getInstance().getCompletePermissionsCommandManager().groups;
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
