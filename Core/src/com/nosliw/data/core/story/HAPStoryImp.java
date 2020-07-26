package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPStoryImp extends HAPEntityInfoImp implements HAPStory{
	
	private static final String INFO_IDINDEX = "story_idindex";

	private String m_showType;
	
	private Map<String, HAPStoryNode> m_nodes;
	
	private Map<String, HAPConnection> m_connections;

	private Map<String, HAPElementGroup> m_connectionGroups;

	public HAPStoryImp() {
		this.m_nodes = new LinkedHashMap<String, HAPStoryNode>();
		this.m_connections = new LinkedHashMap<String, HAPConnection>();
		this.m_connectionGroups = new LinkedHashMap<String, HAPElementGroup>();
	}
	
	@Override
	public String getShowType() {   return this.m_showType;  }
	@Override
	public void setShowType(String showType) {  this.m_showType = showType;	}

	public void setTopicType(String topicType) {    this.m_showType = topicType;     }

	@Override
	public HAPStoryElement addElement(HAPStoryElement element) {
		HAPStoryElement out = null;
		String categary = element.getCategary();
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.addNode((HAPStoryNode)element);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.addConnection((HAPConnection)element);
		else if(HAPConstant.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.addElementGroup((HAPElementGroup)element);
		return out;
	}

	@Override
	public HAPStoryElement getElement(HAPIdElement elementId) {   return this.getElement(elementId.getCategary(), elementId.getId());    }

	@Override
	public HAPStoryElement getElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.getNode(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.getConnection(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_GROUP.equals(categary)) out = this.getElementGroup(id);
		return out;
	}

	@Override
	public Set<HAPStoryNode> getNodes() {  return new HashSet<HAPStoryNode>(this.m_nodes.values());  } 

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_nodes.get(id);  }

	@Override
	public HAPStoryNode addNode(HAPStoryNode node) {
		if(HAPBasicUtility.isStringEmpty(node.getId())) 	node.setId(this.getNextId());
		this.m_nodes.put(node.getId(), node);
		return node;
	}
	
	@Override
	public Set<HAPConnection> getConnections(){ return new HashSet<HAPConnection>(this.m_connections.values());  }

	@Override
	public HAPConnection getConnection(String id) {  return this.m_connections.get(id);  }
	 
	@Override
	public HAPConnection addConnection(HAPConnection connection) {
		if(HAPBasicUtility.isStringEmpty(connection.getId())) 	connection.setId(this.getNextId());
		this.m_connections.put(connection.getId(), connection);
		this.getNode(connection.getEnd1().getNodeId()).addConnection(connection.getId());
		this.getNode(connection.getEnd2().getNodeId()).addConnection(connection.getId());
		return connection;
	}

	@Override
	public Set<HAPElementGroup> getElementGroups() {  return new HashSet<HAPElementGroup>(this.getElementGroups());  }

	@Override
	public HAPElementGroup getElementGroup(String id) {   return this.m_connectionGroups.get(id);  }

	@Override
	public HAPElementGroup addElementGroup(HAPElementGroup connectionGroup) {
		if(HAPBasicUtility.isStringEmpty(connectionGroup.getId())) 	connectionGroup.setId(this.getNextId());
		this.m_connectionGroups.put(connectionGroup.getId(), connectionGroup);  
		return connectionGroup;
	}

	private String getNextId() {
		Integer index = (Integer)this.getInfoValue(INFO_IDINDEX);
		if(index==null) {
			index = Integer.valueOf(0);
		}
		index++;
		this.getInfo().setValue(INFO_IDINDEX, index);
		return index + "";	
	}

	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_nodes), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connections), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connectionGroups), HAPSerializationFormat.JSON));
	}

	@Override
	public void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(this.m_nodes, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(this.m_connections, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(this.m_connectionGroups, HAPSerializationFormat.JSON));
	}

}
