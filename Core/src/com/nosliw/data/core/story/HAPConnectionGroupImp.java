package com.nosliw.data.core.story;

import java.util.HashSet;
import java.util.Set;

public class HAPConnectionGroupImp extends HAPStoryElementImp implements HAPConnectionGroup{

	private Set<String> m_connections;

	public HAPConnectionGroupImp() {
		this.m_connections = new HashSet<String>();
	}
	
	@Override
	public Set<String> getConnections() {  return this.m_connections;  }

}
