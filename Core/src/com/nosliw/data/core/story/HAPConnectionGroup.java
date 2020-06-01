package com.nosliw.data.core.story;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPConnectionGroup extends HAPStoryElement{

	@HAPAttribute
	public static final String CONNECTIONS = "connections";
	
	Set<String> getConnections();
}
