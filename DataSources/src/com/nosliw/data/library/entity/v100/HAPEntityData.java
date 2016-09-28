package com.nosliw.data.library.entity.v100;

import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPEntityData extends HAPDataImp{

	Map<String, HAPData> m_attributes;
	
	public HAPEntityData(HAPDataType dataType) {
		super(dataType);
		// TODO Auto-generated constructor stub
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
