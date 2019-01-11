package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.HAPContext;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPContext buildResultContext(String result, HAPExecutableActivityNormal activity);

}
