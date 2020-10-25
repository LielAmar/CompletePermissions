package bukkit.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.command.CommandSender;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.commands.CommandPermissionManager;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.bukkit.commands.BukkitSubCommand;

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
