package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdDataType extends HAPResourceId{

	private HAPDataTypeId m_dataTypeId;
	
	public HAPResourceIdDataType(){}
	
	public HAPResourceIdDataType(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdDataType(String idLiterate) {
		this.init(HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPE, idLiterate);
	}

	public HAPResourceIdDataType(HAPDataTypeId dataTypeId, String alias){
		this.init(HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPE, null);
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

	public HAPResourceIdDataType clone(){
		HAPResourceIdDataType out = new HAPResourceIdDataType();
		out.cloneFrom(this);
		return out;
	}
}
