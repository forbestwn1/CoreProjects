package com.nosliw.core.application.division.story.xxx.brick;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;

@HAPEntityWithAttribute
public interface HAPStoryElementGroup extends HAPStoryElement{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	@HAPAttribute
	public static final String ELEMENT = "element";
	
	List<HAPStoryInfoElement> getElements();
	
	HAPStoryInfoElement addElement(HAPStoryInfoElement eleId);
}
