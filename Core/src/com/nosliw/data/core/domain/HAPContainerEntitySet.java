package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntitySet implements HAPContainerEntity<HAPInfoContainerElementSet>{

	private Map<String, HAPInfoContainerElementSet> m_eleByName;
//	private Map<HAPIdEntityInDomain, HAPInfoContainerElementSet> m_eleById;
	
	public HAPContainerEntitySet() {
		this.m_eleByName = new LinkedHashMap<String, HAPInfoContainerElementSet>();
	}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_SET; }

	@Override
	public void addEntityElement(HAPInfoContainerElementSet eleInfo) {  	this.m_eleByName.put(eleInfo.getName(), eleInfo); 	}

	@Override
	public HAPInfoContainerElementSet getElementInfoByName(String name) {  return this.m_eleByName.get(name);  }

	@Override
	public HAPInfoContainerElementSet getElementInfoById(HAPIdEntityInDomain id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPIdEntityInDomain getElement(HAPInfoContainerElement eleInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPInfoContainerElementSet> getAllElementsInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPContainerEntity<HAPInfoContainerElementSet> cloneContainerEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
