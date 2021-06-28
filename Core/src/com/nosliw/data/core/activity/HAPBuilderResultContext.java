package com.nosliw.data.core.activity;

import com.nosliw.data.core.valuestructure.HAPValueStructure;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPValueStructure buildResultContext(String result, HAPExecutableActivity activity);

}
