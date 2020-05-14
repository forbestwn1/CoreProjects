package com.nosliw.data.core.story.element.node;

import com.nosliw.data.core.service.provide.HAPDefinitionService;
import com.nosliw.data.core.story.HAPProfile;

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
