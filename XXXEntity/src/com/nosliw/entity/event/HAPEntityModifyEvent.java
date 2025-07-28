package com.nosliw.entity.event;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityWraper;

public class HAPEntityModifyEvent extends HAPEvent{

	HAPEvent m_childEvent;
	HAPEntityWraper m_entityWraper;
	
	public HAPEntityModifyEvent(HAPEntityWraper entityWraper, HAPEvent childEvent){
		this.m_entityWraper = entityWraper;
		this.m_childEvent = childEvent;
	}
	
	public HAPEntityWraper getEntityWraper(){
		return this.m_entityWraper;
	}
	
	public HAPEvent getChildEvent(){
		return this.m_childEvent;
	}
	
	@Override
	public int getType() {
		return HAPConstant.EVENTTYPE_ENTITY_MODIFY;
	}

}
