package com.nosliw.data.core.story.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPContainerChildResource;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPConnection;
import com.nosliw.data.core.story.HAPElementGroup;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryImp;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPHandlerChange;
import com.nosliw.data.core.story.change.HAPRequestChange;
import com.nosliw.data.core.story.change.HAPResultTransaction;

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
	public HAPContainerChildResource getChildrenResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		this.m_story.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPStoryElement addElement(HAPStoryElement element, HAPAliasElement alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement getElement(HAPReferenceElement elementRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement getElement(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPIdElement getElementId(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPIdElement setAlias(HAPAliasElement alias, HAPIdElement eleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPAliasElement getAlias(HAPIdElement eleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement deleteElement(HAPStoryElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement deleteElement(String categary, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryNode addNode(HAPStoryNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPConnection addConnection(HAPConnection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPElementGroup addElementGroup(HAPElementGroup connectionGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPResultTransaction commitTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollbackTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPAliasElement generateTemporaryAlias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerChangeHandler(HAPHandlerChange handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterChangeHandler(HAPHandlerChange handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPChangeItem> change(HAPRequestChange changeRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
