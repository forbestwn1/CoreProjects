package com.nosliw.core.application.common.scriptexpression.serialization.javascript;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionText;

public class HAPSegmentScriptProcessorText implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPSegmentScriptExpressionText textScriptExe = (HAPSegmentScriptExpressionText)scriptExe;
		out.setFunctionBody("\""+textScriptExe.getContent()+"\"");
		return out;
	}
}
