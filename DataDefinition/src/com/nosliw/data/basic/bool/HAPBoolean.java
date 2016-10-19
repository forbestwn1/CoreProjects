package com.nosliw.data.basic.bool;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data.utils.HAPDataErrorUtility;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPDataTypeOperationsAnnotation;

public class HAPBoolean extends HAPDataTypeImp{

	private static HAPBoolean dataType;
	
	private HAPBoolean(HAPDataTypeInfoWithVersion dataTypeInfo, 
						HAPDataType olderDataType, 
						HAPDataTypeInfoWithVersion parentDataTypeInfo, 
						HAPConfigure configure,
						String description,
						HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
	}
	
	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPBooleanOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	@Override
	public HAPData getDefaultData() {
		return createDataByValue(false);
	}

	@Override
	public HAPData parseLiteral(String value) {
		return createDataByValue(Boolean.parseBoolean(value.toString()));
	}

	@Override
	public HAPData parseJson(Object jsonObj){
		return createDataByValue(Boolean.parseBoolean(jsonObj.toString()));
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		HAPDataTypeInfo dataTypeInfo1 = HAPDataUtility.getDataTypeInfo(data);
		if(!this.getDataTypeInfo().equalsWithoutVersion(dataTypeInfo1))  return HAPDataErrorUtility.createDataTypeError(dataTypeInfo1, this.getDataTypeInfo(), null);
		return HAPServiceData.createSuccessData();
	}

	public HAPBooleanData createDataByValue(boolean value){
		return new HAPBooleanData(value);
	}
	
	//factory method to create data type object 
	static public HAPBoolean createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
											HAPDataType olderDataType, 		
											HAPDataTypeInfoWithVersion parentDataTypeInfo, 
											HAPConfigure configures,
											String description,
											HAPDataTypeManager dataTypeMan){
		if(HAPBoolean.dataType==null){
			HAPBoolean.dataType = new HAPBoolean(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPBoolean.dataType;
	}
}
