package com.nosliw.data.library.lengthunit.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.library.distance.v100.HAPDistanceData;

public class HAPLengthUnitOperation extends HAPDataOperation{

	public HAPLengthUnitOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "string:simple" }, out = "lengthUnit:simple", description = "new method")
	public HAPLengthUnitData newKm(HAPData[] parms, HAPOperationContext opContext){
		return ((HAPLengthUnit)this.getDataType()).newKm();
	}

}
