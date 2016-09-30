package com.nosliw.data.library.lengthunit.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPLengthUnit extends HAPDataTypeImp{

	public HAPLengthUnit(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
			HAPDataTypeInfoWithVersion parentDataTypeInfo, HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	@Override
	public HAPData getDefaultData() {
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}
	
	public HAPLengthUnitData newKm(){
		return new HAPLengthUnitData("km", this);
	}
}
