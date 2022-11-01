package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

@HAPEntityWithAttribute
public abstract class HAPAttributeEntity<T> extends HAPSerializableImp implements HAPExpandable{

	@HAPAttribute
	public final static String TYPE = "type"; 

	@HAPAttribute
	public final static String NAME = "name"; 

	@HAPAttribute
	public final static String VALUE = "value"; 

	private String m_type;
	
	private String m_name;
	
	private T m_value;
	
	public HAPAttributeEntity() {}

	public HAPAttributeEntity(String type) {
		this.m_type = type;
	}

	public HAPAttributeEntity(String type, String name, T value) {
		this.m_type = type;
		this.m_name = name;
		this.m_value = value;
	}
	
	public String getType() {    return this.m_type;    }
	
	public String getName() {    return this.m_name;    }
	public void setName(String name) {    this.m_name = name;    }
	
	public T getValue() {    return this.m_value;    }
	public void setValue(T value) {   this.m_value = value;   }
	
	public abstract boolean getIsComplex();
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_value instanceof HAPExpandable) jsonMap.put(VALUE, ((HAPExpandable)this.m_value).toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

	public abstract HAPAttributeEntity cloneEntityAttribute();
	
	protected void cloneToEntityAttribute(HAPAttributeEntity attr) {
		attr.m_type = this.m_type;
		attr.m_name = this.m_name;
	}
}
