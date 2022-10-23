package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPEmbededExecutable;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	
	private String m_entityType;
	
	private Map<String, HAPExecutable> m_attributes;

	public HAPExecutableEntity(String entityType) {
		this.m_entityType = entityType;
		this.m_attributes = new LinkedHashMap<String, HAPExecutable>();
	}
	
	public String getEntityType() {    return this.m_entityType;   }
	
	public Map<String, HAPExecutable> getAttributes(){    return this.m_attributes;     }
	
	public void setNormalAttribute(String attrName, HAPEmbededExecutable embeded) {	this.m_attributes.put(attrName, embeded);	}
	
	public void setContainerAttribute(String attribute, HAPContainerEntityExecutable entityContainer) {		this.m_attributes.put(attribute, entityContainer);	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		jsonMap.put(ENTITYTYPE, this.m_entityType);

		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> attrTypeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.buildAttributeResourceJsonMap(attrJsonMap, attrTypeJsonMap, runtimeInfo);
		jsonMap.put(ATTRIBUTE, HAPJsonUtility.buildMapJson(attrJsonMap, attrTypeJsonMap));
	}

	abstract protected void buildAttributeResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo);	
}
