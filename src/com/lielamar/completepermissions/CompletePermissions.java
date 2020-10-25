package com.lielamar.completepermissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lielamar.completepermissions.commands.CompletePermissionsCommandManager;
import com.lielamar.completepermissions.commands.NickCommandManager;
import com.lielamar.completepermissions.listeners.BukkitPlayerChatEvents;
import com.lielamar.completepermissions.listeners.BukkitPlayerConnectionsEvent;
import com.lielamar.completepermissions.managers.BukkitGroupManager;
import com.lielamar.completepermissions.managers.BukkitUserManager;
import com.lielamar.completepermissions.managers.nick.NickManager;
import com.lielamar.completepermissions.managers.nick.NickVersionManager;
import com.lielamar.completepermissions.utils.Utils;
import com.lielamar.utils.bukkit.utils.BukkitFileManager;
import com.lielamar.utils.bukkit.utils.BukkitManager;
import com.lielamar.utils.core.backend.BackendManager;
import com.lielamar.utils.core.backend.storages.StorageManager;
import com.lielamar.utils.core.interfaces.managers.GroupManager;
import com.lielamar.utils.core.interfaces.managers.UserManager;

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
		instance = this;
		cs = Bukkit.getConsoleSender();
		BukkitManager.sendConsoleMessage(cs, ChatColor.GREEN + "- Enabling CompletePermissions v" + getDescription().getVersion());
		
		setupManagers();
		setupPlugin();
	}
	
	@Override
	public void onDisable() {
		BukkitManager.sendConsoleMessage(cs, ChatColor.RED + "- Disabling CompletePermissions v" + getDescription().getVersion());
		
		destroyManagers();
		cs = null;
	}

	public void setupManagers() {
		saveDefaultConfig();
		
		this.bfm = new BukkitFileManager(this);
		this.utils = Utils.getInstance().setup();
		this.bm = new BackendManager(bfm.getConfig("config"));
		this.sm = this.bm.getStorageManager();
		this.sm.setup();
		this.gm = BukkitGroupManager.getInstance();
		this.gm.setup();
		this.um = BukkitUserManager.getInstance();
		this.um.setup();
		this.nm = NickManager.setup();
//		NickManager.reloadNicks(); // Causes a bug which keeps players on tablist when reloading the server
		unregisterCommands();
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
