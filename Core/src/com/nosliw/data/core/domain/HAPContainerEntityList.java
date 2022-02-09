package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityList extends HashSet<HAPInfoContainerElementList> implements HAPContainerEntity<HAPInfoContainerElementList>{

	@Override
	public boolean add(HAPInfoContainerElementList arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_LIST; }

	@Override
	public void addEntityElement(HAPInfoContainerElementList eleInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPInfoContainerElementList getElementInfoByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPInfoContainerElementList getElementInfoById(HAPIdEntityInDomain id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPIdEntityInDomain getElement(HAPInfoContainerElement eleInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPInfoContainerElementList> getAllElementsInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPContainerEntity<HAPInfoContainerElementList> cloneContainerEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
