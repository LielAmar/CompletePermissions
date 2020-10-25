package com.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.command.CommandSender;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.commands.CommandPermissionManager;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.bukkit.commands.BukkitSubCommand;
import com.lielamar.utils.core.interfaces.modules.Group;
import com.lielamar.utils.core.interfaces.modules.User;

public class PlayerSubCommand extends BukkitSubCommand {

	@Override
	public void onCommand(CommandSender cs, String[] args) {
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return;
		}
		
		if(args.length == 0) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		String target = args[0].toLowerCase();
		
		if(args.length == 1) {
			User targetUser = CompletePermissions.getInstance().getUserManager().getPlayer(target);
			if(targetUser == null) {
				cs.sendMessage(Messages.noDataOnPlayer());
				return;
			}
			cs.sendMessage(targetUser.toString());
			return;
		}
		
		if(args.length < 3) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		String value = "";
		switch(args[1].toLowerCase()) {
			case "add":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.add", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().addPermissionToPlayer(target, args[2].toLowerCase()));
				break;
			case "remove":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.remove", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().removePermissionFromPlayer(target, args[2].toLowerCase()));
				break;
			case "prefix":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.prefix", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				value = "";
				for (int i = 2; i < args.length; i++) {
					value = value + args[i] + " ";
				}
				value = value.substring(0, value.length() - 1);
				if ((value.startsWith("\"")) && (value.endsWith("\""))) {
					value = value.substring(1, value.length()-1);
				}
				if (value.length() > 16) {
					value = value.substring(0, 16);
				}
				
				if(args[2].toLowerCase().equalsIgnoreCase("reset"))
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setPrefixForPlayer(target, ""));
				else
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setPrefixForPlayer(target, value));
				break;
			case "suffix":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.suffix", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				value = "";
				for (int i = 2; i < args.length; i++) {
					value = value + args[i] + " ";
				}
				value = value.substring(0, value.length() - 1);
				if ((value.startsWith("\"")) && (value.endsWith("\""))) {
					value = value.substring(1, value.length() - 1);
				}
				if (value.length() > 16) {
					value = value.substring(0, 16);
				}
				
				if(args[2].toLowerCase().equalsIgnoreCase("reset"))
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setSuffixForPlayer(target, ""));
				else
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setSuffixForPlayer(target, value));
				break;
			case "group":				
				if(args.length < 4) {
					cs.sendMessage(Messages.getCommandUsageMessage());
					return;
				}
				
				String groupName;
				Group group;
				switch(args[2].toLowerCase()) {
					
					case "add":
						if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.group.add", "cp.commands.cp.*")) {
							cs.sendMessage(Messages.getNoPermissionsMessage());
							return;
						}
						
						groupName = args[3].toLowerCase();
						group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
						if(group == null) {
							cs.sendMessage(Messages.groupNotFoundMessage());
							return;
						}
						cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().addGroupToPlayer(target, group));
						break;
					case "remove":
						if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.group.remove", "cp.commands.cp.*")) {
							cs.sendMessage(Messages.getNoPermissionsMessage());
							return;
						}
						
						groupName = args[3].toLowerCase();
						group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
						if(group == null) {
							cs.sendMessage(Messages.groupNotFoundMessage());
							return;
						}
						cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().removeGroupFromPlayer(target, group));
						break;
					case "set":
						if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.player.group.set", "cp.commands.cp.*")) {
							cs.sendMessage(Messages.getNoPermissionsMessage());
							return;
						}
						
						groupName = args[3].toLowerCase();
						group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
						if(group == null) {
							cs.sendMessage(Messages.groupNotFoundMessage());
							return;
						}
						cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().setGroupToPlayer(target, group));
						break;
					default:
						cs.sendMessage(Messages.getCommandUsageMessage());
						break;
				}
				break;
			default:
				cs.sendMessage(Messages.getCommandUsageMessage());
				break;
		}
	}

	@Override
	public String getName() {
		return CompletePermissions.getInstance().getCompletePermissionsCommandManager().player;
	}

	@Override
	public String getInfo() {
		return "";
	}

	@Override
	public String[] aliases() {
		return new String[] {"p"};
	}
}
