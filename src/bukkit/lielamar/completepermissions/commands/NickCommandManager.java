package bukkit.lielamar.completepermissions.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.managers.nick.NickManager;
import bukkit.lielamar.completepermissions.utils.Messages;
import net.lielamar.bukkit.commands.BukkitCommandManager;
import net.lielamar.bukkit.commands.BukkitSubCommand;
import net.lielamar.core.interfaces.moduls.Group;

public class NickCommandManager implements CommandExecutor, BukkitCommandManager {
	
	private static NickCommandManager instance = new NickCommandManager();
	private NickCommandManager() {}
	public static NickCommandManager getInstance() {
		return instance;
	}
	
	private List<BukkitSubCommand> subcommands = new ArrayList<BukkitSubCommand>();
	private List<Group> nickgroups = new ArrayList<Group>();
	
	public final String main = "nick";
	public final String main2 = "unnick";
	
	public void setup(JavaPlugin plugin) {
		plugin.getCommand(main).setExecutor(this);
		plugin.getCommand(main2).setExecutor(this);
		
		Group group = null;
		
		for(String s : CompletePermissions.getInstance().getBukkitFileManager().getConfig("config").getStringList("NickGroups")) {
			group = CompletePermissions.getInstance().getGroupManager().getGroup(s);
			if(group != null)
				nickgroups.add(group);
		}
	}
	
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
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String cmdLabel, String[] args) {
		
		if(!CommandPermissionManager.hasPermission(cs, "cp.commands.nick")) {
			cs.sendMessage(Messages.getNoPermissionsMessage());
			return true;
		}
		
		if(!(cs instanceof Player)) {
			cs.sendMessage(Messages.consoleSenderMessage());
			return true;
		}
		
		Player p = (Player)cs;
		
		if(cmd.getName().equalsIgnoreCase(main)) {
			// cp
			if(args.length < 2) {
				p.sendMessage(Messages.getNickCommandUsageMessage());
				return true;
			}
			
			String nick = args[0];
			String groupName = args[1];
			Group group = CompletePermissions.getInstance().getGroupManager().getGroup(groupName);
			
			if(nick.length() < 4) {
				p.sendMessage(Messages.youCantNickThisNickMessage());
				return true;
			}
			
			if(group == null) {
				p.sendMessage(Messages.groupNotFoundMessage());
				return true;
			}
			
			if(!nickgroups.contains(group) && !CommandPermissionManager.hasPermission(cs, "cp.commands.nick.anygroup")) {
				p.sendMessage(Messages.youCantNickThisGroupMessage());
				return true;
			}
			
			CompletePermissions.getInstance().getNickManager().nickPlayer(p, nick, group);
			p.sendMessage(ChatColor.GREEN + "Nicking as " + nick + " in group " + group.getName());
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase(main2)) {
			if(NickManager.nicks.containsKey(p.getUniqueId())) {
				CompletePermissions.getInstance().getNickManager().unnickPlayer(p);
				p.sendMessage(ChatColor.RED + "Unnicking");
			}
		}
		return true;
	}
}
