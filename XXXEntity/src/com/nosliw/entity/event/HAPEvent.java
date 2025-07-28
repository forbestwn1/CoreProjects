package com.nosliw.entity.event;

import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public abstract class HAPEvent {

	public int m_eventType;
	
	public HAPEvent(){
	}

	public abstract int getType();
	
	static public HAPEntityOperationEvent createEntityOperationEvent(HAPEntityOperationInfo operation){
		HAPEntityOperationEvent event = new HAPEntityOperationEvent(operation);
		return event;
	}

	static public HAPEntityModifyEvent createEntityModifyEvent(HAPEntityWraper entityWraper, HAPEvent cEvent){
		HAPEntityModifyEvent event = new HAPEntityModifyEvent(entityWraper, cEvent);
		return event;
	}

	static public HAPEntityClearupEvent createEntityClearupEvent(HAPEntityWraper entityWraper){
		HAPEntityClearupEvent event = new HAPEntityClearupEvent(entityWraper);
		return event;
	}

	static public HAPEntityNewEvent createEntityNewEvent(HAPEntityWraper entityWraper){
		HAPEntityNewEvent event = new HAPEntityNewEvent(entityWraper);
		return event;
	}
	
}
