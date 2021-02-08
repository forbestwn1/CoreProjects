package com.nosliw.common.strvalue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoMap;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPStringableValueMap<T> extends HAPStringableValueComplex{

	private Map<String, HAPStringableValue> m_elements;
	
	private boolean m_isAtomicChild = false;
	
	public HAPStringableValueMap(){
		this.m_elements = new LinkedHashMap<String, HAPStringableValue>();
	}

	public HAPStringableValueMap(HAPValueInfoMap mapValueInfo){
		super(mapValueInfo);
		this.m_elements = new LinkedHashMap<String, HAPStringableValue>();
	}
	
	public HAPValueInfoMap getValueInfoMap(){   return (HAPValueInfoMap)this.getValueInfo();  }

	/**
	 * Get map value. For atomic child, the value in return is the value in atomic stringable value
	 * @return
	 */
	public Map<String, T> getMapValue(){
		if(this.m_isAtomicChild){
			Map<String, T> out = new LinkedHashMap<String, T>();
			for(String key : this.m_elements.keySet()){
				out.put(key, (T)((HAPStringableValueAtomic)this.m_elements.get(key)).getValue());
			}
			return out;
		}
		else{
			return (Map<String, T>)this.m_elements;  
		}
	}
	
	public Set<T> getValues(){
		if(this.m_isAtomicChild){
			Set<T> out = new HashSet<T>();
			for(String key : this.m_elements.keySet()){
				out.add((T)((HAPStringableValueAtomic)this.m_elements.get(key)).getValue());
			}
			return out;
		}
		else{
			return (Set<T>)this.m_elements;  
		}
	}
	
	public HAPStringableValue updateChild(String name, HAPStringableValue child){
		if(child==null)  this.m_elements.remove(name);
		else{
			this.m_elements.put(name, child);
			if(child instanceof HAPStringableValueAtomic)   this.m_isAtomicChild = true;
		}
		return child;
	}
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_elements.values().iterator();	}
	
	@Override
	public String getStringableStructure(){		return HAPConstantShared.STRINGABLE_VALUESTRUCTURE_MAP;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPStringableValueComplex.ELEMENTS, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON_FULL));
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
		return m_elements.get(name);
	}

	public Set<String> getKeys(){  return this.m_elements.keySet(); }
	
	public void clear(){		this.m_elements.clear();	}
	
	@Override
	public HAPStringableValue cloneStringableValue() {
		HAPStringableValueMap<T> out = new HAPStringableValueMap<T>();
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPStringableValueMap<T> map){
		for(String name : map.m_elements.keySet()){
			this.m_elements.put(name, (HAPStringableValue)map.m_elements.get(name).clone());
		}
		this.m_isAtomicChild = map.m_isAtomicChild;
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
