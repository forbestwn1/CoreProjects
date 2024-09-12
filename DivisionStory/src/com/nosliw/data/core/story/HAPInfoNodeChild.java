package com.nosliw.data.core.story;

import com.nosliw.data.core.story.element.connection.HAPConnectionContain;

public class HAPInfoNodeChild {

	private HAPStoryNode m_childNode;
	
	private HAPConnectionContain m_connection;
	
	public HAPInfoNodeChild(HAPStoryNode childNode, HAPConnectionContain connection) {
		this.m_childNode = childNode;
		this.m_connection = connection;
	}
	
	public HAPStoryNode getChildNode() {   return this.m_childNode;     }
	
	public HAPConnectionContain getConnection() {   return this.m_connection;   }
	
	
}
