package com.nosliw.data.basic.bool;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationInfoAnnotation;

public class HAPBooleanOperation extends HAPDataOperation{

	public HAPBooleanOperation(HAPDataTypeManager man) {
		super(man);
	}

	@HAPOperationInfoAnnotation(in = { "integer:simple", "integer:simple" }, out = "boolean:simple", description = "This is operation on Integer data type to compare two Integer")
	public HAPBooleanData not(HAPData[] parms){
		HAPBooleanData booleanData = (HAPBooleanData)parms[0];
		return HAPDataTypeManager.BOOLEAN.createDataByValue(!booleanData.getValue());
	}
	
	public String not_javascript(){
		String script = "return nosliwCreateData(!parms[0].value, new NosliwDataTypeInfo(\"simple\", \"boolean\"));";
		return script;
	}	

	
}
