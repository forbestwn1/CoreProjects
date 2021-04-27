package com.nosliw.data.core.structure.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPContextDataGroup extends HAPSerializableImp implements HAPContextData{

	private Map<String, HAPContextDataFlat> m_data;
	
	public HAPContextDataGroup() {
		this.m_data = new LinkedHashMap<String, HAPContextDataFlat>();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String name : this.m_data.keySet()) {
			jsonMap.put(name, this.m_data.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
