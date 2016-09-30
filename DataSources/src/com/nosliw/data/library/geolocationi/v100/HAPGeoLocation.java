package com.nosliw.data.library.geolocationi.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPGeoLocation  extends HAPDataTypeImp{

	public HAPGeoLocation(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
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

}
