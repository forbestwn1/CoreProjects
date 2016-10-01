package com.nosliw.data.library.geolocationi.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.info.HAPDataTypeInfo;

public class HAPGeoLocationOperation  extends HAPDataOperation{

	public HAPGeoLocationOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "geoLocation:simple", "geoLocation:simple" }, out = "distance:simple", description = "Calculate the distance between geolocation")
	public HAPData distance(HAPData[] parms, HAPOperationContext opContext){
		HAPGeoLocationData loc1 = (HAPGeoLocationData)parms[0];
		HAPGeoLocationData loc2 = (HAPGeoLocationData)parms[1];
		double distance = HAPGeoLocationUtility.distance(loc1.getLatitude(), loc1.getLontitude(), loc2.getLatitude(), loc2.getLontitude());
		
		HAPData[] ps = {HAPDataTypeManager.DOUBLE.createDataByValue(distance)};
		HAPData out = this.getDataTypeManager().newData(new HAPDataTypeInfo("simple", "distance"), "newKm", ps, opContext);
		return out;
	}

}
