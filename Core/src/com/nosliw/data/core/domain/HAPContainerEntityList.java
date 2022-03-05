package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityList extends HAPContainerEntityImp<HAPInfoContainerElementList>{

	private ArrayList<HAPInfoContainerElementList> m_eleArray;
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_LIST; }

	@Override
	public void addEntityElement(HAPInfoContainerElementList eleInfo) {
		super.addEntityElement(eleInfo);
		int index = eleInfo.getIndex();
		if(index==-1) {
			eleInfo.setIndex(this.m_eleArray.size());
			this.m_eleArray.add(eleInfo);
		}
		else {
			this.m_eleArray.add(eleInfo.getIndex(), eleInfo);
		}
	}

	@Override
	public List<HAPInfoContainerElementList> getAllElementsInfo() {  return this.m_eleArray;  }

	@Override
	public HAPContainerEntity<HAPInfoContainerElementList> cloneContainerEntity() {
		HAPContainerEntityList out = new HAPContainerEntityList();
		this.cloneToContainer(out);
		return out;
	}
}
