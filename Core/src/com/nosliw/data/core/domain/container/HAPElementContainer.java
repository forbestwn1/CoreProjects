package com.nosliw.data.core.domain.container;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPEmbeded;

public abstract class HAPElementContainer<T extends HAPEmbeded> extends HAPSerializableImp{

	public static final String INFO = "info";

	public static final String ELEMENTID = "elementId";

	public static final String ENTITY = "entity";

	private String m_elementId;

	private HAPEntityInfo m_entityInfo;
	
	private T m_embededEntity;

	public HAPElementContainer(T embededEntity, String elementId) {
		this.m_embededEntity = embededEntity;
		this.m_elementId = elementId;
	}
	
	public HAPElementContainer() {	}
	
	public String getElementId() {  return this.m_elementId;  }

	public HAPEntityInfo getInfo() {    return this.m_entityInfo;    }
	public void setInfo(HAPEntityInfo entityInfo) {   this.m_entityInfo = entityInfo;    }
	
	public T getEmbededElementEntity() {   return this.m_embededEntity;   }
	public void setEmbededElementEntity(T entity) {    this.m_embededEntity = entity;   }

	public String getElementName() {    return this.getInfo().getName();    }

	public abstract HAPElementContainer cloneContainerElement();

	protected void cloneToInfoContainerElement(HAPElementContainer containerEleInfo) {
		containerEleInfo.m_embededEntity = this.m_embededEntity.cloneEmbeded();
		containerEleInfo.m_entityInfo = this.m_entityInfo.cloneEntityInfo();
		containerEleInfo.m_elementId = this.m_elementId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENTID, this.getElementId());
		jsonMap.put(INFO, this.m_entityInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ENTITY, this.m_embededEntity.toStringValue(HAPSerializationFormat.JSON));
	}
}
