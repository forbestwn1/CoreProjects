package com.nosliw.data.core.domain.container;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPEmbeded;

public abstract class HAPInfoContainerElementImp<T extends HAPEmbeded> extends HAPSerializableImp implements HAPInfoContainerElement<T>{

	private T m_embededEntity;

	private String m_elementName;
	
	public HAPInfoContainerElementImp(T embededEntity) {
		this.m_embededEntity = embededEntity;
	}
	
	public HAPInfoContainerElementImp() {	}
	
	@Override
	public T getEmbededElementEntity() {   return this.m_embededEntity;   }
	public void setEmbededElementEntity(T entity) {    this.m_embededEntity = entity;   }

	@Override
	public String getElementName() {    return this.m_elementName;    }
	public void setElementName(String elementName) {    this.m_elementName = elementName;    }
	
	protected void cloneToInfoContainerElement(HAPInfoContainerElementImp containerEleInfo) {
		containerEleInfo.m_embededEntity = this.m_embededEntity.cloneEmbeded();
		containerEleInfo.m_elementName = this.m_elementName;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_elementName = (String)jsonObj.opt(ELEMENTNAME);
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(ENTITY, this.m_embededEntity.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
