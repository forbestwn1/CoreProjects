package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public class HAPMatchers extends HAPSerializableImp{

	private Map<HAPDataTypeId, HAPRelationship> m_machers = new LinkedHashMap<HAPDataTypeId, HAPRelationship>();
	
	public HAPMatchers(){
	}

	public void addItem(HAPDataTypeId dataTypeId, HAPRelationship relationship){
		this.m_machers.put(dataTypeId, relationship);
	}

	public Set<HAPRelationship> getRelationships(){
		return new HashSet<HAPRelationship>(this.m_machers.values());
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(HAPDataTypeId dataTypeId : this.m_machers.keySet()){
			jsonMap.put(HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE), 
					HAPSerializeManager.getInstance().toStringValue(this.m_machers.get(dataTypeId), HAPSerializationFormat.JSON));
		}
	}
}
