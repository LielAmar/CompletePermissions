package com.lielamar.utils.core.backend.storages.files;

import com.lielamar.completepermissions.CompletePermissions;
import com.lielamar.utils.bukkit.utils.BukkitFileManager.Config;
import com.lielamar.utils.core.backend.storages.StorageGroupGetterSetter;
import com.lielamar.utils.core.backend.storages.StorageManager;
import com.lielamar.utils.core.backend.storages.StoragePlayerGetterSetter;

public class FilesManager implements StorageManager {

	private FilesPlayerGetterSetter playerGetterSetter;
	private FilesGroupGetterSetter groupGetterSetter;
	
	@Override
	public void setup() {
		Config config = CompletePermissions.getInstance().getBukkitFileManager().getConfig("permissions");
		config.reload();
		this.playerGetterSetter = new FilesPlayerGetterSetter(config);
		this.groupGetterSetter = new FilesGroupGetterSetter(config);
	}

	@Override
	public StoragePlayerGetterSetter getStoragePlayerGetterSetter() {
		return this.playerGetterSetter;
	}
	
	@Override
	public StorageGroupGetterSetter getStorageGroupGetterSetter() {
		return this.groupGetterSetter;
	}
}
