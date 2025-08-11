package com.nosliw.core.application.common.scriptexpression.serialize;

import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPManualSegmentScriptExpressionScriptDataExpression;

public class HAPSegmentScriptProcessorDataExpression implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPManualSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPManualSegmentScriptExpressionScriptDataExpression dataScriptExe = (HAPManualSegmentScriptExpressionScriptDataExpression)scriptExe;
		out.setFunctionBody(expressionsDataParmName+"[\""+dataScriptExe.getDataExpressionId()+"\"]");
		return out;
	}
}
