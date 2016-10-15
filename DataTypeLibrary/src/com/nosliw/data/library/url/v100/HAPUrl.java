package com.nosliw.data.library.url.v100;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.basic.number.HAPIntegerOperation;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;

public class HAPUrl extends HAPDataTypeImp{

	private HAPUrl(HAPDataTypeInfoWithVersion dataTypeInfo, 
					HAPDataType olderDataType, 
					HAPDataTypeInfoWithVersion parentDataTypeInfo, 
					HAPConfigure configure,
					String description,
					HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configure, description, dataTypeMan);
	}

	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPUrlOperation(this.getDataTypeManager()), this.getDataTypeInfo(), this.getDataTypeManager()));
	}

	@Override
	public HAPData parseLiteral(String value) {
		HAPData out = this.createDataByValue(value.toString());
		return out;
	}
	
	public HAPUrlData createDataByValue(String value){
		return new HAPUrlData(value, this);
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
	static public HAPUrl createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
			HAPDataType olderDataType, 		
			HAPDataTypeInfoWithVersion parentDataTypeInfo, 
			HAPConfigure configures,
			String description,
			HAPDataTypeManager dataTypeMan){
		return new HAPUrl(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	
}
