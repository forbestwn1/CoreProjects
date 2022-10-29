package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededExecutable;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp implements HAPExpandable{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	
	private String m_entityType;
	
	private Map<String, Object> m_attributes;

	public HAPExecutableEntity(String entityType) {
		this.m_entityType = entityType;
		this.m_attributes = new LinkedHashMap<String, Object>();
	} 
	
	public String getEntityType() {    return this.m_entityType;   }
	
	public Map<String, Object> getAttributes(){    return this.m_attributes;     }
	
	public void setAttribute(String attrName, Object attrObj) {    this.m_attributes.put(attrName, attrObj);    }
	
	public void setNormalAttribute(String attrName, HAPEmbededExecutable embeded) {	this.m_attributes.put(attrName, embeded);	}
	
	public void setContainerAttribute(String attribute, HAPContainerEntityExecutable entityContainer) {		this.m_attributes.put(attribute, entityContainer);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		buildCommonJsonMap(jsonMap, typeJsonMap);

		Map<String, String> attrsMapJson = new LinkedHashMap<String, String>();
		for(String attrName : this.m_attributes.keySet()) {
			attrsMapJson.put(attrName, HAPSerializeManager.getInstance().toStringValue(this.m_attributes.get(attrName), HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPJsonUtility.buildMapJson(attrsMapJson));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> attrTypeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		for(String attrName : this.m_attributes.keySet()) {
			Object attrObj = this.m_attributes.get(attrName);
			if(attrObj instanceof HAPExecutable)	attrJsonMap.put(attrName, ((HAPExecutable)attrObj).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPJsonUtility.buildMapJson(attrJsonMap, attrTypeJsonMap));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> attrTypeJsonMap = new LinkedHashMap<String, Class<?>>(); 

		for(String attrName : this.m_attributes.keySet()) {
			Object attrObj = this.m_attributes.get(attrName);
			if(attrObj instanceof HAPExpandable) attrJsonMap.put(attrName, ((HAPExpandable)attrObj).toExpandedJsonString(entityDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPJsonUtility.buildMapJson(attrJsonMap, attrTypeJsonMap));
	}

	protected void buildCommonJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		jsonMap.put(ENTITYTYPE, this.m_entityType);
	}
}
