package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithId;
import com.nosliw.data.core.domain.HAPExpandable;

public abstract class HAPInfoDefinitionContainerElement extends HAPInfoContainerElementImp<HAPEmbededWithId> implements HAPExpandable{

	public HAPInfoDefinitionContainerElement(HAPEmbededWithId embededEntity) {
		super(embededEntity);
	}
	
	public HAPInfoDefinitionContainerElement() {	}
	
	@Override
	public String getElementId() {
		return this.getEmbededElementEntity().getEntityId().toString();
	} 
	
//	@Override
//	protected boolean buildObjectByJson(Object json){  
//		JSONObject jsonObj = (JSONObject)json;
//		this.m_elementName = (String)jsonObj.opt(ELEMENTNAME);
//		this.m_embededEntity = HAPIdEntityInDomain.newInstance(jsonObj.opt(ENTITYID));
//		return true;
//	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.toExpanedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	protected void toExpanedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		jsonMap.put(ELEMENTNAME, this.getElementName());
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toExpandedJsonString(entityDomain));
	}
}
