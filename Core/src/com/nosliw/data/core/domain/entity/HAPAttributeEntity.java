package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;

@HAPEntityWithAttribute
public abstract class HAPAttributeEntity<T> extends HAPSerializableImp implements HAPExpandable{

	@HAPAttribute
	public final static String ENTITYTYPE = "entityType"; 

	@HAPAttribute
	public final static String NAME = "name"; 

	@HAPAttribute
	public final static String VALUE = "value"; 

	@HAPAttribute
	public final static String VALUETYPEINFO = "valueTypeInfo"; 

	//normal or container
	private String m_entityType;
	
	private String m_name;
	
	private T m_value;
	
	private HAPInfoValueType m_valueTypeInfo;
	
	public HAPAttributeEntity() {}

	public HAPAttributeEntity(String type) {
		this.m_entityType = type;
	}

	public HAPAttributeEntity(String entityType, String name, T value, HAPInfoValueType valueTypeInfo) {
		this.m_entityType = entityType;
		this.m_valueTypeInfo = valueTypeInfo;
		this.m_name = name;
		this.m_value = value;
	}
	
	public String getEntityType() {    return this.m_entityType;    }
	
	public String getName() {    return this.m_name;    }
	public void setName(String name) {    this.m_name = name;    }
	
	public T getValue() {    return this.m_value;    }
	public void setValue(T value) {   this.m_value = value;   }
	
	public HAPInfoValueType getValueTypeInfo() {     return this.m_valueTypeInfo;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		jsonMap.put(VALUETYPEINFO, HAPSerializeManager.getInstance().toStringValue(this.m_valueTypeInfo, HAPSerializationFormat.JSON));
		jsonMap.put(NAME, this.m_name);
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
		attr.m_entityType = this.m_entityType;
		attr.m_name = this.m_name;
	}
}
