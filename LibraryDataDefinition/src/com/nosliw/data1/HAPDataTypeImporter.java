package com.nosliw.data1;

import java.util.Set;

import com.nosliw.data.HAPDataType;

public interface HAPDataTypeImporter {

	public Set<HAPDataType> getAllDataType();
	
	public void init();
}
