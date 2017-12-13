package com.nosliw.datasource;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.library.entity.v100.HAPEntityData;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;

public class HAPDataSourceUtility {

	public static HAPData setEntityAttribute(HAPData entityData, String attribute, HAPData attrData, HAPDataTypeManager dataTypeMan){
		HAPDataTypeInfo entityDataTypeInfo = new HAPDataTypeInfo("simple", "entity");
		HAPDataType entityDataType = dataTypeMan.getDataType(entityDataTypeInfo);

		HAPData[] parms = {
				entityData,
				HAPDataTypeManager.STRING.createDataByValue(attribute),
				attrData
		};
		entityData = (HAPEntityData)entityDataType.operate("setAttribute", parms, null).getData();
		return entityData;
	}
	
}
