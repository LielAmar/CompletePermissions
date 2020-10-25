package net.lielamar.core.interfaces.moduls;

import java.util.List;
import java.util.Map;

public interface Group {

	String getName();
	Map<String, Group> getParents();
	List<String> getParentsRaw();
	List<String> getPermissions();
	String getPrefix();
	String getSuffix();
	boolean isDefault();
	char getPriority();

	void setName(String name);
	void setParents(Map<String, Group> parents);
	void setParentsRaw(List<String> parentsRaw);
	void setPermissions(List<String> permissions);
	void setPrefix(String prefix);
	void setSuffix(String suffix);
	void setDefault(boolean set);
	void setPriority(char prioritry);
	
	void reloadRelatedPlayers();
	
	String toString();
	
}
