package com.nosliw.data.core.story.element.connectiongroup;

import java.util.List;
import java.util.Set;

import com.nosliw.data.core.story.HAPConnectionGroupImp;
import com.nosliw.data.core.story.HAPStatus;

public class HAPConnectionGroupSwitch extends HAPConnectionGroupImp{

	private Set<String> m_connections;
	
	private String m_endNode;
	
	String m_message;
	
	List<String> m_options;
	
	int m_current;

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getConnections() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
