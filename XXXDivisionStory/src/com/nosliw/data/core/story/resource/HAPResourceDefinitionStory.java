package com.nosliw.data.core.story.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryStoryImp;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryHandlerChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChange;
import com.nosliw.core.application.division.story.change.HAPStoryResultTransaction;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryElementGroup;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;

public class HAPResourceDefinitionStory extends HAPResourceDefinitionComplexImp implements HAPStoryStory{

	private HAPStoryStoryImp m_story;
	
	public HAPResourceDefinitionStory() {
	}

	public void setStory(HAPStoryStoryImp story) {   this.m_story = story;   }
	
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
	public Set<HAPStoryConnection> getConnections() {  return this.m_story.getConnections(); }

	@Override
	public HAPStoryConnection getConnection(String id) {  return this.m_story.getConnection(id);  }

	@Override
	public Set<HAPStoryElementGroup> getElementGroups() {   return this.m_story.getElementGroups();  }

	@Override
	public HAPStoryElementGroup getElementGroup(String id) {  return this.m_story.getElementGroup(id);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		this.m_story.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPStoryElement addElement(HAPStoryElement element, HAPStoryAliasElement alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement getElement(HAPStoryReferenceElement elementRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement getElement(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryIdElement getElementId(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryIdElement setAlias(HAPStoryAliasElement alias, HAPStoryIdElement eleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryAliasElement getAlias(HAPStoryIdElement eleId) {
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
	public HAPStoryConnection addConnection(HAPStoryConnection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElementGroup addElementGroup(HAPStoryElementGroup connectionGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPStoryResultTransaction commitTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollbackTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPStoryAliasElement generateTemporaryAlias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerChangeHandler(HAPStoryHandlerChange handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterChangeHandler(HAPStoryHandlerChange handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPStoryChangeItem> change(HAPStoryRequestChange changeRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryElement deleteElement(HAPStoryIdElement eleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStoryRequestChange newRequestChange(Boolean extend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNextId() {
		// TODO Auto-generated method stub
		return null;
	}

}
