package com.nosliw.core.application.division.story;

import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroup;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryHandlerChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChange;
import com.nosliw.core.application.division.story.change.HAPStoryResultTransaction;

@HAPEntityWithAttribute
public interface HAPStoryStory extends HAPEntityInfo{

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

	HAPStoryElement addElement(HAPStoryElement element, HAPStoryAliasElement alias);
	
	HAPStoryElement getElement(String categary, String id);
	HAPStoryElement getElement(HAPStoryReferenceElement elementRef);
	HAPStoryElement getElement(String alias);
	
	
	HAPStoryIdElement getElementId(String alias);
	HAPStoryIdElement setAlias(HAPStoryAliasElement alias, HAPStoryIdElement eleId);
	HAPStoryAliasElement getAlias(HAPStoryIdElement eleId);
	
	HAPStoryElement deleteElement(HAPStoryElement element);
	HAPStoryElement deleteElement(String categary, String id);
	HAPStoryElement deleteElement(HAPStoryIdElement eleId);
	
	Set<HAPStoryNode> getNodes();
	HAPStoryNode getNode(String id);
	
	Set<HAPStoryConnection> getConnections();
	HAPStoryConnection getConnection(String id);
	
	Set<HAPStoryElementGroup> getElementGroups();
	HAPStoryElementGroup getElementGroup(String id);
	
	void startTransaction();
	HAPStoryResultTransaction commitTransaction();
	void rollbackTransaction();
	HAPStoryAliasElement generateTemporaryAlias();
	void registerChangeHandler(HAPStoryHandlerChange handler);
	void unregisterChangeHandler(HAPStoryHandlerChange handler);
	HAPStoryRequestChange newRequestChange(Boolean extend);
	List<HAPStoryChangeItem> change(HAPStoryRequestChange changeRequest);
	
	String getNextId();
}
