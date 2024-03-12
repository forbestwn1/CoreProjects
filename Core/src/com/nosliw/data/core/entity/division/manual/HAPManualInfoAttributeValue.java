package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPIdEntityType;

public class HAPManualInfoAttributeValue extends HAPSerializableImp{

	public static final String VALUETYPE = "valueType";
	
	public static final String ENTITYTYPEID = "entityTypeId";
	
	private String m_valueType;
	
	private HAPIdEntityType m_entityTypeId;

	public HAPManualInfoAttributeValue(String valueType, HAPIdEntityType entityTypeId) {
		this.m_valueType = valueType;
		this.m_entityTypeId = entityTypeId;
	}
	
	public String getValueType() {   return this.m_valueType;  }
	
	public HAPIdEntityType getEntityTypeId() {   return this.m_entityTypeId;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, m_valueType);
		if(this.m_entityTypeId!=null) {
			jsonMap.put(ENTITYTYPEID, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
