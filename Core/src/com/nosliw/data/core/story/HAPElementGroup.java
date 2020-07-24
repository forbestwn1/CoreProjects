package com.nosliw.data.core.story;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPElementGroup extends HAPStoryElement{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	List<HAPInfoElement> getElements();
	
	void addElement(HAPInfoElement eleId);
}
