package com.nosliw.core.application.division.manual.common.scriptexpression.ser;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;

public interface HAPSegmentScriptProcessor {

	HAPOutputScriptProcessor processor(
		HAPManualSegmentScriptExpression scriptExe,
		String funciontParmName,
		String expressionsDataParmName,
		String constantsDataParmName,
		String variablesDataParmName
	);

}
