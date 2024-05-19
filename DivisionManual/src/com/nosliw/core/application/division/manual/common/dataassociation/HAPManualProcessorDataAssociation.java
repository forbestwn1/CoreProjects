package com.nosliw.core.application.division.manual.common.dataassociation;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.mapping.HAPManualDataAssociationMapping;
import com.nosliw.core.application.division.manual.common.dataassociation.mapping.HAPManualProcessorDataAssociationMapping;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualProcessorDataAssociation {

	public static HAPDataAssociation processDataAssociation(
			HAPManualDataAssociation daDef,
			HAPPath baseBlockPath, 
			HAPBundle currentBundle, 
			HAPRuntimeEnvironment runtimeEnv) {
		
		HAPDataAssociation out = null;
		String daType = daDef.getType();
		if(daType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
			out = HAPManualProcessorDataAssociationMapping.processValueMapping((HAPManualDataAssociationMapping)daDef, baseBlockPath, currentBundle, runtimeEnv);
		}
		
		return out;
	}
}
