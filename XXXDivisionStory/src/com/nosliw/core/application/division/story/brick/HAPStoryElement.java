package com.nosliw.core.application.division.story.brick;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryStatus;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;

@HAPEntityWithAttribute
public interface HAPStoryElement extends HAPEntityInfo, HAPEntityOrReference{

	@HAPAttribute
	public static final String CATEGARY = "categary";
	
	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String ENABLE = "enable";

	@HAPAttribute
	public static final String ELEMENTSTATUS = "eleStatus";

	@HAPAttribute
	public static final String DISPLAYRESOURCE = "displayResource";

	@HAPAttribute
	public static final String EXTRA = "extra";

	public static String[] getRootAttribute() {
		return new String[] {ENABLE};
	}
	
	HAPStoryIdElement getElementId();
	
	String getCategary();
	
	String getType();

	Object getExtra();
	
	HAPDisplayResourceNode getDisplayResource();
	
	HAPStoryChangeResult patch(String path, Object value);
	Object getValueByPath(String path);
	
	//configuration for element, for ui purpose
	HAPStoryStatus getElementStatus();

	boolean isEnable();

	void appendToStory(HAPStoryStory story);   

	HAPStoryElement cloneStoryElement();
}
