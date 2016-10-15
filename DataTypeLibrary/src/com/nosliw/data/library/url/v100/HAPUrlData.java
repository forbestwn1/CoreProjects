package com.nosliw.data.library.url.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.imp.HAPDataImp;

public class HAPUrlData extends HAPDataImp{
	private String m_value;
	
	HAPUrlData(String value, HAPDataType dataType) {
		super(dataType);
		this.m_value = value;
	}

	@Override
	public HAPData cloneData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		// TODO Auto-generated method stub
		return null;
	}

}
