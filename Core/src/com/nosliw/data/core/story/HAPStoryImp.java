package com.nosliw.data.core.story;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPHandlerChange;
import com.nosliw.data.core.story.change.HAPRequestChange;
import com.nosliw.data.core.story.change.HAPResultTransaction;
import com.nosliw.data.core.story.change.HAPUtilityChange;

public class HAPStoryImp extends HAPEntityInfoImp implements HAPStory{
	
	private static final String INFO_IDINDEX = "story_idindex";

	private String m_showType;
	
	private Map<String, HAPStoryNode> m_nodes;
	
	private Map<String, HAPConnection> m_connections;

	private Map<String, HAPElementGroup> m_elementGroups;

	private Map<String, HAPIdElement> m_aliases;
	
	//handlers when transaction commit
	private Set<HAPHandlerChange> m_changeHandlers;
	
	//changes during transaction
	private HAPResultTransaction m_changeResult;
	
	private int m_tempIndexAlias = 0;

	private Set<String> m_temporyAlias;
	
	public HAPStoryImp() {
		this.m_nodes = new LinkedHashMap<String, HAPStoryNode>();
		this.m_connections = new LinkedHashMap<String, HAPConnection>();
		this.m_elementGroups = new LinkedHashMap<String, HAPElementGroup>();
		this.m_changeHandlers = new HashSet<HAPHandlerChange>();
		this.m_aliases = new LinkedHashMap<String, HAPIdElement>();
		this.m_temporyAlias = new HashSet<String>();
	}
	
	@Override
	public void startTransaction() {
		this.m_changeResult = new HAPResultTransaction();
	}
	
	@Override
	public HAPResultTransaction commitTransaction() {
		HAPResultTransaction out = new HAPResultTransaction();

		//change handlers process the change first
		List<HAPChangeItem> changes = new ArrayList<HAPChangeItem>();
		changes.addAll(this.m_changeResult.getChanges());
		for(HAPHandlerChange handler : this.m_changeHandlers) {
			handler.onChanges(changes);
		}
		
		//add all changes
		out.addChanges(this.m_changeResult.getChanges());

		this.removeTemporaryAlias();
		
		this.m_changeResult = null;
		return out;
	}
	
	@Override
	public void rollbackTransaction() {		
		HAPUtilityChange.revertChange(this, this.m_changeResult.getChanges());	
		this.removeTemporaryAlias();
	}
	
	@Override
	public HAPAliasElement generateTemporaryAlias() {	return new HAPAliasElement("temporary_"+this.m_tempIndexAlias++);	}

	@Override
	public void registerChangeHandler(HAPHandlerChange handler) {		this.m_changeHandlers.add(handler);	}
	
	@Override
	public void unregisterChangeHandler(HAPHandlerChange handler) {		this.m_changeHandlers.remove(handler);	}
	
	@Override
	public List<HAPChangeItem> change(HAPRequestChange changeRequest) {
		List<HAPChangeItem> out = new ArrayList<HAPChangeItem>();
		if(changeRequest.isExtend()) {
			HAPUtilityChange.applyChanges(this, changeRequest.getChanges(), out);
		}
		else {
			HAPUtilityChange.applyChanges(this, changeRequest.getChanges());
			out.addAll(changeRequest.getChanges());
		}
		this.m_changeResult.getChanges().addAll(out);
		return out;
	}
	
	@Override
	public String getShowType() {   return this.m_showType;  }
	@Override
	public void setShowType(String showType) {  this.m_showType = showType;	}

	public void setTopicType(String topicType) {    this.m_showType = topicType;     }
	
	@Override
	public HAPStoryElement addElement(HAPStoryElement element, HAPAliasElement alias) {
		HAPStoryElement out = null;
		String categary = element.getCategary();
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.addNode((HAPStoryNode)element);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.addConnection((HAPConnection)element);
		else if(HAPConstant.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.addElementGroup((HAPElementGroup)element);
		//set alias
		if(alias!=null) {
			this.m_aliases.put(alias.getAlias(), out.getElementId());
			if(alias.isTemporary())   this.m_temporyAlias.add(alias.getAlias());
		}
		return out;
	}
	
	@Override
	public HAPIdElement getElementId(String alias) {	return this.m_aliases.get(alias);	}
	@Override
	public HAPIdElement setAlias(HAPAliasElement alias, HAPIdElement eleId) {
		HAPIdElement old = this.m_aliases.remove(alias.getAlias());
		if(eleId!=null)   this.m_aliases.put(alias.getAlias(), eleId);
		if(alias.isTemporary())  this.m_temporyAlias.add(alias.getAlias());
		return old;
	}

	public void deleteAlias(String alias) {   
		this.m_aliases.remove(alias);
		this.m_temporyAlias.remove(alias);
	}
	public void deleteAlias(HAPIdElement eleId) {
		HAPAliasElement alias = this.getAlias(eleId);
		if(alias!=null)  this.deleteAlias(alias.getAlias());
	}
	
	@Override
	public HAPAliasElement getAlias(HAPIdElement eleId) {
		for(String alias : this.m_aliases.keySet()) {
			if(eleId.equals(this.m_aliases.get(alias))) {
				boolean isTemp = this.m_temporyAlias.contains(alias); 
				return new HAPAliasElement(alias, isTemp);
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement getElement(HAPReferenceElement elementRef) {
		if(elementRef instanceof HAPIdElement) {
			HAPIdElement elementId = (HAPIdElement)elementRef;
			return this.getElement(elementId.getCategary(), elementId.getId());    
		}
		else if(elementRef instanceof HAPAliasElement) {
			HAPAliasElement eleAlias = (HAPAliasElement)elementRef;
			return this.getElement(eleAlias.getAlias());
		}
		return null;
	}

	@Override
	public HAPStoryElement getElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.getNode(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.getConnection(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.getElementGroup(id);
		return out;
	}

	@Override
	public HAPStoryElement getElement(String alias) {		return this.getElement(this.getElementId(alias));	}

	@Override
	public HAPStoryElement deleteElement(HAPStoryElement element) {  return this.deleteElement(element.getCategary(), element.getId());  }
	
	@Override
	public HAPStoryElement deleteElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.deleteNode(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.deleteConnection(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.deleteElementGroup(id);
		
		return out;
	}
	
	public HAPStoryElement deleteNode(String id) {	return this.m_nodes.remove(id);	}
	public HAPStoryElement deleteConnection(String id) {	return this.m_connections.remove(id);	}
	public HAPStoryElement deleteElementGroup(String id) {	return this.m_elementGroups.remove(id);	}
	
	@Override
	public Set<HAPStoryNode> getNodes() {  return new HashSet<HAPStoryNode>(this.m_nodes.values());  } 

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_nodes.get(id);  }

	@Override
	public HAPStoryNode addNode(HAPStoryNode node) {
		if(HAPBasicUtility.isStringEmpty(node.getId())) 	node.setId(this.getNextId(node));
		this.m_nodes.put(node.getId(), node);
		return node;
	}
	
	@Override
	public Set<HAPConnection> getConnections(){ return new HashSet<HAPConnection>(this.m_connections.values());  }

	@Override
	public HAPConnection getConnection(String id) {  return this.m_connections.get(id);  }
	 
	@Override
	public HAPConnection addConnection(HAPConnection connection) {
		//assign id
		if(HAPBasicUtility.isStringEmpty(connection.getId())) 	connection.setId(this.getNextId(connection));

		//solidate end ref
		HAPReferenceElement endRef1 = connection.getEnd1().getNodeRef();
		if(endRef1!=null) {
			HAPIdElement endNodeId1 = HAPUtilityStory.getElementIdByReference(endRef1, this);
			connection.getEnd1().setNodeRef(endNodeId1);
		}

		HAPReferenceElement endRef2 = connection.getEnd2().getNodeRef();
		if(endRef2!=null) {
			HAPIdElement endNodeId2 = HAPUtilityStory.getElementIdByReference(endRef2, this);
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
	public Set<HAPElementGroup> getElementGroups() {  return new HashSet<HAPElementGroup>(this.getElementGroups());  }

	@Override
	public HAPElementGroup getElementGroup(String id) {   return this.m_elementGroups.get(id);  }

	@Override
	public HAPElementGroup addElementGroup(HAPElementGroup connectionGroup) {
		if(HAPBasicUtility.isStringEmpty(connectionGroup.getId())) 	connectionGroup.setId(this.getNextId(connectionGroup));
		//build solid element ref
		for(HAPInfoElement ele : connectionGroup.getElements()) {
			ele.getElementId();
		}
		this.m_elementGroups.put(connectionGroup.getId(), connectionGroup);  
		return connectionGroup;
	}

	private String getNextId(HAPStoryElement ele) {
		Integer index = (Integer)this.getInfoValue(INFO_IDINDEX);
		if(index==null) {
			index = Integer.valueOf(0);
		}
		index++;
		this.getInfo().setValue(INFO_IDINDEX, index);
		return HAPUtilityStory.buildStoryElementId(ele, index + "");	
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
		jsonMap.put(NODE, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_nodes), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connections), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_elementGroups), HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPJsonUtility.buildJson(this.m_aliases, HAPSerializationFormat.JSON));
	}

	@Override
	public void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(this.m_nodes, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(this.m_connections, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(this.m_elementGroups, HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPJsonUtility.buildJson(this.m_aliases, HAPSerializationFormat.JSON));
	}
}
