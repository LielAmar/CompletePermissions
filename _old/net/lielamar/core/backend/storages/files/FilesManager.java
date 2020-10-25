package net.lielamar.core.backend.storages.files;

import bukkit.lielamar.completepermissions.CompletePermissions;
import net.lielamar.bukkit.utils.BukkitFileManager.Config;
import net.lielamar.core.backend.storages.StorageGroupGetterSetter;
import net.lielamar.core.backend.storages.StorageManager;
import net.lielamar.core.backend.storages.StoragePlayerGetterSetter;

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
