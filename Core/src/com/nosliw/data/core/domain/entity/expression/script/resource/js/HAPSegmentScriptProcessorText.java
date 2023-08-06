package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpressionText;

public class HAPSegmentScriptProcessorText implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPExecutableSegmentExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPExecutableSegmentExpressionText textScriptExe = (HAPExecutableSegmentExpressionText)scriptExe;
		out.setFunctionBody("\""+textScriptExe.getContent()+"\"");
		return out;
	}
}
