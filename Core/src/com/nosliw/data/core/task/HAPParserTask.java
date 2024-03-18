package com.nosliw.data.core.task;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPManualBrickComplex complexEntity);
	
}
