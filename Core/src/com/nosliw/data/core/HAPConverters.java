package com.nosliw.data.core;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPConverters extends HAPSerializableImp{

	private Map<HAPDataTypeId, HAPRelationship> m_convertors = new LinkedHashMap<HAPDataTypeId, HAPRelationship>();
	
	public HAPConverters(){
	}

	public void addItem(HAPDataTypeId dataTypeId, HAPRelationship relationship){
		this.m_convertors.put(dataTypeId, relationship);
	}

	public Set<HAPRelationship> getRelationships(){
		return new HashSet<HAPRelationship>(this.m_convertors.values());
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(HAPDataTypeId dataTypeId : this.m_convertors.keySet()){
			jsonMap.put(HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE), 
					HAPSerializeManager.getInstance().toStringValue(this.m_convertors.get(dataTypeId), HAPSerializationFormat.JSON));
		}
	}
}
