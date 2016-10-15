package com.nosliw.data.basic.bool;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.imp.HAPDataImp;

public class HAPBooleanData extends HAPDataImp{

	private boolean m_value;
	
	public HAPBooleanData(boolean value){
		super(HAPDataTypeManager.BOOLEAN);
		this.m_value = value;
	}
	
	@Override
	public String toDataStringValue(String format) {
		if(format.equals(HAPSerializationFormat.JSON)){
			return String.valueOf(this.m_value);
		}
		return null;
	}

	public boolean getValue(){ return this.m_value;}
	public void setValue(boolean value){this.m_value = value;}

	@Override
	public HAPData cloneData() {
		return new HAPBooleanData(this.m_value);
	}

}
