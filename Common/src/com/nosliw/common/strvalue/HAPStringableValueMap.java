package com.nosliw.common.strvalue;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPStringableValueMap extends HAPStringableValueComplex{

	private Map<String, HAPStringableValue> m_elements;
	
	public HAPStringableValueMap(){
		this.m_elements = new LinkedHashMap<String, HAPStringableValue>();
	}
	
	public HAPStringableValue updateChild(String name, HAPStringableValue child){
		if(child==null)  this.m_elements.remove(name);
		else  this.m_elements.put(name, child);
		return child;
	}
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_elements.values().iterator();	}
	
	@Override
	public String getStringableCategary(){		return HAPConstant.STRINGABLE_VALUECATEGARY_MAP;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPAttributeConstant.STRINGABLEVALUE_ELEMENTS, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON_FULL));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		for(String child : this.m_elements.keySet()){
			jsonMap.put(child, this.m_elements.get(child).toStringValue(HAPSerializationFormat.JSON));
		}
	}


	@Override
	public HAPStringableValue getChild(String name) {
		return m_elements.get(Integer.valueOf(name));
	}

	public Set<String> getKeys(){  return this.m_elements.keySet(); }
	
	@Override
	public HAPStringableValue cloneStringableValue() {
		HAPStringableValueMap out = new HAPStringableValueMap();
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPStringableValueMap map){
		for(String name : map.m_elements.keySet()){
			this.m_elements.put(name, map.m_elements.get(name));
		}
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPStringableValueMap){
			HAPStringableValueMap value = (HAPStringableValueMap)obj;
			out = HAPBasicUtility.isEqualMaps(value.m_elements, value.m_elements);
		}
		return out;
	}
}
