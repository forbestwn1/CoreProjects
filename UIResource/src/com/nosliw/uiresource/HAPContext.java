package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPContext {

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	private Map<String, HAPContextNodeRoot> m_elements;
	
	public HAPContext(){
		this.m_elements = new LinkedHashMap<String, HAPContextNodeRoot>();
	}
	
	public void addElement(String name, HAPContextNodeRoot rootEle){
		this.m_elements.put(name, rootEle);
	}
	
}
