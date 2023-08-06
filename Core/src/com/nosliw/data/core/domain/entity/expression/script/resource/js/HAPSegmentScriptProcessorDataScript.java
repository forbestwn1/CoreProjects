package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import java.util.List;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpressionDataScript;

public class HAPSegmentScriptProcessorDataScript implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPExecutableSegmentExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPExecutableSegmentExpressionDataScript dataScriptScriptExe = (HAPExecutableSegmentExpressionDataScript)scriptExe;
		List<HAPExecutableSegmentExpression> segments = dataScriptScriptExe.getSegments();
		
		for(HAPExecutableSegmentExpression segment : segments) {
			out.addChild(segment);
		}
		out.setFunctionBody(HAPUtilityScriptForExecuteJSScript.buildSegmentFunctionScript(segments).toString());
		return out;
	}
}
