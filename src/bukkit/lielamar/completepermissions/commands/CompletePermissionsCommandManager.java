package bukkit.lielamar.completepermissions.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import bukkit.lielamar.completepermissions.commands.completepermissions.ConfigSubCommand;
import bukkit.lielamar.completepermissions.commands.completepermissions.GroupSubCommand;
import bukkit.lielamar.completepermissions.commands.completepermissions.GroupsSubCommand;
import bukkit.lielamar.completepermissions.commands.completepermissions.PermissionsSubCommand;
import bukkit.lielamar.completepermissions.commands.completepermissions.PlayerSubCommand;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.bukkit.commands.BukkitCommandManager;
import net.lielamar.bukkit.commands.BukkitSubCommand;

public class CompletePermissionsCommandManager implements CommandExecutor, BukkitCommandManager {
	
	private static CompletePermissionsCommandManager instance = new CompletePermissionsCommandManager();
	private CompletePermissionsCommandManager() {}
	public static CompletePermissionsCommandManager getInstance() {
		return instance;
	}
	
	private List<BukkitSubCommand> subcommands = new ArrayList<BukkitSubCommand>();
	
	public final String main = "completepermissions";
	public final String player = "player";
	public final String group = "group";
	public final String groups = "groups";
	public final String config = "config";
	public final String permissions = "permissions";
	
    /**
     * Sets the CompletePermissions sub-commands
     */
	public void setup(JavaPlugin plugin) {
		plugin.getCommand(main).setExecutor(this);
		
		this.subcommands.add(new PlayerSubCommand());
		this.subcommands.add(new GroupSubCommand());
		this.subcommands.add(new GroupsSubCommand());
		this.subcommands.add(new ConfigSubCommand());
		this.subcommands.add(new PermissionsSubCommand());
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {
		
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.cp", "cp.commands.cp.*")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase(main)) {
			if(args.length == 0) {
				cs.sendMessage(Messages.getCommandUsageMessage());
				return true;
			}
			BukkitSubCommand subcommand = getSubCommand(args[0].toLowerCase());
			if(subcommand == null) {
				cs.sendMessage(Messages.getCommandUsageMessage());
				return true;
			}
			
			String[] arguments = argumentFixer(args);
			
			try {
				subcommand.onCommand(cs, arguments);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return true;
	}

    /**
     * @param name      Name of a Sub-Command
     * @return          A {@link net.lielamar.bukkit.commands.SubCommand} instance
     */
	public BukkitSubCommand getSubCommand(String name) {
		Iterator<BukkitSubCommand> subcommands = this.subcommands.iterator();
		while(subcommands.hasNext()) {
			BukkitSubCommand sc = (BukkitSubCommand) subcommands.next();
			if(sc.getName().equalsIgnoreCase(name))
				return sc;
			
			for(String s : sc.aliases())
				if(s.equalsIgnoreCase(name))
					return sc;
		}
		return null;
	}
	
	public String[] argumentFixer(String[] args) {
		String[] argumentsTemp = new String[args.length];
		for(int i = 0; i < args.length; i++)
			argumentsTemp[i] = args[i];
		
		String[] arguments = new String[argumentsTemp.length-1];
		for(int i = 0; i < arguments.length; i++) {
			arguments[i] = argumentsTemp[i+1];
		}
		return arguments;
	}
}
