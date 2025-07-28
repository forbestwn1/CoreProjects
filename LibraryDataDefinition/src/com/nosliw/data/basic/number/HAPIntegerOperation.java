package com.nosliw.data.basic.number;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.bool.HAPBooleanData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data1.HAPDataOperation;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperationContext;
import com.nosliw.data1.HAPOperationInfoAnnotation;
import com.nosliw.data1.HAPScriptOperationInfoAnnotation;

public class HAPIntegerOperation extends HAPDataOperation{

	HAPIntegerOperation(HAPDataTypeManager dataTypeMan, HAPDataType dataType){
		super(dataTypeMan, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "integer:simple", "integer:simple" }, out = "boolean:simple", description = "This is operation on Integer data type to compare two Integer")
	public HAPBooleanData largerThan(HAPData[] parms, HAPOperationContext opContext){
		HAPIntegerData integerData1 = (HAPIntegerData)parms[0];
		HAPIntegerData integerData2 = (HAPIntegerData)parms[1];
		boolean out = integerData1.getValue() > integerData2.getValue();
		return HAPDataTypeManager.BOOLEAN.createDataByValue(out);
	}
	
	@HAPScriptOperationInfoAnnotation(dependent="boolean:simple")
	public String largerThan_javascript(){
		String script = "return nosliwCreateData(parms[0].value>parms[1].value, new NosliwDataTypeInfo(\"simple\", \"boolean\"));";
		return script;
	}	

	@HAPOperationInfoAnnotation(in = { "string:simple"}, out = "integer:simple", description = "This is new operation")
	public HAPIntegerData new1(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData stringData = (HAPStringData)parms[0];
		return new HAPIntegerData(Integer.valueOf(stringData.getValue()));
	}	

	@HAPScriptOperationInfoAnnotation(dependent="")
	public String new1_javascript(){
		String script = "return nosliwCreateData(parseInt(parms[0].value), new NosliwDataTypeInfo(\"simple\", \"integer\"));";
		return script;
	}	
	
}
