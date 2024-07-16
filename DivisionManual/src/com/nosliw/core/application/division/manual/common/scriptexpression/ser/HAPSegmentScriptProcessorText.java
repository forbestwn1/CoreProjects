package com.nosliw.core.application.division.manual.common.scriptexpression.ser;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionText;

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
