package com.nosliw.data.core.domain.entity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
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
	public static String VALUETYPE = "valueType";

	@HAPAttribute
	public static String VALUEEXPANDED = "valueExpanded";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	private Object m_value;
	
	private String m_valueType;
	
	//multiple adapters by name
	private Map<String, HAPInfoAdapter> m_adapters;
	
	public HAPEmbeded() {
		this.m_adapters = new LinkedHashMap<String, HAPInfoAdapter>();
	}
	
	public HAPEmbeded(Object value) {
		this();
		this.m_value = value;
	}
	
	public HAPEmbeded(Object value, String valueType) {
		this(value);
		this.m_valueType = valueType;
	}
	
	public Object getValue() {   return this.m_value;   }
	public void setValue(Object value) {    this.m_value = value;    }
	
	public String getValueType() {   return this.m_valueType;    }
	
	public Set<HAPInfoAdapter> getAdapters(){   return new HashSet(this.m_adapters.values());     }
	public HAPInfoAdapter getAdapter(String name) {	return m_adapters.get(name);	}
	public void addAdapter(HAPInfoAdapter adapter) {
		String name = adapter.getName();
		if(HAPUtilityBasic.isStringEmpty(name))  name = HAPConstantShared.NAME_DEFAULT;
		this.m_adapters.put(name, adapter);	
	}

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
		
		Map<String, String> adaptersJson = new LinkedHashMap<String, String>();
		for(String name : this.m_adapters.keySet()) {
			adaptersJson.put(name, m_adapters.get(name).toExpandedJsonString(entityDomain));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildMapJson(adaptersJson));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ADAPTER, HAPSerializeManager.getInstance().toStringValue(this.getAdapters(), HAPSerializationFormat.JSON));
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
		jsonMap.put(VALUETYPE, this.m_valueType);
	}
	
	public abstract HAPEmbeded cloneEmbeded();

	protected void cloneToEmbeded(HAPEmbeded embeded) {
		embeded.setValue(HAPUtilityClone.cloneValue(this.getValue()));	
		
		for(HAPInfoAdapter adapterInfo : this.m_adapters.values()) {
			embeded.addAdapter((HAPInfoAdapter)HAPUtilityClone.cloneValue(adapterInfo));
		}
	}
}
