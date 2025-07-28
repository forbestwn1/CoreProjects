package com.nosliw.entity.event;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityWraper;

public class HAPEntityClearupEvent extends HAPEvent{

	private HAPEntityWraper m_entityWraper;
	
	public HAPEntityClearupEvent(HAPEntityWraper entityWraper){
		this.m_entityWraper = entityWraper;
	}
	
	public HAPEntityWraper getEntityWraper(){
		return this.m_entityWraper;
	}
	
	@Override
	public int getType() {
		return HAPConstant.EVENTTYPE_ENTITY_CLEARUP;
	}

}
