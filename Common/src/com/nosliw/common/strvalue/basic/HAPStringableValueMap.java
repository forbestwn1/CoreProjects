package com.nosliw.common.strvalue.basic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPStringableValueMap extends HAPStringableValueComplex{

	private Map<String, HAPStringableValue> m_elements;
	
	public HAPStringableValueMap(){
		this.m_elements = new LinkedHashMap<String, HAPStringableValue>();
	}
	
	public HAPStringableValue addElement(String name, HAPStringableValue element){
		this.m_elements.put(name, element);
		return element;
	}
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_elements.values().iterator();	}
	
	@Override
	public String getStringableCategary(){		return HAPConstant.CONS_STRINGABLE_VALUECATEGARY_MAP;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put("elements", HAPJsonUtility.getMapObjectJson(this.m_elements));
	}

	@Override
	public HAPStringableValue getChild(String name) {
		return m_elements.get(Integer.valueOf(name));
	}

	@Override
	public HAPStringableValue clone() {
		HAPStringableValueMap out = new HAPStringableValueMap();
		out.cloneFrom(this);
		return out;
	}

	public void cloneFrom(HAPStringableValueMap map){
		for(String name : map.m_elements.keySet()){
			this.m_elements.put(name, map.m_elements.get(name));
		}
	}
}
