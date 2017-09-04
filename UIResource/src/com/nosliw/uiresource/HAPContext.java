package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPContext {

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	private Map<String, HAPContextElement> m_elements;
	
	public HAPContext(){
		this.m_elements = new LinkedHashMap<String, HAPContextElement>();
	}
	
	public void addElement(String name, HAPContextElement element){
		this.m_elements.put(name, element);
	}
	
}
