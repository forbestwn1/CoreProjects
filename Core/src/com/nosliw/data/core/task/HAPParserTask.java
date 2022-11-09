package com.nosliw.data.core.task;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public interface HAPParserTask {

	HAPDefinitionTask parseTaskDefinition(Object obj, HAPDefinitionEntityInDomainComplex complexEntity);
	
}
