/*
TODO: Copyrights
*/

package bukkit.lielamar.completepermissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import bukkit.lielamar.completepermissions.commands.CompletePermissionsCommandManager;
import bukkit.lielamar.completepermissions.commands.NickCommandManager;
import bukkit.lielamar.completepermissions.listeners.BukkitPlayerChatEvents;
import bukkit.lielamar.completepermissions.listeners.BukkitPlayerConnectionsEvent;
import bukkit.lielamar.completepermissions.managers.BukkitGroupManager;
import bukkit.lielamar.completepermissions.managers.BukkitUserManager;
import bukkit.lielamar.completepermissions.managers.nick.NickManager;
import bukkit.lielamar.completepermissions.managers.nick.NickVersionManager;
import bukkit.lielamar.completepermissions.utils.Utils;
import net.lielamar.bukkit.utils.BukkitFileManager;
import net.lielamar.bukkit.utils.BukkitManager;
import net.lielamar.core.backend.BackendManager;
import net.lielamar.core.backend.storages.StorageManager;
import net.lielamar.core.interfaces.managers.GroupManager;
import net.lielamar.core.interfaces.managers.UserManager;

public class CompletePermissions extends JavaPlugin {

	private static CompletePermissions instance;
	private static ConsoleCommandSender cs;
	
	private BukkitFileManager bfm;
	private Utils utils;
	private BackendManager bm;
	private StorageManager sm;
	private BukkitGroupManager gm;
	private BukkitUserManager um;
	private CompletePermissionsCommandManager cpcm;
	private NickCommandManager ncm;
	private NickVersionManager nm;
	
	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		cs = Bukkit.getConsoleSender();
		BukkitManager.sendConsoleMessage(cs, ChatColor.GREEN + "- Enabling CompletePermissions v" + getDescription().getVersion());
		
		setupManagers();
		setupPlugin();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		BukkitManager.sendConsoleMessage(cs, ChatColor.RED + "- Disabling CompletePermissions v" + getDescription().getVersion());
		
		destroyManagers();
		cs = null;
	}

	public void setupManagers() {
		saveDefaultConfig();
		
		this.bfm = new BukkitFileManager(this);
		this.utils = Utils.getInstance().setup();
		this.bm = new BackendManager(bfm.getConfig("config"));
		this.sm = this.bm.getStorageManager(); this.sm.setup();
		this.gm = BukkitGroupManager.getInstance(); this.gm.setup();
		this.um = BukkitUserManager.getInstance(); this.um.setup();
		this.nm = NickManager.setup(); NickManager.reloadNicks();
		unregisterCommands();
		registerCommands();
	}

	protected void destroyManagers() {
		NickManager.export();
		for(Player pl : Bukkit.getOnlinePlayers())
			this.um.ejectPlayer(pl);
		this.bfm = null;
		this.bm = null;
		this.sm = null;
		this.um = null;
		this.gm = null;
		System.gc();
	}

	protected void setupPlugin() {
		registerCommands();
		registerEvents();
	}

	protected void registerCommands() {
		this.cpcm = CompletePermissionsCommandManager.getInstance();
		this.cpcm.setup(this);
		this.ncm = NickCommandManager.getInstance();
		this.ncm.setup(this);
	}
	
	protected void unregisterCommands() {
		getCommand("completepermissions").unregister(null);
		getCommand("completepermissions").unregister(null);
		this.cpcm = null;
		this.ncm = null;
	}

	protected void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BukkitPlayerChatEvents(), this);
		pm.registerEvents(new BukkitPlayerConnectionsEvent(), this);
	}
	
	public static CompletePermissions getInstance() {
		return instance;
	}

	public static ConsoleCommandSender getConsoleSender() {
		return cs;
	}
	
	public BukkitFileManager getBukkitFileManager() {
		return this.bfm;
	}
	
	public BackendManager getBackendManager() {
		return this.bm;
	}
	
	public Utils getUtils() {
		return this.utils;
	}
	
	public StorageManager getStorageManager() {
		return this.sm;
	}
	
	public void setStorageManager(StorageManager sm) {
		this.sm = sm;
	}
	
	public GroupManager getGroupManager() {
		return this.gm;
	}
		
	public UserManager getUserManager() {
		return this.um;

	}
	
	public NickVersionManager getNickManager() {
		return this.nm;
	}

	public CompletePermissionsCommandManager getCompletePermissionsCommandManager() {
		return cpcm;
	}
}
