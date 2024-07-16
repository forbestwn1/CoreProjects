package com.nosliw.core.application.division.manual.common.scriptexpression.serialize;

import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpressionText;

public class HAPSegmentScriptProcessorText implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPManualSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPManualSegmentScriptExpressionText textScriptExe = (HAPManualSegmentScriptExpressionText)scriptExe;
		out.setFunctionBody("\""+textScriptExe.getContent()+"\"");
		return out;
	}
}
