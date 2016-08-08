package com.nosliw.data.basic.string;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataTypeManager;

public class HAPStringData extends HAPDataImp{

	private String m_value;
	
	public HAPStringData(String value) {
		super(HAPDataTypeManager.STRING);
		this.m_value = value;
	}

	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		String out = null;
		if(HAPConstant.CONS_SERIALIZATION_JSON.equals(format)){
			out = this.m_value;
		}
		return out;
	}

	public String getValue(){return this.m_value;}
	
	public String toString(){return this.m_value;}
}
