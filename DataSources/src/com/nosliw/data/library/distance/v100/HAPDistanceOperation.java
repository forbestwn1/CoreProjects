package com.nosliw.data.library.distance.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.basic.bool.HAPBooleanData;
import com.nosliw.data.basic.doubl.HAPDoubleData;
import com.nosliw.data.library.lengthunit.v100.HAPLengthUnitData;

public class HAPDistanceOperation extends HAPDataOperation{

	public HAPDistanceOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "distance:simple", "distance:simple" }, out = "boolean:simple", description = "Whether distance1 is longer than distance2")
	public HAPBooleanData longer(HAPData[] parms, HAPOperationContext opContext){
		HAPDistanceData dis1 = (HAPDistanceData)parms[0];
		HAPDistanceData dis2 = (HAPDistanceData)parms[1];
		
		HAPData[] convertParms = {dis1, dis2.getLengthUnit()};
		HAPDistanceData dis11 = (HAPDistanceData)this.dataOperate(dis1.getDataType(), "convert", convertParms, opContext).getData();
		double distance1 = dis11.getDistance();
		double distance2 = dis2.getDistance();
		
		if(distance1>distance2)  return HAPDataTypeManager.BOOLEAN.createDataByValue(true);
		else return HAPDataTypeManager.BOOLEAN.createDataByValue(false);
	}
	
	@HAPOperationInfoAnnotation(in = { "distance:simple", "lengthUnit:simple" }, out = "distance:simple", description = "Convert distance to specified unit")
	public HAPDistanceData convert(HAPData[] parms, HAPOperationContext opContext){
		HAPDistanceData distance = (HAPDistanceData)parms[0];
		HAPLengthUnitData unit = (HAPLengthUnitData)parms[1];
		return distance;
	}

	
	@HAPOperationInfoAnnotation(in = { "distance:simple" }, out = "float:simple", description = "Get distance")
	public HAPDoubleData getDistance(HAPData[] parms, HAPOperationContext opContext){
		HAPDistanceData parm1 = (HAPDistanceData)parms[0];
		return HAPDataTypeManager.DOUBLE.createDataByValue(parm1.getDistance());
	}
	
	@HAPOperationInfoAnnotation(in = { "double:simple" }, out = "distance:simple", description = "New distance")
	public HAPDistanceData newKm(HAPData[] parms, HAPOperationContext opContext){
		HAPDoubleData distanceData = (HAPDoubleData)parms[0];
		
		HAPDistance thisDataType = (HAPDistance)this.getDataType();
		return thisDataType.newDistanceKm(distanceData.getValue(), opContext);
	}
	
}
