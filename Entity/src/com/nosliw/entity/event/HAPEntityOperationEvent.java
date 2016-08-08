package com.nosliw.entity.event;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPEntityOperationEvent extends HAPEvent{

	private HAPEntityOperationInfo m_operation;
	
	public HAPEntityOperationEvent(HAPEntityOperationInfo operation){
		this.m_operation = operation;
	}
	
	public HAPEntityOperationInfo getOperation(){
		return this.m_operation;
	}
	
	@Override
	public int getType() {
		return HAPConstant.CONS_EVENTTYPE_ENTITY_OPERATION;
	}
	
}
