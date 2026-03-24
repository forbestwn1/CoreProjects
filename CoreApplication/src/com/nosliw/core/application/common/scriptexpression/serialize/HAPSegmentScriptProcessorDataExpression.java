package com.nosliw.core.application.common.scriptexpression.serialize;

import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpressionScriptDataExpression;

public class HAPSegmentScriptProcessorDataExpression implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPSegmentScriptExpressionScriptDataExpression dataScriptExe = (HAPSegmentScriptExpressionScriptDataExpression)scriptExe;
		out.setFunctionBody(expressionsDataParmName+"[\""+dataScriptExe.getDataExpressionId()+"\"]");
		return out;
	}
}
