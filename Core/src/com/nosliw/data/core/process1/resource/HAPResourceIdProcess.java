package com.nosliw.data.core.process1.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdProcess  extends HAPResourceIdSimple{

	private HAPIdProcess m_processId; 
	
	public HAPResourceIdProcess(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS);     }

	public HAPResourceIdProcess(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdProcess(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdProcess(HAPIdProcess processId){
		this();
		init(null, null);
		this.m_processId = processId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(processId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_processId = new HAPIdProcess(id);
	}

	public HAPIdProcess getProcessId(){  return this.m_processId;	}
	
	public HAPResourceIdProcessSuite getProcessSuiteResourceId() {
		HAPIdProcess processId = this.getProcessId();
		HAPResourceIdProcessSuite out = new HAPResourceIdProcessSuite(processId.getSuiteId(), this.getSupplement());
		return out;
	}
	
	@Override
	public HAPResourceIdProcess clone(){
		HAPResourceIdProcess out = new HAPResourceIdProcess();
		out.cloneFrom(this);
		return out;
	}

}
