package com.nosliw.data.library.list.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPList extends HAPDataTypeImp{

	protected HAPList(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
			HAPDataTypeInfoWithVersion parentDataTypeInfo, HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HAPData getDefaultData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		// TODO Auto-generated method stub
		return null;
	}

}
