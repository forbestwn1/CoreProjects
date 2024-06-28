package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;

public interface HAPSegmentScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPSegmentScriptExpression scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
