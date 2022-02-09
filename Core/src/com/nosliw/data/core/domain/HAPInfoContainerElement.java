package com.nosliw.data.core.domain;

import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPInfoContainerElement extends HAPEntityInfoImp{

	private HAPIdEntityInDomain m_entityId;

	private String m_elementInfoType;
	
	public HAPInfoContainerElement(String eleInfoType, HAPIdEntityInDomain entityId) {
		this(eleInfoType);
		this.m_entityId = entityId;
	}
	
	public HAPInfoContainerElement(String eleInfoType) {
		this.m_elementInfoType = eleInfoType;
	}
	
	public String getInfoType() {  return this.m_elementInfoType;    }
	
	public HAPIdEntityInDomain getElementId() {   return this.m_entityId;   }

	public void setElementId(HAPIdEntityInDomain entityId) {   this.m_entityId = entityId;   }
	
	public void cloneToInfoContainerElement(HAPInfoContainerElement containerEleInfo) {
		this.cloneToEntityInfo(containerEleInfo);
		containerEleInfo.m_entityId = this.m_entityId;
	}
	
	public abstract HAPInfoContainerElement cloneContainerElementInfo();
	
}
