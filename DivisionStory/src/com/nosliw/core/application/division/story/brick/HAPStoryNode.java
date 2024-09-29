package com.nosliw.core.application.division.story.brick;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPStoryNode extends HAPStoryElement{

	@HAPAttribute
	public static final String CONNECTIONS = "connection";

	//connections node connect to
	List<String> getConnections();
	
	void addConnection(String connectionId);
	
}
