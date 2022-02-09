package com.nosliw.data.core.domain;

import java.util.Collection;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

//container for entity
public interface HAPContainerEntity<T extends HAPInfoContainerElement> extends Collection<T>{

	@HAPAttribute
	public static String ELEMENT = "element";

	String getContainerType();
	
//	HAPInfoContainerElement newElementInfoInstance();
	
	void addEntityElement(T eleInfo);

	T getElementInfoByName(String name);
	
	T getElementInfoById(HAPIdEntityInDomain id);
	
	HAPIdEntityInDomain getElement(HAPInfoContainerElement eleInfo);
	
	List<T> getAllElementsInfo();
	
	HAPContainerEntity<T> cloneContainerEntity();

}
