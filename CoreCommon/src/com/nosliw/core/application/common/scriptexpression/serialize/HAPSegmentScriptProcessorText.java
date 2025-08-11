package com.nosliw.core.application.common.scriptexpression.serialize;

import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpressionText;

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
