package com.nosliw.core.application.division.manual.common.scriptexpression.ser;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionScriptDataExpression;

public class HAPSegmentScriptProcessorData implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPManualSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPManualSegmentScriptExpressionScriptDataExpression dataScriptExe = (HAPManualSegmentScriptExpressionScriptDataExpression)scriptExe;
		out.setFunctionBody(expressionsDataParmName+"[\""+dataScriptExe.getDataExpressionId()+"\"]");
		return out;
	}
}
