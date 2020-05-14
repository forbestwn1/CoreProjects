package com.nosliw.data.core.story.element.node;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.story.HAPConnection;
import com.nosliw.data.core.story.HAPStatus;
import com.nosliw.data.core.story.HAPStoryNode;

public abstract class HAPStoryNodeImp extends HAPEntityInfoImp implements HAPStoryNode{

	private String m_type;
	
	private Set<HAPConnection> m_connections;
	
	private Object m_entity;
	
	private HAPStatus m_configuration;
	
	public HAPStoryNodeImp(String type) {
		this.m_connections = new HashSet<HAPConnection>();
		this.m_type = type;
	}

	@Override
	public String getType() {   return this.m_type;   }
	
	@Override
	public Object getEntity() {  return this.m_entity; }
	public void setEntity(Object entity) {   this.m_entity = entity;    }

	@Override
	public Set<HAPConnection> getConnection() {  return this.m_connections;  }
	public void addConnection(HAPConnection connection) {   this.m_connections.add(connection);    }

	@Override
	public HAPStatus getStatus() {  return this.m_configuration; }

}
