package com.nosliw.core.application.division.manual.common.scriptexpression.ser;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionScriptComplex;

public class HAPSegmentScriptProcessorDataScript implements HAPSegmentScriptProcessor{

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
				segmentProcessOutput = new HAPSegmentScriptProcessorScript().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
				segmentProcessOutput = new HAPSegmentScriptProcessorData().processor(segment, funciontParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
			}
			scrip.append(segmentProcessOutput.getFunctionBody());
		}
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		out.setFunctionBody(scrip.toString());
		return out;
	}

}
