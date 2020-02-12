package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdProcess  extends HAPResourceIdSimple{

	private HAPProcessId m_processId; 
	
	public HAPResourceIdProcess(){    super(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS);     }

	public HAPResourceIdProcess(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdProcess(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdProcess(HAPProcessId processId){
		this();
		init(null, null);
		this.m_processId = processId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(processId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_processId = new HAPProcessId(id);
	}

	public HAPProcessId getProcessId(){  return this.m_processId;	}
	
	public HAPResourceIdProcessSuite getProcessSuiteResourceId() {
		HAPProcessId processId = this.getProcessId();
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
