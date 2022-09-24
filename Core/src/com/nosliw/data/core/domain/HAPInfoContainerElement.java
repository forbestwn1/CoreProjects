package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPInfoContainerElement extends HAPSerializableImp implements HAPExpandable{

	public static final String ELEMENTNAME = "eleName";
	
	public static final String ENTITYID = "entityId";

	public static final String ENTITY = "entity";

	private HAPEmbeded m_embededEntity;

	private String m_elementName;
	
	public HAPInfoContainerElement(HAPEmbeded embededEntity) {
		this.m_embededEntity = embededEntity;
	}
	
	public HAPInfoContainerElement() {	}
	
	public HAPEmbeded getEmbededElementEntity() {   return this.m_embededEntity;   }
	public void setEmbededElementEntity(HAPEmbeded entity) {    this.m_embededEntity = entity;   }

	public String getElementId() {
		String out = null;
		if(this.m_embededEntity instanceof HAPEmbededWithId) {
			out = ((HAPEmbededWithId)this.m_embededEntity).getEntityId().toString();
		}
		return out;
	} 
	
	public String getElementName() {    return this.m_elementName;    }
	public void setElementName(String elementName) {    this.m_elementName = elementName;    }
	
	public void cloneToInfoContainerElement(HAPInfoContainerElement containerEleInfo) {
		containerEleInfo.m_embededEntity = this.m_embededEntity;
		containerEleInfo.m_elementName = this.m_elementName;
	}
	
	public abstract String getInfoType();
	public abstract HAPInfoContainerElement cloneContainerElementInfo();
	
//	@Override
//	protected boolean buildObjectByJson(Object json){  
//		JSONObject jsonObj = (JSONObject)json;
//		this.m_elementName = (String)jsonObj.opt(ELEMENTNAME);
//		this.m_embededEntity = HAPIdEntityInDomain.newInstance(jsonObj.opt(ENTITYID));
//		return true;
//	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(ENTITY, this.m_embededEntity.toStringValue(HAPSerializationFormat.LITERATE));
	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.toExpanedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	protected void toExpanedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		if(this.m_embededEntity instanceof HAPExpandable)	jsonMap.put(ENTITY, ((HAPExpandable)this.m_embededEntity).toExpandedJsonString(entityDomain));
		else jsonMap.put(ENTITY, this.m_embededEntity.toStringValue(HAPSerializationFormat.LITERATE));
	}
	
}
