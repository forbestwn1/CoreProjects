package com.nosliw.data.core.script.context.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;

public class HAPContextDataFlat extends HAPSerializableImp implements HAPContextData{

	private Map<String, HAPData> m_data;

	public HAPContextDataFlat() {
		this.m_data = new LinkedHashMap<String, HAPData>();
	}

	public HAPContextDataFlat(Map<String, HAPData> data) {
		this();
		this.m_data.putAll(data);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String name : this.m_data.keySet()) {
			jsonMap.put(name, this.m_data.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
	}

}
