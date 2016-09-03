package com.nosliw.data.basic.floa;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataTypeManager;

public class HAPFloatData extends HAPDataImp{

	private float m_value;
	
	public HAPFloatData(float value) {
		super(HAPDataTypeManager.FLOAT);
		this.m_value = value;
	}

	@Override
	public HAPData cloneData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPConstant.SERIALIZATION_JSON)){
			return String.valueOf(this.m_value);
		}
		return null;
	}

	public float getValue(){ return this.m_value;}
}
