package com.nosliw.data.basic.doubl;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;

public class HAPDouble extends HAPDataTypeImp{

	private static HAPDouble dataType;
	
	private HAPDouble(HAPDataTypeInfoWithVersion dataTypeInfo,
			HAPDataType olderDataType, 
			HAPDataTypeInfoWithVersion parentDataTypeInfo, 
			HAPConfigure configure,
			String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
	}
	
	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPDoubleOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
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

	public HAPDoubleData createDataByValue(double value){
		return new HAPDoubleData(value);
	}
	
	//factory method to create data type object 
	static public HAPDouble createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
										HAPDataType olderDataType, 		
										HAPDataTypeInfoWithVersion parentDataTypeInfo, 
										HAPConfigure configures,
										String description,
										HAPDataTypeManager dataTypeMan){
		if(HAPDouble.dataType==null){
			HAPDouble.dataType = new HAPDouble(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPDouble.dataType;
	}
}
