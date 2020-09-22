package bukkit.lielamar.completepermissions.utils;

import org.bukkit.ChatColor;

public class Messages {
	
	public static String getNoPermissionsMessage() {
		return ChatColor.RED + "You don't have enough permissions to do that!";
	}
	
	public static String consoleSenderMessage() {
		return ChatColor.RED + "You must be a player to nick!";
	}
	
	public static String getSavedGroupMessage() {
		return ChatColor.GREEN + "Group saved!";
	}
	
	public static String getSavedUserMessage() {
		return ChatColor.GREEN + "User saved!";
	}
	
	public static String getCreatedGroupMessage() {
		return ChatColor.GREEN + "Created group!";
	}
	
	public static String getGroupExistsMessage() {
		return ChatColor.RED + "Group already exists!";
	}
	
	public static String getDeletedGroupMessage() {
		return ChatColor.RED + "Deleted group!";
	}
	
	public static String addedPermissionToGroupMessage() {
		return ChatColor.GREEN + "Adding permission to group";
	}
	
	public static String addedParentToGroupMessage() {
		return ChatColor.GREEN + "Adding parent to group";
	}
	
	public static String removedPermissionToGroupMessage() {
		return ChatColor.RED + "Removing permission from group";
	}
	
	public static String removedParentToGroupMessage() {
		return ChatColor.RED + "Removing parent from group";
	}
	
	public static String setPrefixForGroupMessage() {
		return ChatColor.GREEN + "Setting group prefix";
	}
	
	public static String setSuffixForGroupMessage() {
		return ChatColor.GREEN + "Setting group suffix";
	}
	
	public static String setPriorityForGroupMessage() {
		return ChatColor.GREEN + "Setting group priority";
	}
	
	public static String resetPrefixForGroupMessage() {
		return ChatColor.GREEN + "Resetting group prefix";
	}
	
	public static String resetSuffixForGroupMessage() {
		return ChatColor.GREEN + "Resetting group suffix";
	}
	
	public static String addedPermissionToPlayerMessage() {
		return ChatColor.GREEN + "Adding permission to player";
	}
	
	public static String addedGroupToPlayerMessage() {
		return ChatColor.GREEN + "Adding group to player";
	}
	
	public static String removedPermissionToPlayerMessage() {
		return ChatColor.RED + "Removing permission from player";
	}
	
	public static String removedGroupToPlayerMessage() {
		return ChatColor.RED + "Removing group from player";
	}
	
	public static String setGroupToPlayerMessage() {
		return ChatColor.GREEN + "Setting player group";
	}
	
	public static String setPrefixForPlayerMessage() {
		return ChatColor.GREEN + "Setting player prefix";
	}
	
	public static String setSuffixForPlayerMessage() {
		return ChatColor.GREEN + "Setting player suffix";
	}
	
	public static String resetPrefixForPlayerMessage() {
		return ChatColor.GREEN + "Resetting player prefix";
	}
	
	public static String resetSuffixForPlayerMessage() {
		return ChatColor.GREEN + "Resetting player suffix";
	}
	
	public static String playerIsNotOnlineMessage() {
		return ChatColor.RED + "Player is not online";
	}
	
	public static String noDataOnPlayer() {
		return ChatColor.RED + "There is no data for this player!";
	}
	
	public static String groupNotFoundMessage() {
		return ChatColor.RED + "Couldn't find group";
	}
	
	public static String reloadedConfigMessage() {
		return ChatColor.GREEN + "Config was reloaded";
	}
	
	public static String reloadedPermissionsMessage() {
		return ChatColor.GREEN + "Permissions were reloaded";
	}
	
	public static String getNickCommandUsageMessage() {
		return ChatColor.RED + "Usage: /Nick <nick> <group>";
	}
	
	public static String youCantNickThisGroupMessage() {
		return ChatColor.RED + "You can't select this group as your nick group!";
	}
	
	public static String getCommandUsageMessage() {
		return 
				ChatColor.YELLOW + "===== Commands for Complete Perms =====" + ChatColor.GRAY + "\n"
				+ "- /completeperms player <player>" + ChatColor.GRAY + "\n"
				+ "- /completeperms player <player> add <permission>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms player <player> remove <permission>" + ChatColor.GRAY + "\n"
				+ "- /completeperms player <player> group set <group>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms player <player> group add <group>" + ChatColor.GRAY + "\n"
				+ "- /completeperms player <player> group remove <group>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms player <player> prefix <prefix>" + ChatColor.GRAY + "\n"
				+ "- /completeperms player <player> suffix <suffix>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms group <group>" + ChatColor.GRAY
				+ "- /completeperms group <group> create" + ChatColor.GRAY+ "\n"
				+ "- /completeperms group <group> delete" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms group <group> add <permission>" + ChatColor.GRAY + "\n"
				+ "- /completeperms group <group> remove <permission>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms group <group> prefix <prefix>" + ChatColor.GRAY + "\n"
				+ "- /completeperms group <group> suffix <suffix>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms group <group> parents add <parent>" + ChatColor.GRAY + "\n"
				+ "- /completeperms group <group> parents remove <parent>" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms groups" + ChatColor.GRAY + "\n"
				+ "- /completeperms config reload" + ChatColor.DARK_GRAY + "\n"
				+ "- /completeperms permissions reload" + ChatColor.GRAY + "\n";
	}
}
