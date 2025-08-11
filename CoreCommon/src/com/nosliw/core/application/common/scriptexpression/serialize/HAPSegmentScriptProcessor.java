package com.nosliw.core.application.common.scriptexpression.serialize;

import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpression;

public interface HAPSegmentScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPManualSegmentScriptExpression scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
