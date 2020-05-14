package com.nosliw.data.core.story.resource;

import java.util.Set;

import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.story.HAPConnection;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryImp;
import com.nosliw.data.core.story.HAPStoryNode;

public class HAPResourceDefinitionStory extends HAPResourceDefinitionComplexImp implements HAPStory{

	private HAPStoryImp m_story;
	
	public HAPResourceDefinitionStory() {
	}

	@Override
	public Set<HAPStoryNode> getNodes() {  return this.m_story.getNodes(); }

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_story.getNode(id); }

	@Override
	public Set<HAPConnection> getConnections() {  return this.m_story.getConnections(); }

	@Override
	public HAPConnection getConnection(String id) {  return this.m_story.getConnection(id);  }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}
}
