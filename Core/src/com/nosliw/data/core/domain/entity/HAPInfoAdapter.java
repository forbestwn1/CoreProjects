package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPUtilityDomain;

@HAPEntityWithAttribute
public abstract class HAPInfoAdapter extends HAPEntityInfoImp implements HAPExpandable{

	@HAPAttribute
	public static String VALUETYPE = "valueType";

	@HAPAttribute
	public static String VALUE = "value";

	private String m_valueType;
	
	private Object m_value;
	
	public HAPInfoAdapter(String valueType, Object value) {
		this.m_valueType = valueType;
		this.m_value = value;
	}
	
	public String getValueType() {    return this.m_valueType;     }
	
	public Object getValue() {    return this.m_value;     }

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		HAPUtilityDomain.buildExpandedJsonMap(this.getValue(), VALUE, jsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.m_valueType);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
}
