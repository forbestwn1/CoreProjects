package com.nosliw.data.core.task;

import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPManualEntityComplex complexEntity);
	
}
