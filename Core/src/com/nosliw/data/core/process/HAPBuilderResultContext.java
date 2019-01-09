package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.HAPContext;

public interface HAPBuilderResultContext {

	HAPContext buildResultContext(String result, HAPExecutableActivityNormal activity);

}
