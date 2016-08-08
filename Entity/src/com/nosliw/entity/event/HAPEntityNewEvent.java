package com.nosliw.entity.event;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityWraper;

public class HAPEntityNewEvent extends HAPEvent{

	private HAPEntityWraper m_entity;
	
	public HAPEntityNewEvent(HAPEntityWraper entity) {
		super();
		this.m_entity = entity;
	}
	
	public HAPEntityWraper getEntityWraper(){
		return this.m_entity;
	}

	@Override
	public int getType() {
		return HAPConstant.CONS_EVENTTYPE_ENTITY_NEW;
	}
	
}
