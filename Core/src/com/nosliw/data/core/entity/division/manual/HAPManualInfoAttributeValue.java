package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValue extends HAPSerializableImp{

	public static final String VALUETYPE = "valueType";
	
	public static final String ENTITYTYPE = "entityType";
	
	private String m_valueType;
	
	private HAPInfoEntityType m_entityTypeInfo;

	public HAPManualInfoAttributeValue(String valueType, HAPInfoEntityType entityTypeInfo) {
		this.m_valueType = valueType;
		this.m_entityTypeInfo = entityTypeInfo;
	}
	
	public String getValueType() {   return this.m_valueType;  }
	
	public HAPInfoEntityType getEntityTypeInfo() {   return this.m_entityTypeInfo;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, m_valueType);
		if(this.m_entityTypeInfo!=null) {
			jsonMap.put(ENTITYTYPE, this.m_entityTypeInfo.toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
