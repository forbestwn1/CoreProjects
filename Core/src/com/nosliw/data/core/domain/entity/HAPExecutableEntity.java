package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp implements HAPExpandable{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	private String m_entityType;
	
	private List<HAPAttributeEntityExecutable> m_attributes;

	public HAPExecutableEntity(String entityType) {
		this.m_entityType = entityType;
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 
	
	public String getEntityType() {    return this.m_entityType;   }
	
	public List<HAPAttributeEntityExecutable> getAttributes(){    return this.m_attributes;     }
	
	public void setAttribute(HAPAttributeEntityExecutable attrObj) {    this.m_attributes.add(attrObj);    }
	
	public void setNormalAttribute(String attributeName, HAPEmbededExecutable embededEntity) {
		if(embededEntity!=null) {
			if(embededEntity instanceof HAPEmbededExecutableWithId) {
				this.setAttribute(new HAPAttributeEntityExecutableNormalId(attributeName, (HAPEmbededExecutableWithId)embededEntity));    
			}
			else if(embededEntity instanceof HAPEmbededExecutableWithValue) {
				this.setAttribute(new HAPAttributeEntityExecutableNormalValue(attributeName, (HAPEmbededExecutableWithValue)embededEntity));    
			}
			else if(embededEntity instanceof HAPEmbededExecutableWithEntity) {
				this.setAttribute(new HAPAttributeEntityExecutableNormalEntity(attributeName, (HAPEmbededExecutableWithEntity)embededEntity));    
			}
		}
	}

	public void setContainerAttribute(String attributeName, HAPContainerEntityExecutable container) {
		if(container!=null)		this.setAttribute(new HAPAttributeEntityExecutableContainer(attributeName, container));    
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toExpandedJsonString(entityDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	protected void buildCommonJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		jsonMap.put(ENTITYTYPE, this.m_entityType);
	}
}
