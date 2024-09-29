package com.nosliw.core.application.division.story.brick;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;

@HAPEntityWithAttribute
public interface HAPStoryElementGroup extends HAPStoryElement{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	@HAPAttribute
	public static final String ELEMENT = "element";
	
	List<HAPStoryInfoElement> getElements();
	
	HAPStoryInfoElement addElement(HAPStoryInfoElement eleId);
}
