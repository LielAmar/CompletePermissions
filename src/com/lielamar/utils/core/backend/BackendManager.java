package com.lielamar.utils.core.backend;

import com.lielamar.utils.bukkit.utils.BukkitFileManager.Config;
import com.lielamar.utils.core.backend.storages.StorageManager;
import com.lielamar.utils.core.backend.storages.StorageType;
import com.lielamar.utils.core.backend.storages.files.FilesManager;
import com.lielamar.utils.core.backend.storages.mysql.MySQLManager;

public class BackendManager {

	private StorageType type;
	private StorageManager manager;
	
	public BackendManager(Config config) {
		boolean mysql;
		if(config.contains("MySQL.enabled"))
			mysql = (boolean)config.get("MySQL.enabled");
		else
			mysql = false;
		
		if(mysql)
			type = StorageType.MYSQL;
		else
			type = StorageType.FILES;
		
		setup();
	}
	
	public StorageType getType() {
		return this.type;
	}
	
	public void setType(StorageType type) {
		this.type = type;
	}
	
	public StorageManager setup() {
		switch(type) {
			case MYSQL:
				manager = new MySQLManager();
				break;
			case FILES:
				manager = new FilesManager();
				break;
			default:
				manager = new FilesManager();
				break;
		}
		return manager;
	}
	
	public StorageManager getStorageManager() {
		return manager;
	}
}
