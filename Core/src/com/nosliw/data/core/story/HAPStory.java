package com.nosliw.data.core.story;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;

@HAPEntityWithAttribute
public interface HAPStory extends HAPEntityInfo{

	@HAPAttribute
	public static final String DIRECTOR = "director";

	@HAPAttribute
	public static final String SHOWTYPE = "showType";

	@HAPAttribute
	public static final String NODE = "node";

	@HAPAttribute
	public static final String CONNECTION = "connection";
	
	@HAPAttribute
	public static final String CONNECTIONGROUP = "connectionGroup";
	
	String getShowType();
	
	Set<HAPStoryNode> getNodes();
	HAPStoryNode getNode(String id);
	
	Set<HAPConnection> getConnections();
	HAPConnection getConnection(String id);
	
	Set<HAPConnectionGroup> getConnectionGroups();
	HAPConnectionGroup getConnectionGroup(String id);
	
}
