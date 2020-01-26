package com.nosliw.data.core.event;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdEventTask   extends HAPResourceIdSimple{

	private HAPEventTaskId m_eventTaskId; 
	
	public HAPResourceIdEventTask(){   super(HAPConstant.RUNTIME_RESOURCE_TYPE_EVENTTASK);     }

	public HAPResourceIdEventTask(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdEventTask(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdEventTask(HAPEventTaskId uiResourceId){
		this();
		init(null, null);
		this.m_eventTaskId = uiResourceId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiResourceId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_eventTaskId = new HAPEventTaskId(id);
	}

	public HAPEventTaskId getUIResourceId(){  return this.m_eventTaskId;	}
	
	@Override
	public HAPResourceIdEventTask clone(){
		HAPResourceIdEventTask out = new HAPResourceIdEventTask();
		out.cloneFrom(this);
		return out;
	}
}