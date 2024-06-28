package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionData;

public class HAPSegmentScriptProcessorData implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPSegmentScriptExpressionData dataScriptExe = (HAPSegmentScriptExpressionData)scriptExe;
		out.setFunctionBody(expressionsDataParmName+"[\""+dataScriptExe.getDataExpressionId()+"\"]");
		return out;
	}
}
