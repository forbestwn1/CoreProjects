package com.nosliw.data.core.story;

import java.util.Set;

public interface HAPStoryNode extends HAPStoryElement{

	public static final String CONNECTIONS = "connection";

	//connections node connect to
	Set<String> getConnections();
	
	void addConnection(String connectionId);
	
}
