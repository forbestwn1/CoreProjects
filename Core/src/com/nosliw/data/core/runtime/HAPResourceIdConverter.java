package com.nosliw.data.core.runtime;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdConverter extends HAPResourceIdSimple{

	private HAPDataTypeConverter m_dataTypeConverter;
	
	public HAPResourceIdConverter(){   super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER);     }

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
		this.m_id = HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeConverter, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdConverter clone(){
		HAPResourceIdConverter out = new HAPResourceIdConverter();
		out.cloneFrom(this);
		return out;
	}

}
