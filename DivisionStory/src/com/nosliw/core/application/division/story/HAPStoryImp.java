package com.nosliw.core.application.division.story;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroup;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryHandlerChange;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChange;
import com.nosliw.core.application.division.story.change.HAPStoryResultTransaction;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPStoryImp extends HAPEntityInfoImp implements HAPStoryStory{
	
	private HAPStoryManagerChange m_changeMan;
	
	private String m_showType;
	
	private Map<String, HAPStoryNode> m_nodes;
	
	private Map<String, HAPStoryConnection> m_connections;

	private Map<String, HAPStoryElementGroup> m_elementGroups;

	private Map<String, HAPStoryIdElement> m_aliases;
	
	//handlers when transaction commit
	private Set<HAPStoryHandlerChange> m_changeHandlers;
	
	//changes during transaction
	private HAPStoryResultTransaction m_changeResult;
	
	private int m_tempIndexAlias = 0;

	private Set<String> m_temporyAlias;
	
	public HAPStoryImp(HAPStoryManagerChange changeMan) {
		this.m_changeMan = changeMan;
		this.m_nodes = new LinkedHashMap<String, HAPStoryNode>();
		this.m_connections = new LinkedHashMap<String, HAPStoryConnection>();
		this.m_elementGroups = new LinkedHashMap<String, HAPStoryElementGroup>();
		this.m_changeHandlers = new HashSet<HAPStoryHandlerChange>();
		this.m_aliases = new LinkedHashMap<String, HAPStoryIdElement>();
		this.m_temporyAlias = new HashSet<String>();
	}

	@Override
	public void startTransaction() {
		this.m_changeResult = new HAPStoryResultTransaction();
		this.m_changeResult.setOldIdIndex(this.getIdIndex());
	}
	
	@Override
	public HAPStoryResultTransaction commitTransaction() {
		HAPStoryResultTransaction out = new HAPStoryResultTransaction();

		//change handlers process the change first
		List<HAPStoryChangeItem> changes = new ArrayList<HAPStoryChangeItem>();
		changes.addAll(this.m_changeResult.getChanges());
		for(HAPStoryHandlerChange handler : this.m_changeHandlers) {
			handler.onChanges(changes);
		}
		
		//add id index change first
		out.addChange(HAPStoryUtilityChange.newStoryIndexChange(this));

		//add all changes
		out.addChanges(this.m_changeResult.getChanges());

		this.removeTemporaryAlias();
		
		this.m_changeResult = null;
		return out;
	}
	
	@Override
	public void rollbackTransaction() {	
		this.m_changeMan.revertChange(this, this.m_changeResult.getChanges());	
		this.removeTemporaryAlias();
		this.setIdIndex(this.m_changeResult.getOldIdIndex());
		this.m_changeResult = null;
	}
	
	@Override
	public HAPStoryAliasElement generateTemporaryAlias() {	return new HAPStoryAliasElement("temporary_"+this.m_tempIndexAlias++);	}

	@Override
	public void registerChangeHandler(HAPStoryHandlerChange handler) {		this.m_changeHandlers.add(handler);	}
	
	@Override
	public void unregisterChangeHandler(HAPStoryHandlerChange handler) {		this.m_changeHandlers.remove(handler);	}
	
	@Override
	public List<HAPStoryChangeItem> change(HAPStoryRequestChange changeRequest) {
		List<HAPStoryChangeItem> out = new ArrayList<HAPStoryChangeItem>();
		if(changeRequest.isExtend()) {
			this.m_changeMan.applyChanges(this, changeRequest.getChanges(), out);
		}
		else {
			this.m_changeMan.applyChanges(this, changeRequest.getChanges());
			out.addAll(changeRequest.getChanges());
		}
		this.m_changeResult.getChanges().addAll(out);
		return out;
	}
	
	@Override
	public HAPStoryRequestChange newRequestChange(Boolean extend) {   return new HAPStoryRequestChange(extend, this);  }

	@Override
	public String getShowType() {   return this.m_showType;  }
	@Override
	public void setShowType(String showType) {  this.m_showType = showType;	}

	public void setTopicType(String topicType) {    this.m_showType = topicType;     }
	
	@Override
	public HAPStoryElement addElement(HAPStoryElement element, HAPStoryAliasElement alias) {
		HAPStoryElement out = null;
		if(HAPUtilityBasic.isStringEmpty(element.getId())) 	element.setId(HAPStoryUtilityStory.buildStoryElementId(element, this.getNextId()));
		element.appendToStory(this);
		
		String categary = element.getCategary();
		if(HAPConstantShared.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.addNode((HAPStoryNode)element);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.addConnection((HAPStoryConnection)element);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.addElementGroup((HAPStoryElementGroup)element);
		//set alias
		if(alias!=null) {
			this.m_aliases.put(alias.getName(), out.getElementId());
			if(alias.isTemporary())   this.m_temporyAlias.add(alias.getName());
		}
		return out;
	}
	
	@Override
	public HAPStoryIdElement getElementId(String alias) {	return this.m_aliases.get(alias);	}
	@Override
	public HAPStoryIdElement setAlias(HAPStoryAliasElement alias, HAPStoryIdElement eleId) {
		HAPStoryIdElement old = this.m_aliases.remove(alias.getName());
		if(eleId!=null)   this.m_aliases.put(alias.getName(), eleId);
		if(alias.isTemporary())  this.m_temporyAlias.add(alias.getName());
		return old;
	}

	public void deleteAlias(String alias) {   
		this.m_aliases.remove(alias);
		this.m_temporyAlias.remove(alias);
	}
	public void deleteAlias(HAPStoryIdElement eleId) {
		HAPStoryAliasElement alias = this.getAlias(eleId);
		if(alias!=null)  this.deleteAlias(alias.getName());
	}
	
	@Override
	public HAPStoryAliasElement getAlias(HAPStoryIdElement eleId) {
		for(String alias : this.m_aliases.keySet()) {
			if(eleId.equals(this.m_aliases.get(alias))) {
				boolean isTemp = this.m_temporyAlias.contains(alias); 
				return new HAPStoryAliasElement(alias, isTemp);
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement getElement(HAPStoryReferenceElement elementRef) {
		if(elementRef instanceof HAPStoryIdElement) {
			HAPStoryIdElement elementId = (HAPStoryIdElement)elementRef;
			return this.getElement(elementId.getCategary(), elementId.getId());    
		}
		else if(elementRef instanceof HAPStoryAliasElement) {
			HAPStoryAliasElement eleAlias = (HAPStoryAliasElement)elementRef;
			return this.getElement(eleAlias.getName());
		}
		return null;
	}

	@Override
	public HAPStoryElement getElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstantShared.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.getNode(id);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.getConnection(id);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.getElementGroup(id);
		return out;
	}

	@Override
	public HAPStoryElement getElement(String alias) {		return this.getElement(this.getElementId(alias));	}

	@Override
	public HAPStoryElement deleteElement(HAPStoryElement element) {  return this.deleteElement(element.getCategary(), element.getId());  }
	
	@Override
	public HAPStoryElement deleteElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstantShared.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.deleteNode(id);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.deleteConnection(id);
		else if(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.deleteElementGroup(id);
		return out;
	}

	@Override
	public HAPStoryElement deleteElement(HAPStoryIdElement eleId) {   return this.deleteElement(eleId.getCategary(), eleId.getId());  }

	public HAPStoryElement deleteNode(String id) {	return this.m_nodes.remove(id);	}
	public HAPStoryElement deleteConnection(String id) {	return this.m_connections.remove(id);	}
	public HAPStoryElement deleteElementGroup(String id) {	return this.m_elementGroups.remove(id);	}
	
	@Override
	public Set<HAPStoryNode> getNodes() {  return new HashSet<HAPStoryNode>(this.m_nodes.values());  } 

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_nodes.get(id);  }

	private HAPStoryNode addNode(HAPStoryNode node) {
		this.m_nodes.put(node.getId(), node);
		return node;
	}
	
	@Override
	public Set<HAPStoryConnection> getConnections(){ return new HashSet<HAPStoryConnection>(this.m_connections.values());  }

	@Override
	public HAPStoryConnection getConnection(String id) {  return this.m_connections.get(id);  }
	 
	private HAPStoryConnection addConnection(HAPStoryConnection connection) {
		//solidate end ref
		HAPStoryReferenceElement endRef1 = connection.getEnd1().getNodeRef();
		if(endRef1!=null) {
			HAPStoryIdElement endNodeId1 = HAPStoryUtilityStory.getElementIdByReference(endRef1, this);
			connection.getEnd1().setNodeRef(endNodeId1);
		}

		HAPStoryReferenceElement endRef2 = connection.getEnd2().getNodeRef();
		if(endRef2!=null) {
			HAPStoryIdElement endNodeId2 = HAPStoryUtilityStory.getElementIdByReference(endRef2, this);
			connection.getEnd2().setNodeRef(endNodeId2);
		}

		HAPStoryNode node1 = (HAPStoryNode)this.getElement(connection.getEnd1().getNodeElementId());
		HAPStoryNode node2 = (HAPStoryNode)this.getElement(connection.getEnd2().getNodeElementId());
		if(node1!=null && node2!=null) {
			node1.addConnection(connection.getId());
			node2.addConnection(connection.getId());
			this.m_connections.put(connection.getId(), connection);
			return connection;
		}
		else return null;
	}

	@Override
	public Set<HAPStoryElementGroup> getElementGroups() {  return new HashSet<HAPStoryElementGroup>(this.getElementGroups());  }

	@Override
	public HAPStoryElementGroup getElementGroup(String id) {   return this.m_elementGroups.get(id);  }

	@Override
	public String getNextId() {
		int index = this.getIdIndex();
		int out = index+1;
		this.setIdIndex(out);
		return out + "";
	}

	private int getIdIndex() {
		Integer index = (Integer)this.getInfoValue(HAPConstantShared.STORY_INFO_IDINDEX);
		if(index==null) {
			index = Integer.valueOf(0);
		}
		return index;
	}
	
	private void setIdIndex(int index) {
		this.getInfo().setValue(HAPConstantShared.STORY_INFO_IDINDEX, index);
	}
	
	private HAPStoryElementGroup addElementGroup(HAPStoryElementGroup connectionGroup) {
		//build solid element ref
		for(HAPStoryInfoElement ele : connectionGroup.getElements()) {
			ele.getElementId();
		}
		this.m_elementGroups.put(connectionGroup.getId(), connectionGroup);  
		return connectionGroup;
	}

	private void removeTemporaryAlias() {
		for(String alias : this.m_temporyAlias) {
			this.m_aliases.remove(alias);
		}
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPUtilityJson.buildJson(HAPUtilityBasic.toList(this.m_nodes), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPUtilityJson.buildJson(HAPUtilityBasic.toList(this.m_connections), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPUtilityJson.buildJson(HAPUtilityBasic.toList(this.m_elementGroups), HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPUtilityJson.buildJson(this.m_aliases, HAPSerializationFormat.JSON));
	}

	@Override
	public void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPUtilityJson.buildJson(this.m_nodes, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPUtilityJson.buildJson(this.m_connections, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPUtilityJson.buildJson(this.m_elementGroups, HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPUtilityJson.buildJson(this.m_aliases, HAPSerializationFormat.JSON));
	}

}
