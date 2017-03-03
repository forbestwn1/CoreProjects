package com.nosliw.data.core;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDataWrapperLiterate extends HAPDataImp{

	private String m_valueLiterate;
	
	public HAPDataWrapperLiterate(){}
	
	public HAPDataWrapperLiterate(String dataLiterate){
		this.buildObjectByLiterate(this.m_valueLiterate);
	}
	
	public HAPDataWrapperLiterate(HAPDataTypeId dataTypeId, Object value) {
		super(dataTypeId, value);
	}
	
	@Override
	Object buildObjectVale(Object value, HAPSerializationFormat format) {
		this.m_valueLiterate = (String)value;
		return this.m_valueLiterate;
	}

	@Override
	protected String toStringValueValue(HAPSerializationFormat format) {
		return this.m_valueLiterate;
	}
}
