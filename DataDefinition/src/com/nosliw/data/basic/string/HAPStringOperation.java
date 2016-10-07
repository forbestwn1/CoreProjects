package com.nosliw.data.basic.string;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.HAPScriptOperationInfoAnnotation;
import com.nosliw.data.basic.bool.HAPBooleanData;

public class HAPStringOperation extends HAPDataOperation{

	public HAPStringOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "string:simple", "string:simple" }, out = "boolean:simple", description = "This is operation on string data type to compare two string")
	public HAPBooleanData equals(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData stringData1 = (HAPStringData)parms[0];
		HAPStringData stringData2 = (HAPStringData)parms[1];
		return HAPDataTypeManager.BOOLEAN.createDataByValue(stringData1.getValue().equals(stringData2.getValue()));
	}
	
	@HAPOperationInfoAnnotation(in = { "string:simple", "string:simple" }, out = "string:simple", description = "This is operation on string data type to compare two string")
	public HAPStringData cascade(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData stringData1 = (HAPStringData)parms[0];
		HAPStringData stringData2 = (HAPStringData)parms[1];
		return HAPDataTypeManager.STRING.createDataByValue(stringData1.getValue()+stringData2.getValue());
	}

	@HAPOperationInfoAnnotation(in = { "boolean:simple" }, out = "string:simple", description = "This is operation on string data type to compare two string")
	public HAPStringData toString(HAPData[] parms, HAPOperationContext opContext){
		HAPBooleanData booleanData = (HAPBooleanData)parms[0];
		return HAPDataTypeManager.STRING.createDataByValue(booleanData.toString());
	}
	
	@HAPOperationInfoAnnotation(in = { "string:simple" }, out = "boolean:simple", description = "This is operation on string data type to compare two string")
	public HAPBooleanData subString(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData fullText = (HAPStringData)parms[0];
		HAPStringData subText = (HAPStringData)parms[1];
		int index = fullText.getValue().indexOf(subText.getValue());
		return HAPDataTypeManager.BOOLEAN.createDataByValue(index!=-1);
	}

	@HAPScriptOperationInfoAnnotation(dependent="boolean:simple")
	public String subString_javascript(){
		String script = "return nosliwCreateData(parms[0].value.indexOf(parms[1].value)!=-1, new NosliwDataTypeInfo(\"simple\", \"boolean\"));";
		return script;
	}	
	
	@HAPOperationInfoAnnotation(in = { "string:simple" }, out = "boolean:simple", description = "This is operation on string data type to compare two string")
	public HAPBooleanData subStringServer(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData fullText = (HAPStringData)parms[0];
		HAPStringData subText = (HAPStringData)parms[1];
		int index = fullText.getValue().indexOf(subText.getValue());
		return HAPDataTypeManager.BOOLEAN.createDataByValue(index!=-1);
	}

}
