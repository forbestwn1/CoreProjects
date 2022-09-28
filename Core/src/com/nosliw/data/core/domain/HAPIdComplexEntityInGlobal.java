package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

//id for entity globally (accross different complex resource)
@HAPEntityWithAttribute
public class HAPIdComplexEntityInGlobal extends HAPSerializableImp{

	@HAPAttribute
	public static String RESOURCEINFO = "resourceInfo";

	@HAPAttribute
	public static String ENTITYIDINDOMAIN = "entityIdInDomain";

	//resource info for this complex entity
	private HAPInfoResourceIdNormalize m_resourceInfo;
	
	//entity id within resource domain (calcuated)
	private HAPIdEntityInDomain m_entityIdInDomain;

	public HAPIdComplexEntityInGlobal(HAPInfoResourceIdNormalize resourceInfo, HAPIdEntityInDomain entityIdInDomain) {
		this.m_resourceInfo = resourceInfo;
		this.m_entityIdInDomain = entityIdInDomain;
	}
	
	public HAPIdEntityInDomain getEntityIdInDomain() {    return this.m_entityIdInDomain;    }
	public HAPInfoResourceIdNormalize getResourceInfo() {   return this.m_resourceInfo;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOURCEINFO, this.getResourceInfo().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ENTITYIDINDOMAIN, this.getEntityIdInDomain().toStringValue(HAPSerializationFormat.JSON));
	}

}
