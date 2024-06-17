package com.nosliw.data.core.process1.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPSupplementResourceId;

public class HAPResourceIdProcessSuite  extends HAPResourceIdSimple{

	private HAPIdProcessSuite m_processSuiteId; 
	
	public HAPResourceIdProcessSuite(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE);  }

	public HAPResourceIdProcessSuite(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdProcessSuite(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdProcessSuite(HAPIdProcessSuite processSuiteId){
		this();
		init(null, null);
		this.m_processSuiteId = processSuiteId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(processSuiteId, HAPSerializationFormat.LITERATE); 
	}

	public HAPResourceIdProcessSuite(String id, HAPSupplementResourceId supplement){
		this();
		init(id, supplement);
	}
	
	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_processSuiteId = new HAPIdProcessSuite(id);
	}

	public HAPIdProcessSuite getProcessSuiteId(){  return this.m_processSuiteId;	}
	
	@Override
	public HAPResourceIdProcessSuite clone(){
		HAPResourceIdProcessSuite out = new HAPResourceIdProcessSuite();
		out.cloneFrom(this);
		return out;
	}
}
