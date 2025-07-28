package com.nosliw.data.basic.number;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.imp.HAPDataImp;
import com.nosliw.data1.HAPDataTypeManager;

public class HAPIntegerData extends HAPDataImp{

	private int m_value;
	
	HAPIntegerData(int value){
		super(HAPDataTypeManager.INTEGER);
		this.m_value = value;
	}

	@Override
	public HAPData cloneData() {
		return new HAPIntegerData(this.m_value);
	}

	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPSerializationFormat.JSON)){
			return String.valueOf(this.m_value);
		}
		return null;
	}
	
	public int getValue(){ return this.m_value;}
	public void setValue(int value){this.m_value = value;}
	
}
