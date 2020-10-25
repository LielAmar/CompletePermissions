package com.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.command.CommandSender;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.completepermissions.commands.CommandPermissionManager;
import com.lielamar.completepermissions.utils.Messages;
import com.lielamar.utils.bukkit.commands.BukkitSubCommand;
import com.lielamar.utils.core.interfaces.modules.Group;

public class GroupSubCommand extends BukkitSubCommand {
	
	@Override
	public void onCommand(CommandSender cs, String[] args) {
		
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return;
		}
		
		if(args.length == 0) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		Group target = CompletePermissions.getInstance().getGroupManager().getGroup(args[0]);
		
		if(args.length == 1) {
			if(target == null) {
				cs.sendMessage(Messages.groupNotFoundMessage());
				return;
			}
			cs.sendMessage(target.toString());
			return;
		}
		
		if(args.length < 2) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		String value = "";
		switch(args[1].toLowerCase()) {
			case "create":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.create", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().createGroup(args[0].toLowerCase()));
				break;
			case "delete":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.delete", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().deleteGroup(args[0].toLowerCase()));
				break;
			case "add":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.add", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
					return;
				}
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().addPermissionToGroup(target, args[2].toLowerCase()));
				break;
			case "remove":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.remove", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
					return;
				}
				cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().removePermissionFromGroup(target, args[2].toLowerCase()));
				break;
			case "prefix":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.prefix", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
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
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setPrefixForGroup(target, ""));
				else
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setPrefixForGroup(target, value));
				break;
			case "suffix":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.suffix", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
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
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setSuffixForGroup(target, ""));
				else
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setSuffixForGroup(target, value));
				break;
			case "parents":
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
					return;
				}
				
				if(args.length < 4) {
					cs.sendMessage(Messages.getCommandUsageMessage());
					return;
				}
				
				String parentName;
				Group parent;
				switch(args[2].toLowerCase()) {
					
					case "add":
						if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.parents.add", "cp.commands.cp.*")) {
							cs.sendMessage(Messages.getNoPermissionsMessage());
							return;
						}
						
						parentName = args[3].toLowerCase();
						parent = CompletePermissions.getInstance().getGroupManager().getGroup(parentName);
						if(parent == null) {
							cs.sendMessage(Messages.groupNotFoundMessage());
							return;
						}
						cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().addParentToGroup(target, parent));
						break;
					case "remove":
						if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.parents.remove", "cp.commands.cp.*")) {
							cs.sendMessage(Messages.getNoPermissionsMessage());
							return;
						}
						
						parentName = args[3].toLowerCase();
						parent = CompletePermissions.getInstance().getGroupManager().getGroup(parentName);
						if(parent == null) {
							cs.sendMessage(Messages.groupNotFoundMessage());
							return;
						}
						cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().removeParentFromGroup(target, parent));
						break;
					default:
						cs.sendMessage(Messages.getCommandUsageMessage());
						break;
				}
				break;
			case "priority":
				if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.group.priority", "cp.commands.cp.*")) {
					cs.sendMessage(Messages.getNoPermissionsMessage());
					return;
				}
				
				if(target == null) {
					cs.sendMessage(Messages.groupNotFoundMessage());
					return;
				}
				
				if(args.length < 2) {
					cs.sendMessage(Messages.getCommandUsageMessage());
					return;
				}
				
				char cValue = args[2].charAt(0);
				
				if(args[2].toLowerCase().equalsIgnoreCase("reset"))
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setPriorityForGroup(target, 'z'));
				else
					cs.sendMessage(CompletePermissions.getInstance().getStorageManager().getStorageGroupGetterSetter().setPriorityForGroup(target, cValue));
				break;
			default:
				cs.sendMessage(Messages.getCommandUsageMessage());
				break;
		}
	}

	@Override
	public String getName() {
		return CompletePermissions.getInstance().getCompletePermissionsCommandManager().group;
	}

	@Override
	public String getInfo() {
		return "";
	}

	@Override
	public String[] aliases() {
		return new String[] {"g"};
	}

}
