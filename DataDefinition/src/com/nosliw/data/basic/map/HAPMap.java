package com.nosliw.data.basic.map;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeImp;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperationsAnnotation;
import com.nosliw.data.basic.floa.HAPFloat;
import com.nosliw.data.basic.list.HAPListOperation;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;

public class HAPMap extends HAPDataTypeImp{

	private static HAPMap dataType;
	
	protected HAPMap(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataType olderDataType,
			HAPDataTypeInfoWithVersion parentDataTypeInfo, HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
	}

	@Override
	public void buildOperation(){
		this.setDataTypeOperations(new HAPDataTypeOperationsAnnotation(new HAPMapOperation(this.getDataTypeManager(), this), this.getDataTypeInfo(), this.getDataTypeManager()));
	}
	
	public HAPMapData newMap(){
		return new HAPMapData(this);
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
	static public HAPMap createDataType(HAPDataTypeInfoWithVersion dataTypeInfo, 
										HAPDataType olderDataType, 		
										HAPDataTypeInfoWithVersion parentDataTypeInfo, 
										HAPConfigure configures,
										String description,
										HAPDataTypeManager dataTypeMan){
		if(HAPMap.dataType==null){
			HAPMap.dataType = new HAPMap(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description, dataTypeMan);
		}
		return HAPMap.dataType;
	}
}
