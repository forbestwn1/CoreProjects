package com.nosliw.data.core.domain.container;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbeded;
import com.nosliw.data.core.domain.HAPExpandable;

@HAPEntityWithAttribute
public abstract class HAPElementContainer<T extends HAPEmbeded> extends HAPSerializableImp  implements HAPExpandable{

	public static final String INFO = "info";

	public static final String ELEMENTID = "elementId";

	public static final String ENTITY = "entity";

	private HAPEntityInfo m_entityInfo;
	
	private String m_elementId;

	private T m_embededEntity;

	public HAPElementContainer(T embededEntity, String elementId) {
		this.m_embededEntity = embededEntity;
		this.m_elementId = elementId;
	}
	
	public HAPElementContainer() {	}
	
	public String getElementId() {  return this.m_elementId;  }
	public void setElementId(String eleId) {   this.m_elementId = eleId;    }

	public HAPEntityInfo getInfo() {    return this.m_entityInfo;    }
	public void setInfo(HAPEntityInfo entityInfo) {   this.m_entityInfo = entityInfo;    }
	
	public T getEmbededElementEntity() {   return this.m_embededEntity;   }
	public void setEmbededElementEntity(T entity) {    this.m_embededEntity = entity;   }

	public String getElementName() {    return this.getInfo().getName();    }

	public abstract HAPElementContainer cloneContainerElement();

	protected void cloneToContainerElement(HAPElementContainer containerEle) {
		containerEle.m_embededEntity = this.m_embededEntity.cloneEmbeded();
		containerEle.m_entityInfo = this.m_entityInfo.cloneEntityInfo();
		containerEle.m_elementId = this.m_elementId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INFO, this.m_entityInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTID, this.getElementId());
		jsonMap.put(ENTITY, this.m_embededEntity.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_embededEntity.toExpandedJsonString(entityDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
