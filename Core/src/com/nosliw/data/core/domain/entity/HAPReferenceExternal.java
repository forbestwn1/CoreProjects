package com.nosliw.data.core.domain.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

@HAPEntityWithAttribute
public class HAPReferenceExternal extends HAPSerializableImp{

	@HAPAttribute
	public static String NORMALIZEDRESOURCEID = "resourceId";

	private HAPInfoResourceIdNormalize m_normalizedResourceId;
	
	public HAPReferenceExternal(HAPInfoResourceIdNormalize normalizedResourceId) {
		this.m_normalizedResourceId = normalizedResourceId;
	}
	
	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return this.m_normalizedResourceId;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NORMALIZEDRESOURCEID, this.m_normalizedResourceId.toStringValue(HAPSerializationFormat.JSON));
	}

}
