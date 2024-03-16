package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPWrapperValueInAttributeReferenceResource extends HAPWrapperValueInAttribute{

	@HAPAttribute
	public static String NORMALIZEDRESOURCEID = "resourceId";

	private HAPInfoResourceIdNormalize m_normalizedResourceId;
	
	public HAPWrapperValueInAttributeReferenceResource(HAPInfoResourceIdNormalize normalizedResourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_normalizedResourceId = normalizedResourceId;
	}
	
	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return this.m_normalizedResourceId;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NORMALIZEDRESOURCEID, this.m_normalizedResourceId.toStringValue(HAPSerializationFormat.JSON));
	}
}
