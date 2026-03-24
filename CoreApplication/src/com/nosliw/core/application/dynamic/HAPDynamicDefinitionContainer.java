package com.nosliw.core.application.dynamic;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionContainer extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPDynamicDefinitionItem> m_elements;

	public HAPDynamicDefinitionContainer() {
		this.m_elements = new LinkedHashMap<String, HAPDynamicDefinitionItem>();
	}
	
	public Set<HAPDynamicDefinitionItem> getItems(){
		return new HashSet(this.m_elements.values());
	}
	
	public void addElement(HAPDynamicDefinitionItem ele) {
		this.m_elements.put(ele.getName(), ele);
	}

	public HAPDynamicDefinitionItem getDescent(String path) {
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPDynamicDefinitionItem out = this.m_elements.get(complexPath.getRoot());
		
		for(String seg : complexPath.getPathSegs()) {
			out = out.getChild(seg);
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPManagerSerialize.getInstance().toStringValue(m_elements, HAPSerializationFormat.JSON));
	}
	
}
