package com.nosliw.entity.data;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data1.HAPDataTypeManager;

public class HAPWraperContainer  extends HAPDataTypeImp{

	public static HAPDataType dataType;
	
	public HAPWraperContainer(HAPDataTypeManager dataTypeMan) {
		super(new HAPDataTypeInfoWithVersion(HAPConstant.DATATYPE_CATEGARY_CONTAINER, HAPConstant.DATATYPE_TYPE_CONTAINER_WRAPPER), null, null, null, "", dataTypeMan);
	}

	@Override
	public HAPData getDefaultData() {
		return new HAPWraperContainerData(this);
	}

	@Override
	public HAPData toData(Object value, String format) {
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}

}
