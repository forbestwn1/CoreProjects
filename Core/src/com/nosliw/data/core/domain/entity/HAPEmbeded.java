package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.value.HAPUtilityClone;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPUtilityDomain;

@HAPEntityWithAttribute
public abstract class HAPEmbeded extends HAPSerializableImp implements HAPExpandable{

	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String VALUE = "value";

	@HAPAttribute
	public static String VALUEEXPANDED = "valueExpanded";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	private Object m_value;
	
	private Object m_adapter;
	
	public HAPEmbeded() {}
	
	public HAPEmbeded(Object value, Object adapter) {
		this.m_value = value;
		this.m_adapter = adapter;
	}
	
	public Object getValue() {   return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public Object getAdapter() {	return m_adapter;	}
	public void setAdapter(Object adapter) {	this.m_adapter = adapter;	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		HAPUtilityDomain.buildExpandedJsonMap(this.getValue(), VALUEEXPANDED, jsonMap, entityDomain);
		HAPUtilityDomain.buildExpandedJsonMap(this.getAdapter(), ADAPTER, jsonMap, entityDomain);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		if(this.getAdapter()!=null) {
			jsonMap.put(ADAPTER, HAPSerializeManager.getInstance().toStringValue(this.getAdapter(), HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
	
	public abstract HAPEmbeded cloneEmbeded();

	protected void cloneToEmbeded(HAPEmbeded embeded) {
		embeded.setAdapter(HAPUtilityClone.cloneValue(this.getAdapter()));
		embeded.setValue(HAPUtilityClone.cloneValue(this.getValue()));	
	}
}
