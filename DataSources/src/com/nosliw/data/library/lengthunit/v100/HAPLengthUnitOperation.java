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

	@HAPOperationInfoAnnotation(in = { "distance:simple", "lengthUnit:simple" }, out = "distance:simple", description = "Convert distance to specified unit")
	public HAPDistanceData convert(HAPData[] parms, HAPOperationContext opContext){
		HAPDistanceData distance = (HAPDistanceData)parms[0];
		HAPLengthUnitData unit = (HAPLengthUnitData)parms[1];
		return distance;
	}

	@HAPOperationInfoAnnotation(in = { "string:simple" }, out = "lengthUnit:simple", description = "new method")
	public HAPLengthUnitData newKm(HAPData[] parms, HAPOperationContext opContext){
		return ((HAPLengthUnit)this.getDataType()).newKm();
	}

}
