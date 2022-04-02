package com.nosliw.data.core.task;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPDefinitionEntityInDomainComplex complexEntity);
	
}
