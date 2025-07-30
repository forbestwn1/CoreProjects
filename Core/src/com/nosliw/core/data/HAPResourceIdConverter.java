package com.nosliw.core.data;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPResourceIdConverter extends HAPResourceIdSimple{

	private HAPDataTypeConverter m_dataTypeConverter;
	
	public HAPResourceIdConverter(){   super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER, "1.0.0");     }

	public HAPResourceIdConverter(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdConverter(String idLiterate) {
		this();
		this.init(idLiterate, null);
	}

	public HAPResourceIdConverter(HAPDataTypeConverter dataTypeConverter){
		this();
		this.init(null, null);
		this.setConverter(dataTypeConverter);
	}
	
	@Override
	public void setId(String id){
		super.setId(id);
		this.m_dataTypeConverter = new HAPDataTypeConverter(id);
	}

	public HAPDataTypeConverter getConverter(){  return this.m_dataTypeConverter;	}
	protected void setConverter(HAPDataTypeConverter converter){
		this.m_dataTypeConverter = converter;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(this.m_dataTypeConverter, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdConverter clone(){
		HAPResourceIdConverter out = new HAPResourceIdConverter();
		out.cloneFrom(this);
		return out;
	}

}
