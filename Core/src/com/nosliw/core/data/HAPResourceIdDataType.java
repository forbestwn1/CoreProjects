package com.nosliw.core.data;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPResourceIdDataType extends HAPResourceIdSimple{

	private HAPDataTypeId m_dataTypeId;
	
	public HAPResourceIdDataType(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATATYPE, "1.0.0");    }
	
	public HAPResourceIdDataType(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdDataType(String idLiterate) {
		this();
		this.init(idLiterate, null);
	}

	public HAPResourceIdDataType(HAPDataTypeId dataTypeId, String alias){
		this();
		this.init(null, null);
		this.setDataTypeId(dataTypeId);
	}

	@Override
	public void setId(String id){
		super.setId(id);
		this.m_dataTypeId = new HAPDataTypeId(id);
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;	}
	protected void setDataTypeId(HAPDataTypeId dataTypeId){
		this.m_dataTypeId = dataTypeId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdDataType clone(){
		HAPResourceIdDataType out = new HAPResourceIdDataType();
		out.cloneFrom(this);
		return out;
	}
}
