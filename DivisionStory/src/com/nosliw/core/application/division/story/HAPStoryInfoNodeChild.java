package com.nosliw.core.application.division.story;

import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionContain;

public class HAPStoryInfoNodeChild {

	private HAPStoryNode m_childNode;
	
	private HAPStoryConnectionContain m_connection;
	
	public HAPStoryInfoNodeChild(HAPStoryNode childNode, HAPStoryConnectionContain connection) {
		this.m_childNode = childNode;
		this.m_connection = connection;
	}
	
	public HAPStoryNode getChildNode() {   return this.m_childNode;     }
	
	public HAPStoryConnectionContain getConnection() {   return this.m_connection;   }
	
	
}
