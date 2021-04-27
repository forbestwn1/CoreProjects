package com.nosliw.data.core.process;

import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPContextStructureValueDefinition buildResultContext(String result, HAPExecutableActivityNormal activity);

}
