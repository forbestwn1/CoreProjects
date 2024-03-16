package com.nosliw.data.core.task;

import com.nosliw.core.application.division.manual.HAPManualEntityComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPManualEntityComplex complexEntity);
	
}
