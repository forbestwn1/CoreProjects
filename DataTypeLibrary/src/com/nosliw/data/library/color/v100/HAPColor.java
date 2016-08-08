package com.nosliw.data.library.color.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPColor extends HAPDataTypeImp{

	private HAPColor(HAPDataTypeInfoWithVersion dataTypeInfo, 
					HAPDataType olderDataType, 
					HAPDataTypeInfoWithVersion parentDataTypeInfo, 
					HAPConfigure configure,
					String description,
					HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPColorOperation(dataTypeMan), dataTypeInfo, dataTypeMan));
	}

	@Override
	public HAPData getDefaultData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPData toData(Object value, String format) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HAPServiceData validate(HAPData data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//factory method to create data type object 
	static public HAPColor createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
			HAPDataType olderDataType, 		
			HAPDataTypeInfoWithVersion parentDataTypeInfo, 
			HAPConfigure configures,
			String description,
			HAPDataTypeManager dataTypeMan){
		return new HAPColor(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	
}
