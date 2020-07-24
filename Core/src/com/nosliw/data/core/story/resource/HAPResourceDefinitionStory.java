package com.nosliw.data.core.story.resource;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.story.HAPConnection;
import com.nosliw.data.core.story.HAPElementGroup;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryImp;
import com.nosliw.data.core.story.HAPStoryNode;

public class HAPResourceDefinitionStory extends HAPResourceDefinitionComplexImp implements HAPStory{

	private HAPStoryImp m_story;
	
	public HAPResourceDefinitionStory() {
	}

	public void setStory(HAPStoryImp story) {   this.m_story = story;   }
	
	@Override
	public String getShowType() {   return this.m_story.getShowType();  }
	@Override
	public void setShowType(String showType) {  this.m_story.setShowType(showType);	}

	@Override
	public HAPStoryElement getElement(String categary, String id) {  return this.m_story.getElement(categary, id);  }

	@Override
	public Set<HAPStoryNode> getNodes() {  return this.m_story.getNodes(); }

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_story.getNode(id); }

	@Override
	public Set<HAPConnection> getConnections() {  return this.m_story.getConnections(); }

	@Override
	public HAPConnection getConnection(String id) {  return this.m_story.getConnection(id);  }

	@Override
	public Set<HAPElementGroup> getElementGroups() {   return this.m_story.getElementGroups();  }

	@Override
	public HAPElementGroup getElementGroup(String id) {  return this.m_story.getElementGroup(id);  }

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		this.m_story.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public void addNode(HAPStoryNode node) {
		// TODO Auto-generated method stub
		
	}

}
