package com.nosliw.data.library.distance.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.list.HAPListOperation;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPDataTypeOperationsAnnotation;
import com.nosliw.data1.HAPOperationContext;

public class HAPDistance extends HAPDataTypeImp{

	public HAPDistance(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
			HAPDataTypeInfoWithVersion parentDataTypeInfo, HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPDistanceOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	@Override
	public HAPData getDefaultData() {
		return null;
	}
	
	@Override
	public HAPServiceData validate(HAPData data) {
		return HAPServiceData.createSuccessData();
	}

	public HAPDistanceData newDistanceKm(double distance, HAPOperationContext opContext){
		HAPData lengthUnit = (HAPData)this.getDataTypeManager().getDataType(new HAPDataTypeInfo("simple", "lengthUnit")).newData("newKm", null, opContext).getData();
		return new HAPDistanceData(distance, lengthUnit, this);
	}
	
}
