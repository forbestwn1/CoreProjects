package com.nosliw.core.application.division.manual.common.scriptexpression.serialize;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualSegmentScriptExpressionScriptComplex;

public class HAPSegmentScriptProcessorScriptComplex implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPManualSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPManualSegmentScriptExpressionScriptComplex dataScriptScriptExe = (HAPManualSegmentScriptExpressionScriptComplex)scriptExe;
		List<HAPManualSegmentScriptExpression> segments = dataScriptScriptExe.getChildren();
		
		StringBuffer scrip = new StringBuffer();
		for(HAPManualSegmentScriptExpression segment : segments) {
			HAPOutputScriptProcessor segmentProcessOutput = null;
			String segType = segment.getType();
			if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
				segmentProcessOutput = new HAPSegmentScriptProcessorScriptSimple().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
				segmentProcessOutput = new HAPSegmentScriptProcessorDataExpression().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			scrip.append(segmentProcessOutput.getFunctionBody());
		}
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		out.setFunctionBody(scrip.toString());
		return out;
	}

}
