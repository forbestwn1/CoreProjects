package com.nosliw.data.core.story;

import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPHandlerChange;
import com.nosliw.data.core.story.change.HAPRequestChange;
import com.nosliw.data.core.story.change.HAPResultTransaction;

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
	
	@HAPAttribute
	public static final String ALIAS = "alias";
	
	String getShowType();
	void setShowType(String showType);

	HAPStoryElement addElement(HAPStoryElement element, HAPAliasElement alias);
	
	HAPStoryElement getElement(String categary, String id);
	HAPStoryElement getElement(HAPReferenceElement elementRef);
	HAPStoryElement getElement(String alias);
	
	
	HAPIdElement getElementId(String alias);
	HAPIdElement setAlias(HAPAliasElement alias, HAPIdElement eleId);
	HAPAliasElement getAlias(HAPIdElement eleId);
	
	HAPStoryElement deleteElement(HAPStoryElement element);
	HAPStoryElement deleteElement(String categary, String id);
	HAPStoryElement deleteElement(HAPIdElement eleId);
	
	Set<HAPStoryNode> getNodes();
	HAPStoryNode getNode(String id);
	HAPStoryNode addNode(HAPStoryNode node);
	
	Set<HAPConnection> getConnections();
	HAPConnection getConnection(String id);
	HAPConnection addConnection(HAPConnection connection);
	
	Set<HAPElementGroup> getElementGroups();
	HAPElementGroup getElementGroup(String id);
	HAPElementGroup addElementGroup(HAPElementGroup connectionGroup);
	
	void startTransaction();
	HAPResultTransaction commitTransaction();
	void rollbackTransaction();
	HAPAliasElement generateTemporaryAlias();
	void registerChangeHandler(HAPHandlerChange handler);
	void unregisterChangeHandler(HAPHandlerChange handler);
	List<HAPChangeItem> change(HAPRequestChange changeRequest);
}
