package net.lielamar.core.backend.storages.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import bukkit.lielamar.completepermissions.CompletePermissions;
import net.lielamar.bukkit.utils.BukkitManager;
import net.lielamar.core.backend.storages.StorageGroupGetterSetter;
import net.lielamar.core.backend.storages.StorageManager;
import net.lielamar.core.backend.storages.StoragePlayerGetterSetter;
import net.lielamar.core.backend.storages.StorageType;

public class MySQLManager implements StorageManager {
	
	private Connection connection;
	private MySQLPlayerGetterSetter playerGetterSetter;
	private MySQLGroupGetterSetter groupGetterSetter;
	
	public static final String groupsTable = "groups";
	public static final String groupsParentsTable = "groups_parents";
	public static final String groupsPermissionsTable = "groups_permissions";
	public static final String playersTable = "players";
	public static final String playersGroupsTable = "players_groups";
	public static final String playersPermissionsTable = "players_permissions";
	
	@Override
	public void setup() {
		String host = CompletePermissions.getInstance().getConfig().getString("MySQL.host");
		String database = CompletePermissions.getInstance().getConfig().getString("MySQL.database");
		String username = CompletePermissions.getInstance().getConfig().getString("MySQL.username");
		String password = CompletePermissions.getInstance().getConfig().getString("MySQL.password");
		int port = 3306;
		
		if(connect(host, database, username, password, port)) {
			this.playerGetterSetter = new MySQLPlayerGetterSetter(connection);
			this.groupGetterSetter = new MySQLGroupGetterSetter(connection);
		} else
			useFiles();
	}
	
	public boolean connect(String host, String database, String username, String password, int port) {
		try {
			synchronized (this) {
				if ((getConnection() != null) && (!getConnection().isClosed())) {
					return true;
				}
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/"
						+ database + "?createDatabaseIfNotExist=true", username, password));
				setupDatabase();
				BukkitManager.sendConsoleMessage(CompletePermissions.getConsoleSender(), ChatColor.GREEN + "[MySQL] Done setup");
			}
			return true;
		} catch (SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setupDatabase() {
		try {
			String tbl_groups = "CREATE TABLE IF NOT EXISTS `" + groupsTable + "` (`name` varchar(64), `prefix` varchar(32), `suffix` varchar(32), `isdefault` varchar(10), `priority` varchar(10));";
			String tbl_groups_permissions = "CREATE TABLE IF NOT EXISTS `" + groupsPermissionsTable + "` (`name` varchar(64), `permission` varchar(64));";
			String tbl_groups_parents = "CREATE TABLE IF NOT EXISTS `" + groupsParentsTable + "` (`name` varchar(64), `parent` varchar(64));";

			String tbl_players = "CREATE TABLE IF NOT EXISTS `" + playersTable + "` (`uuid` varchar(40), `name` varchar(40), `prefix` varchar(32), `suffix` varchar(32));";
			String tbl_players_permissions = "CREATE TABLE IF NOT EXISTS `" + playersPermissionsTable + "` (`uuid` varchar(40), `permission` varchar(64));";
			String tbl_players_groups = "CREATE TABLE IF NOT EXISTS `" + playersGroupsTable + "` (`uuid` varchar(40), `parent` varchar(64));";

			this.connection.prepareStatement(tbl_groups).executeUpdate();
			this.connection.prepareStatement(tbl_groups_permissions).executeUpdate();
			this.connection.prepareStatement(tbl_groups_parents).executeUpdate();

			this.connection.prepareStatement(tbl_players).executeUpdate();
			this.connection.prepareStatement(tbl_players_permissions).executeUpdate();
			this.connection.prepareStatement(tbl_players_groups).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public StoragePlayerGetterSetter getStoragePlayerGetterSetter() {
		return this.playerGetterSetter;
	}
	
	@Override
	public StorageGroupGetterSetter getStorageGroupGetterSetter() {
		return this.groupGetterSetter;
	}
	
	public void useFiles() {
		CompletePermissions.getInstance().getBackendManager().setType(StorageType.FILES);
		CompletePermissions.getInstance().setStorageManager(CompletePermissions.getInstance().getBackendManager().setup());
		CompletePermissions.getInstance().getStorageManager().setup();
		BukkitManager.sendConsoleMessage(CompletePermissions.getConsoleSender(), ChatColor.RED + "CompletePermissions couldn't connect to MySQL. Using files instead");
	}
}