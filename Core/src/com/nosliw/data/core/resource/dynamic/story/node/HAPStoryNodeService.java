package com.nosliw.data.core.resource.dynamic.story.node;

import com.nosliw.data.core.resource.dynamic.story.HAPProfile;
import com.nosliw.data.core.service.provide.HAPDefinitionService;

public class HAPStoryNodeService extends HAPStoryNodeImp{

	private String m_seviceId;
	
	private HAPDefinitionService m_serviceDefinition;
	
	public HAPStoryNodeService(String type) {
		super(type);
	}

	@Override
	public HAPProfile getProfile(String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
