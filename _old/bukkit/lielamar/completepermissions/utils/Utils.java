package bukkit.lielamar.completepermissions.utils;

import bukkit.lielamar.completepermissions.CompletePermissions;

public class Utils {
	
	private static Utils instance = new Utils();
	private String format = null;
	
	private Utils() {
		setup();
	}
	
	public Utils setup() {
		format = (String)CompletePermissions.getInstance().getBukkitFileManager().getConfig("config").get("ChatFormat");
		return this;
	}

	public static Utils getInstance() {
		return instance;
	}
	
	public String getFormat() {
		return this.format;
	}
}
