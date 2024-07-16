package com.nosliw.core.application.division.manual.common.scriptexpression.serialize;

import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpressionScriptDataExpression;

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
