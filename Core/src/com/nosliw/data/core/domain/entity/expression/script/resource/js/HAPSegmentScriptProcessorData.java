package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpressionData;

public class HAPSegmentScriptProcessorData implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPExecutableSegmentExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPExecutableSegmentExpressionData dataScriptExe = (HAPExecutableSegmentExpressionData)scriptExe;
		out.setFunctionBody(expressionsDataParmName+"["+dataScriptExe.getDataExpressionId()+"]");
		return out;
	}
}
