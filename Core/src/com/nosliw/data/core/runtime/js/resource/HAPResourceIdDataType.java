package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdDataType extends HAPResourceIdSimple{

	private HAPDataTypeId m_dataTypeId;
	
	public HAPResourceIdDataType(){    super(HAPConstant.RUNTIME_RESOURCE_TYPE_DATATYPE);    }
	
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
	protected void setId(String id){
		super.setId(id);
		this.m_dataTypeId = new HAPDataTypeId(id);
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;	}
	protected void setDataTypeId(HAPDataTypeId dataTypeId){
		this.m_dataTypeId = dataTypeId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdDataType clone(){
		HAPResourceIdDataType out = new HAPResourceIdDataType();
		out.cloneFrom(this);
		return out;
	}
}
