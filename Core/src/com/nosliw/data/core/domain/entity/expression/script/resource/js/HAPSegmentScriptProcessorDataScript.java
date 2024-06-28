package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionDataScript;

public class HAPSegmentScriptProcessorDataScript implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPSegmentScriptExpressionDataScript dataScriptScriptExe = (HAPSegmentScriptExpressionDataScript)scriptExe;
		List<HAPSegmentScriptExpression> segments = dataScriptScriptExe.getSegments();
		
		StringBuffer scrip = new StringBuffer();
		for(HAPSegmentScriptExpression segment : segments) {
			HAPOutputScriptProcessor segmentProcessOutput = null;
			String segType = segment.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT)) {
				segmentProcessOutput = new HAPSegmentScriptProcessorScript().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA)) {
				segmentProcessOutput = new HAPSegmentScriptProcessorData().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			scrip.append(segmentProcessOutput.getFunctionBody());
		}
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		out.setFunctionBody(scrip.toString());
		return out;
	}

}
