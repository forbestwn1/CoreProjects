package com.nosliw.core.application.common.dataassociation.definition;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPDefinitionProcessorDataAssociation {

	public static HAPDataAssociation processDataAssociation(
			HAPDefinitionDataAssociation daDef,
			HAPPath baseBlockPath, 
			HAPPath secondBlockPath,
		    Map<String, HAPPath> aliasMapping,
			HAPBundle currentBundle, 
			String rootBrickName,
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerResource resourceMan,
			HAPRuntimeInfo runtimeInfo) 
	{
		
		HAPDataAssociation out = null;
		String daType = daDef.getType();
		if(daType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
			out = HAPDefinitionProcessorMappingDataAssociation.processValueMapping((HAPDefinitionDataAssociationMapping)daDef, baseBlockPath, secondBlockPath, aliasMapping, currentBundle, rootBrickName, dataTypeHelper, resourceMan, runtimeInfo);
		}
		
		return out;
	}
}
