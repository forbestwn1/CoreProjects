package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdOperation extends HAPResourceId{

	private HAPOperationId m_operationId;
	
	public HAPResourceIdOperation(String idLiterate) {
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION, idLiterate);
	}

	public HAPResourceIdOperation(HAPOperationId operationId){
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION, null);
		this.setOperationId(operationId);
	}
	
	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_operationId = new HAPOperationId(id);
	}

	public HAPOperationId getOperationId(){  return this.m_operationId;	}
	protected void setOperationId(HAPOperationId operationId){
		this.m_operationId = operationId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(operationId, HAPSerializationFormat.LITERATE); 
	}
}
