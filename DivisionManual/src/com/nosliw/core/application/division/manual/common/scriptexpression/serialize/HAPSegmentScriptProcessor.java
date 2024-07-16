package com.nosliw.core.application.division.manual.common.scriptexpression.serialize;

import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpression;

public interface HAPSegmentScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPManualSegmentScriptExpression scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
