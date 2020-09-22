package net.lielamar.bukkit.utils;

import org.bukkit.Bukkit;

import net.lielamar.utils.JavaReflectionManager;

public class BukkitReflectionManager {

	public static Class<?> getNMSClass(String className) {
		return JavaReflectionManager.getClass("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3], className);
	}
	
	public static Class<?> getClass(String packageName, String className) {
		return JavaReflectionManager.getClass(packageName + "." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3], className);
	}
}
