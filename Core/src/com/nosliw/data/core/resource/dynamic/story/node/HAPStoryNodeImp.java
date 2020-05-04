package com.nosliw.data.core.resource.dynamic.story.node;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.resource.dynamic.story.HAPConnection;
import com.nosliw.data.core.resource.dynamic.story.HAPStoryNode;

public abstract class HAPStoryNodeImp extends HAPEntityInfoImp implements HAPStoryNode{

	private String m_type;
	
	private List<HAPConnection> m_connections;
	
	private Object m_entity;
	
	public HAPStoryNodeImp(String type) {
		this.m_connections = new ArrayList<HAPConnection>();
		this.m_type = type;
	}

	@Override
	public String getType() {   return this.m_type;   }
	
	@Override
	public Object getEntity() {  return this.m_entity; }
	public void setEntity(Object entity) {   this.m_entity = entity;    }

	@Override
	public List<HAPConnection> getConnection() {  return this.m_connections;  }
	public void addConnection(HAPConnection connection) {   this.m_connections.add(connection);    }
}
