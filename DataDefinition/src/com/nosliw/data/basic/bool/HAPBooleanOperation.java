package com.nosliw.data.basic.bool;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;

public class HAPBooleanOperation extends HAPDataOperation{

	public HAPBooleanOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "boolean:simple" }, out = "boolean:simple", description = "This is operation on Integer data type to compare two Integer")
	public HAPBooleanData not(HAPData[] parms, HAPOperationContext opContext){
		HAPBooleanData booleanData = (HAPBooleanData)parms[0];
		return HAPDataTypeManager.BOOLEAN.createDataByValue(!booleanData.getValue());
	}

	@HAPOperationInfoAnnotation(in = { "boolean:simple", "boolean:simple" }, out = "boolean:simple", description = "This is operation on Integer data type to compare two Integer")
	public HAPBooleanData or(HAPData[] parms, HAPOperationContext opContext){
		HAPBooleanData booleanData1 = (HAPBooleanData)parms[0];
		HAPBooleanData booleanData2 = (HAPBooleanData)parms[1];
		boolean v2 = booleanData2==null ? false : booleanData2.getValue();
		return HAPDataTypeManager.BOOLEAN.createDataByValue(booleanData1.getValue()||v2);
	}
	
	public String not_javascript(){
		String script = "return nosliwCreateData(!parms[0].value, new NosliwDataTypeInfo(\"simple\", \"boolean\"));";
		return script;
	}	

	
}
