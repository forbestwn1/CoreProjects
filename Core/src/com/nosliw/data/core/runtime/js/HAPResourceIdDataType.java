package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdDataType extends HAPResourceId{

	private HAPDataTypeId m_dataTypeId;
	
	public HAPResourceIdDataType(String idLiterate, String alias) {
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE, idLiterate, alias);
	}

	public HAPResourceIdDataType(HAPDataTypeId dataTypeId, String alias){
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE, null, alias);
		this.setDataTypeId(dataTypeId);
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_dataTypeId = new HAPDataTypeId(id);
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;	}
	protected void setDataTypeId(HAPDataTypeId dataTypeId){
		this.m_dataTypeId = dataTypeId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE); 
	}

}
