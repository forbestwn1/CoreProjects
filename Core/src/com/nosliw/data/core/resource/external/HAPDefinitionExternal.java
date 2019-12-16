package com.nosliw.data.core.resource.external;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

//define what external resource depend on
@HAPEntityWithAttribute
public class HAPDefinitionExternal extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, Map<String, HAPDefinitionExternalEle>> m_element;
	
	public HAPDefinitionExternal() {
		this.m_element = new LinkedHashMap<>();
	}
	
	public void addElement(String type, HAPDefinitionExternalEle ele) {
		Map<String, HAPDefinitionExternalEle> byName = this.m_element.get(type);
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPDefinitionExternalEle>();
			this.m_element.put(type, byName);
		}
		byName.put(ele.getName(), ele);
	}

	
	
}
