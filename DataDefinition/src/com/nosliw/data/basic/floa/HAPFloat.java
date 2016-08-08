package com.nosliw.data.basic.floa;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.basic.bool.HAPBooleanOperation;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPFloat extends HAPDataTypeImp{

	private static HAPFloat dataType;
	
	private HAPFloat(HAPDataTypeInfoWithVersion dataTypeInfo,
			HAPDataType olderDataType, 
			HAPDataTypeInfoWithVersion parentDataTypeInfo, 
			HAPConfigure configure,
			String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
	}
	
	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPFloatOperation(this.getDataTypeManager()), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	@Override
	public HAPData getDefaultData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPData parseLiteral(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		// TODO Auto-generated method stub
		return null;
	}

	public HAPFloatData createDataByValue(float value){
		return new HAPFloatData(value);
	}
	
	//factory method to create data type object 
	static public HAPFloat createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
										HAPDataType olderDataType, 		
										HAPDataTypeInfoWithVersion parentDataTypeInfo, 
										HAPConfigure configures,
										String description,
										HAPDataTypeManager dataTypeMan){
		if(HAPFloat.dataType==null){
			HAPFloat.dataType = new HAPFloat(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPFloat.dataType;
	}
}
