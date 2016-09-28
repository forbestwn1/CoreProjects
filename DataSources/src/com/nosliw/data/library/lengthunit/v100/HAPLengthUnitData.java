package com.nosliw.data.library.lengthunit.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPLengthUnitData extends HAPDataImp{

	private String m_unit;
	
	public HAPLengthUnitData(String unit, HAPDataType dataType) {
		super(dataType);
		this.m_unit = unit;
	}

	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		return null;
	}

	
	
}
