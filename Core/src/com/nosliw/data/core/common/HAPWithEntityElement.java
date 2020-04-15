package com.nosliw.data.core.common;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;

public interface HAPWithEntityElement <T extends HAPEntityInfo> {

	@HAPAttribute
	public static String ELEMENT = "element";
	
	Set<T> getEntityElements();
	
	T getEntityElement(String id);
	
	void addEntityElement(T entityElement);
	
}
