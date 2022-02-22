package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoContainerElementList extends HAPInfoContainerElement{

	private int m_index;
	
	public HAPInfoContainerElementList(HAPIdEntityInDomain entityId) {
		super(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST, entityId);
	}

	public HAPInfoContainerElementList() {
		super(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST);
	}
	
	public HAPInfoContainerElementList(int index) {
		this();
		this.m_index = index;
	}

	public int getIndex() {   return this.m_index;    }
	
	@Override
	public HAPInfoContainerElement cloneContainerElementInfo() {
		HAPInfoContainerElementList out = new HAPInfoContainerElementList();
		this.cloneToInfoContainerElement(out);
		out.m_index = this.m_index;
		return out;
	}

}
