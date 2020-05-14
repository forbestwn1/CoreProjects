package com.nosliw.data.core.story;

import java.util.Set;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPStory extends HAPEntityInfo{

	public static final String BUILDERID = "builderId";

	public static final String PARMDEF = "parmDef";

	public static final String NODE = "node";

	public static final String CONNECTION = "connection";
	
	public static final String CONNECTIONGROUP = "connectionGroup";
	
	Set<HAPStoryNode> getNodes();
	HAPStoryNode getNode(String id);
	
	Set<HAPConnection> getConnections();
	HAPConnection getConnection(String id);
	
	Set<HAPConnectionGroup> getConnectionGroups();
	HAPConnectionGroup getConnectionGroup(String id);
	
}
