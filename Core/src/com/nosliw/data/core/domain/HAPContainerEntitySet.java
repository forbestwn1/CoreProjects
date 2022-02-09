package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntitySet extends HashSet<HAPInfoContainerElementSet> implements HAPContainerEntity<HAPInfoContainerElementSet>{

	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_SET; }

	@Override
	public void addEntityElement(HAPInfoContainerElementSet eleInfo) {  	this.add(eleInfo);  	}

	@Override
	public HAPInfoContainerElementSet getElementInfoByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

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
