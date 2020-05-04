package com.nosliw.data.core.resource.dynamic.story;

import java.util.List;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPStoryNode extends HAPEntityInfo{

	String getType();
	
	Object getEntity();
	
	List<HAPConnection> getConnection();
	
	HAPProfile getProfile(String type);
}
