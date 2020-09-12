package com.nosliw.data.core.story;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEventListener;

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
	public static final String ELEMENTGROUP = "elementGroup";
	
	String getShowType();
	void setShowType(String showType);

	HAPStoryElement addElement(HAPStoryElement element);
	HAPStoryElement getElement(String categary, String id);
	HAPStoryElement getElement(HAPIdElement elementId);
	
	HAPStoryElement deleteElement(HAPStoryElement element);
	HAPStoryElement deleteElement(String categary, String id);
	
	Set<HAPStoryNode> getNodes();
	HAPStoryNode getNode(String id);
	HAPStoryNode addNode(HAPStoryNode node);
	
	Set<HAPConnection> getConnections();
	HAPConnection getConnection(String id);
	HAPConnection addConnection(HAPConnection connection);
	
	Set<HAPElementGroup> getElementGroups();
	HAPElementGroup getElementGroup(String id);
	HAPElementGroup addElementGroup(HAPElementGroup connectionGroup);
	
	void registerListener(HAPEventListener listener);
	void trigueEvent(String eventName, Object eventData);
}
