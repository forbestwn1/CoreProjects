package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public abstract class HAPEmbeded extends HAPSerializableImp implements HAPExpandable{

	@HAPAttribute
	public static String EMBEDED = "embeded";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	private Object m_value;
	
	private Object m_adapter;
	
	public HAPEmbeded() {}
	
	public HAPEmbeded(Object value) {
		this.m_value = value;
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
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	protected abstract void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain);	

	public abstract HAPEmbeded cloneEmbeded();
}
