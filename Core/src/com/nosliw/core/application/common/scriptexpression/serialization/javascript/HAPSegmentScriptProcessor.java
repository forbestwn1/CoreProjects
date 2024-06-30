package com.nosliw.core.application.common.scriptexpression.serialization.javascript;

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
