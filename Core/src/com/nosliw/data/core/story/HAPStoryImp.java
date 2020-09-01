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

	private Map<String, HAPElementGroup> m_elementGroups;

	public HAPStoryImp() {
		this.m_nodes = new LinkedHashMap<String, HAPStoryNode>();
		this.m_connections = new LinkedHashMap<String, HAPConnection>();
		this.m_elementGroups = new LinkedHashMap<String, HAPElementGroup>();
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
		if(HAPBasicUtility.isStringEmpty(connection.getId())) 	connection.setId(this.getNextId(connection));
		HAPStoryNode node1 = this.getNode(connection.getEnd1().getNodeId());
		HAPStoryNode node2 = this.getNode(connection.getEnd2().getNodeId());
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

	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_nodes), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connections), HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_elementGroups), HAPSerializationFormat.JSON));
	}

	@Override
	public void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(this.m_nodes, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(this.m_connections, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTGROUP, HAPJsonUtility.buildJson(this.m_elementGroups, HAPSerializationFormat.JSON));
	}

}
