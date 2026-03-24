package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpressionScriptComplex;

public class HAPSegmentScriptProcessorScriptComplex implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		HAPSegmentScriptExpressionScriptComplex dataScriptScriptExe = (HAPSegmentScriptExpressionScriptComplex)scriptExe;
		List<HAPSegmentScriptExpression> segments = dataScriptScriptExe.getChildren();
		
		StringBuffer scrip = new StringBuffer();
		for(HAPSegmentScriptExpression segment : segments) {
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
