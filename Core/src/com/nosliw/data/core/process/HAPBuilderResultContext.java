package com.nosliw.data.core.process;

import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPStructureValueDefinition buildResultContext(String result, HAPExecutableActivityNormal activity);

}
