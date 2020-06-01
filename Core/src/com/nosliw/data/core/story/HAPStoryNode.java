package com.nosliw.data.core.story;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPStoryNode extends HAPStoryElement{

	@HAPAttribute
	public static final String CONNECTIONS = "connection";

	//connections node connect to
	Set<String> getConnections();
	
	void addConnection(String connectionId);
	
}
