package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;

@HAPEntityWithAttribute
public abstract class HAPAttributeEntity<T> extends HAPEntityInfoImp implements HAPExpandable{

	@HAPAttribute
	public final static String VALUE = "value"; 

	@HAPAttribute
	public final static String VALUETYPEINFO = "valueTypeInfo"; 

	private T m_value;
	
	private HAPInfoValueType m_valueTypeInfo;
	
	public HAPAttributeEntity() {}

	public HAPAttributeEntity(String name, T value, HAPInfoValueType valueTypeInfo) {
		this.m_valueTypeInfo = valueTypeInfo;
		this.setName(name);
		this.m_value = value;
	}
	
	public T getValue() {    return this.m_value;    }
	public void setValue(T value) {   this.m_value = value;   }
	
	public HAPInfoValueType getValueTypeInfo() {     return this.m_valueTypeInfo;     }
	
	
	public boolean isAttributeAutoProcess(boolean defaultValue) {
		Object value = this.getInfoValue(HAPConstantShared.ENTITYATTRIBUTE_INFO_AUTOPROCESS);
		if(value!=null)   return Boolean.valueOf((String)value);
		else return defaultValue;
	}
	
	public void setAttributeAutoProcess(boolean auto) {		this.getInfo().setValue(HAPConstantShared.ENTITYATTRIBUTE_INFO_AUTOPROCESS, auto+""); 	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPEINFO, HAPSerializeManager.getInstance().toStringValue(this.m_valueTypeInfo, HAPSerializationFormat.JSON));
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_value instanceof HAPExpandable) jsonMap.put(VALUE, ((HAPExpandable)this.m_value).toExpandedJsonString(entityDomain));
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}

	public abstract HAPAttributeEntity cloneEntityAttribute();
	
	protected void cloneToEntityAttribute(HAPAttributeEntity attr) {
		this.cloneToEntityInfo(attr);
		attr.m_valueTypeInfo = this.m_valueTypeInfo;
	}
}
