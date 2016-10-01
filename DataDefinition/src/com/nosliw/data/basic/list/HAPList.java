package com.nosliw.data.basic.list;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.basic.bool.HAPBooleanOperation;
import com.nosliw.data.basic.floa.HAPFloat;
import com.nosliw.data.basic.map.HAPMap;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPList extends HAPDataTypeImp{

	private static HAPList dataType;

	protected HAPList(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
			HAPDataTypeInfoWithVersion parentDataTypeInfo, HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	public HAPListData newList(){
		HAPListData out = new HAPListData(this);
		return out;
	}
	
	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPListOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	@Override
	public HAPData getDefaultData() {
		return null;
	}

	@Override
	public HAPServiceData validate(HAPData data) {
		return null;
	}

	//factory method to create data type object 
	static public HAPList createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
										HAPDataType olderDataType, 		
										HAPDataTypeInfoWithVersion parentDataTypeInfo, 
										HAPConfigure configures,
										String description,
										HAPDataTypeManager dataTypeMan){
		if(HAPList.dataType==null){
			HAPList.dataType = new HAPList(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPList.dataType;
	}

}
