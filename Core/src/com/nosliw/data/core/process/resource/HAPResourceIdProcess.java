package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPIdProcess;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdProcess  extends HAPResourceId{

	private HAPIdProcess m_processId; 
	
	public HAPResourceIdProcess(){}

	public HAPResourceIdProcess(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdProcess(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, idLiterate, null);
	}

	public HAPResourceIdProcess(HAPIdProcess processId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, null, null);
		this.m_processId = processId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(processId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_processId = new HAPIdProcess(id);
	}

	public HAPIdProcess getProcessId(){  return this.m_processId;	}
	
	@Override
	public HAPResourceIdProcess clone(){
		HAPResourceIdProcess out = new HAPResourceIdProcess();
		out.cloneFrom(this);
		return out;
	}

}
