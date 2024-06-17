package com.nosliw.data.core.runtime;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPOperationId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdOperation extends HAPResourceIdSimple{

	private HAPOperationId m_operationId;
	
	public HAPResourceIdOperation(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION, "1.0.0");      }

	public HAPResourceIdOperation(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdOperation(String idLiterate) {
		this();
		this.init(idLiterate, null);
	}

	public HAPResourceIdOperation(HAPOperationId operationId){
		this();
		this.init(null, null);
		this.setOperationId(operationId);
	}
	
	@Override
	public void setId(String id){
		super.setId(id);
		this.m_operationId = new HAPOperationId(id);
	}

	public HAPOperationId getOperationId(){  return this.m_operationId;	}
	protected void setOperationId(HAPOperationId operationId){
		this.m_operationId = operationId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(operationId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdOperation clone(){
		HAPResourceIdOperation out = new HAPResourceIdOperation();
		out.cloneFrom(this);
		return out;
	}
}
