package com.nosliw.data.library.distance.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPDistance extends HAPDataTypeImp{

	protected HAPDistance(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
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

	public HAPDistanceData newDistanceKm(double distance){
		HAPData lengthUnit = (HAPData)this.getDataTypeManager().getDataType(new HAPDataTypeInfo("simple", "lengthUnit")).newData("newKm", null).getData();
		return new HAPDistanceData(distance, lengthUnit, this);
	}
	
}
