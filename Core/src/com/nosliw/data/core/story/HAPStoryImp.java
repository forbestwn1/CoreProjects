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
	
	private String m_showType;
	
	private Map<String, HAPStoryNode> m_nodes;
	
	private Map<String, HAPConnection> m_connections;

	private Map<String, HAPConnectionGroup> m_connectionGroups;

	public HAPStoryImp() {
		this.m_nodes = new LinkedHashMap<String, HAPStoryNode>();
		this.m_connections = new LinkedHashMap<String, HAPConnection>();
		this.m_connectionGroups = new LinkedHashMap<String, HAPConnectionGroup>();
	}
	
	@Override
	public String getShowType() {   return this.m_showType;  }
	@Override
	public void setShowType(String showType) {  this.m_showType = showType;	}

	public void setTopicType(String topicType) {    this.m_showType = topicType;     }

	@Override
	public HAPStoryElement getElement(String categary, String id) {
		HAPStoryElement out = null;
		if(HAPConstant.STORYELEMENT_CATEGARY_NODE.equals(categary)) out = this.getNode(id);
		else if(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION.equals(categary)) out = this.getConnection(id);
		return out;
	}

	@Override
	public Set<HAPStoryNode> getNodes() {  return new HashSet<HAPStoryNode>(this.m_nodes.values());  } 

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_nodes.get(id);  }

	@Override
	public void addNode(HAPStoryNode node) {   this.m_nodes.put(node.getId(), node);   }
	
	@Override
	public Set<HAPConnection> getConnections(){ return new HashSet<HAPConnection>(this.m_connections.values());  }

	@Override
	public HAPConnection getConnection(String id) {  return this.m_connections.get(id);  }
	
	public void addConnection(HAPConnection connection) {    this.m_connections.put(connection.getId(), connection);     }

	@Override
	public Set<HAPConnectionGroup> getConnectionGroups() {  return new HashSet<HAPConnectionGroup>(this.getConnectionGroups());  }

	@Override
	public HAPConnectionGroup getConnectionGroup(String id) {   return this.m_connectionGroups.get(id);  }

	public void addConnectionGroup(HAPConnectionGroup connectionGroup) {    this.m_connectionGroups.put(connectionGroup.getId(), connectionGroup);  }

	@Override
	public void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_nodes), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connections), HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTIONGROUP, HAPJsonUtility.buildJson(HAPBasicUtility.toList(this.m_connectionGroups), HAPSerializationFormat.JSON));
	}

	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHOWTYPE, this.m_showType);
		jsonMap.put(NODE, HAPJsonUtility.buildJson(this.m_nodes, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTION, HAPJsonUtility.buildJson(this.m_connections, HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTIONGROUP, HAPJsonUtility.buildJson(this.m_connectionGroups, HAPSerializationFormat.JSON));
	}
}
