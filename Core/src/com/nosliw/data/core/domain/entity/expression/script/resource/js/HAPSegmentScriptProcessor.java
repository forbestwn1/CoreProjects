package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;

public interface HAPSegmentScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPExecutableSegmentExpression scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
