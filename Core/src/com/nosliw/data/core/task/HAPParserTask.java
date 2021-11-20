package com.nosliw.data.core.task;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPDefinitionEntityComplex complexEntity);
	
}
