package bukkit.lielamar.completepermissions.commands.completepermissions;

import org.bukkit.command.CommandSender;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.commands.CommandPermissionManager;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.bukkit.commands.BukkitSubCommand;

public class PermissionsSubCommand extends BukkitSubCommand {

	@Override
	public void onCommand(CommandSender cs, String[] args) {
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp.permissions", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return;
		}
		
		if(args.length == 0) {
			cs.sendMessage(Messages.getCommandUsageMessage());
			return;
		}
		
		if(args[0].equalsIgnoreCase("reload")) {
			CompletePermissions.getInstance().getBukkitFileManager().getConfig("permissions").reload();
			CompletePermissions.getInstance().getGroupManager().setup();
			CompletePermissions.getInstance().getUserManager().setup();
			System.gc();
			cs.sendMessage(Messages.reloadedPermissionsMessage());
		}
	}

	@Override
	public String getName() {
		return CompletePermissions.getInstance().getCompletePermissionsCommandManager().permissions;
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
