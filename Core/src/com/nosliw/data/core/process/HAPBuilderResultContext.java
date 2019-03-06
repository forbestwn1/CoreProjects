package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.HAPContextStructure;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPContextStructure buildResultContext(String result, HAPExecutableActivityNormal activity);

}
