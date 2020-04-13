package com.nosliw.data.core.common;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;

public interface HAPWithEntityElement <T extends HAPEntityInfo> {

	@HAPAttribute
	public static String ELEMENT = "element";
	
	Map<String, T> getEntityElements();
	
	T getEntityElement(String name);
	
	void addEntityElement(T entityElement);
	
}
