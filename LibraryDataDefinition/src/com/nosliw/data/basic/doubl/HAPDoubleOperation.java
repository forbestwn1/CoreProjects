package com.nosliw.data.basic.doubl;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.bool.HAPBooleanData;
import com.nosliw.data1.HAPDataOperation;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperationContext;
import com.nosliw.data1.HAPOperationInfoAnnotation;

public class HAPDoubleOperation extends HAPDataOperation{

	public HAPDoubleOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "double:simple", "double:simple" }, out = "boolean:simple", description = "This is operation on Integer data type to compare two Integer")
	public HAPBooleanData largerThan(HAPData[] parms, HAPOperationContext opContext){
		HAPDoubleData data1 = (HAPDoubleData)parms[0];
		HAPDoubleData data2 = (HAPDoubleData)parms[1];
		boolean out = data1.getValue() >= data2.getValue();
		return HAPDataTypeManager.BOOLEAN.createDataByValue(out);
	}
	
}
