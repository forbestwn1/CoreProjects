package com.nosliw.data.core.domain;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPInfoContainerElement extends HAPSerializableImp{

	public static final String ELEMENTNAME = "eleName";
	
	public static final String ENTITYID = "entityId";
	
	private HAPIdEntityInDomain m_entityId;

	private String m_elementName;
	
	public HAPInfoContainerElement(HAPIdEntityInDomain entityId) {
		this.m_entityId = entityId;
	}
	
	public HAPInfoContainerElement() {	}
	
	public HAPIdEntityInDomain getElementEntityId() {   return this.m_entityId;   }
	public void setElementEntityId(HAPIdEntityInDomain entityId) {    this.m_entityId = entityId;   }

	public String getElementName() {    return this.m_elementName;    }
	public void setElementName(String elementName) {    this.m_elementName = elementName;    }
	
	public void cloneToInfoContainerElement(HAPInfoContainerElement containerEleInfo) {
		containerEleInfo.m_entityId = this.m_entityId;
		containerEleInfo.m_elementName = this.m_elementName;
	}
	
	public abstract HAPInfoContainerElement cloneContainerElementInfo();
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_elementName = (String)jsonObj.opt(ELEMENTNAME);
		this.m_entityId = HAPIdEntityInDomain.newInstance(jsonObj.opt(ENTITYID));
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
