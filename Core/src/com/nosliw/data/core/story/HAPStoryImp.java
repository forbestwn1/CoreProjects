package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryImp extends HAPEntityInfoImp implements HAPStory{
	
	private String m_resourceType;
	
	private Map<String, HAPStoryNode> m_nodes;
	
	private Map<String, HAPConnection> m_connections;

	private Map<String, HAPConnectionGroup> m_connectionGroups;

	@Override
	public String getResourceType() {   return this.m_resourceType;  }

	@Override
	public Set<HAPStoryNode> getNodes() {  return new HashSet<HAPStoryNode>(this.m_nodes.values());  } 

	@Override
	public HAPStoryNode getNode(String id) {  return this.m_nodes.get(id);  }

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
	public String toStringValue(HAPSerializationFormat format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		// TODO Auto-generated method stub
		return false;
	}

}
