package com.nosliw.entity.data;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;

public abstract class HAPBaseDataType extends HAPDataTypeImp{
	HAPEntityDefinitionManager m_entityDefMan;
	
	protected HAPBaseDataType(HAPDataTypeInfoWithVersion dataTypeInfo,
			HAPDataType olderDataType, HAPDataTypeInfoWithVersion parentDataTypeInfo,
			HAPConfigure configures, String description,
			HAPDataTypeManager dataTypeMan,
			HAPEntityDefinitionManager entityDefMan) {
		super(dataTypeInfo, olderDataType, parentDataTypeInfo, configures, description,
				dataTypeMan);
		this.m_entityDefMan = entityDefMan;
	}
	
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){
		return this.m_entityDefMan;
	}
}
