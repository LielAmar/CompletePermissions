package net.lielamar.utils;

public class JavaReflectionManager {
	
	/**
	 * Returns a class by package & class
	 * 
	 * @param packageName      Name of a package
	 * @param className        Name of a class
	 * @return                 A {@link Class} object by adding
	 */
	public static Class<?> getClass(String packageName, String className) {
		try {
			return Class.forName(packageName + "." + className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a class by full path
	 * 
	 * @param fullClassName      Full path to class
	 * @return                   A {@link Class} object by adding
	 */
	public static Class<?> getClass(String fullClassName) {
		try {
			return Class.forName(fullClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
