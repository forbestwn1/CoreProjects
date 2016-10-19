package com.nosliw.data.basic.string;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.bool.HAPBooleanOperation;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPDataTypeOperationsAnnotation;

public class HAPString extends HAPDataTypeImp{

	//static attribute to store single data type instance for particular DataType, so that they can get it instantly without do search throug data type manager
	private static HAPString dataType;
	
	private HAPString(HAPDataTypeInfoWithVersion dataTypeInfo, 
					HAPDataType olderDataType, 
					HAPDataTypeInfoWithVersion parentDataTypeInfo, 
					HAPConfigure configure,
					String description,
					HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
	}
	
	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPStringOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	@Override
	public HAPData getDefaultData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPData parseLiteral(String value) {
		HAPData out = this.createDataByValue(value.toString());
		return out;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return null;
	}

	public HAPStringData createDataByValue(String value){
		return new HAPStringData(value);
	}
	
	//factory method to create data type object 
	static public HAPString createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
										HAPDataType olderDataType, 		
										HAPDataTypeInfoWithVersion parentDataTypeInfo, 
										HAPConfigure configures,
										String description,
										HAPDataTypeManager dataTypeMan){
		if(HAPString.dataType==null){
			HAPString.dataType = new HAPString(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPString.dataType;
	}
}
