package bukkit.lielamar.completepermissions.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import bukkit.lielamar.completepermissions.CompletePermissions;
import bukkit.lielamar.completepermissions.managers.nick.NickManager;
import bukkit.lielamar.completepermissions.permission.CustomPermissions;
import bukkit.lielamar.completepermissions.permission.PermissibleInjector;
import net.lielamar.bukkit.utils.BukkitFileManager;
import net.lielamar.core.interfaces.managers.UserManager;
import net.lielamar.core.interfaces.moduls.User;

public class BukkitUserManager implements UserManager {

	private static BukkitUserManager instance = new BukkitUserManager();
	private HashMap<UUID, User> users = null;
	
	private BukkitUserManager() {}
	
	public static BukkitUserManager getInstance() {
		return instance;
	}
	
	/**
	 * Sets up the Player Manager
	 */
	public void setup() {
		users = new HashMap<UUID, User>();
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			injectPlayer(pl);
		}
	}
	
	/**
	 * Injects a player
	 * 
	 * @param p     Player to inject
	 * @return      A {@link net.lielamar.core.interfaces.moduls.User} object
	 */
	@Override
	public User injectPlayer(Player p) {
		BukkitFileManager bfm = CompletePermissions.getInstance().getBukkitFileManager();
		if(!p.hasPlayedBefore()) {
			bfm.getConfig("nameuuidfetcher").set(p.getName().toLowerCase(), p.getUniqueId().toString());
			bfm.getConfig("nameuuidfetcher").save();
		}
		
		if(!bfm.getConfig("nameuuidfetcher").contains(p.getName().toLowerCase())) {
			bfm.getConfig("nameuuidfetcher").set(p.getName().toLowerCase(), p.getUniqueId().toString());
			bfm.getConfig("nameuuidfetcher").save();
		}
		
		Permissible permissible = new CustomPermissions(p);
		Permissible oldPermissible = PermissibleInjector.inject(p, permissible);
        ((CustomPermissions) permissible).setOldPermissible(oldPermissible);
		User user = CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().getUser(p);
		
		this.users.put(p.getUniqueId(), user.reloadPlayer());
		
		NickManager.handleRenick(p);
		return user;
	}
	
	/**
	 * Ejects a player
	 * 
	 * @param p     Player to eject
	 */
	@Override
	public void ejectPlayer(Player p) {
		PermissibleInjector.eject(p);
		
		if(this.users.containsKey(p.getUniqueId())) {
			this.users.put(p.getUniqueId(), null);
			this.users.remove(p.getUniqueId());
		}
		
		NickManager.handleSavenick(p);
	}

	@Override
	public User getPlayer(Player p) {
		return (users.containsKey(p.getUniqueId()) ? users.get(p.getUniqueId()) : null);
	}
	
	@Override
	public User getPlayer(String name) {
		return CompletePermissions.getInstance().getStorageManager().getStoragePlayerGetterSetter().getUser(name);
	}

	@Override
	public HashMap<UUID, User> getUsers() {
		return this.users;
	}
}
