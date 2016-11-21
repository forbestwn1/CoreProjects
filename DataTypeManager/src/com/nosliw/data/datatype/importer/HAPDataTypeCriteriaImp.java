package com.nosliw.data.datatype.importer;

import com.nosliw.data.HAPDataTypeCriteria;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPDataTypeCriteriaImp extends HAPDataTypeInfoImp implements HAPDataTypeCriteria{

	@Override
	public boolean isValidDataType(HAPDataTypeInfo dataTypeInfo) {
		boolean out = true;
		if(!this.getName().equals(dataTypeInfo.getName())){
			out = false;
		}
		return out;
	}
}
