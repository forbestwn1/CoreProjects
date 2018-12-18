package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdProcess  extends HAPResourceId{

	private HAPProcessId m_processId; 
	
	public HAPResourceIdProcess(){}

	public HAPResourceIdProcess(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdProcess(String idLiterate) {
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, idLiterate);
	}

	public HAPResourceIdProcess(HAPProcessId processId){
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, null);
		this.m_processId = processId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(processId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_processId = new HAPProcessId(id);
	}

	public HAPProcessId getProcessId(){  return this.m_processId;	}
	
	public HAPResourceIdProcess clone(){
		HAPResourceIdProcess out = new HAPResourceIdProcess();
		out.cloneFrom(this);
		return out;
	}

}
