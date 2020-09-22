package net.lielamar.core.backend.storages;

public interface StorageManager {
	
	void setup();
	StoragePlayerGetterSetter getStoragePlayerGetterSetter();
	StorageGroupGetterSetter getStorageGroupGetterSetter();
}
