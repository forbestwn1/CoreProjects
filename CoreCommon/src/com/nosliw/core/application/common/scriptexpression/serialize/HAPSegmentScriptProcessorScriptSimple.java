package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.List;

import com.nosliw.common.serialization.HAPUtilityJavaScript;
import com.nosliw.core.application.common.scriptexpression.HAPManualConstantInScript;
import com.nosliw.core.application.common.scriptexpression.HAPManualSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPManualSegmentScriptExpressionScriptSimple;
import com.nosliw.core.application.common.scriptexpression.HAPManualVariableInScript;

public class HAPSegmentScriptProcessorScriptSimple implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPManualSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPManualSegmentScriptExpressionScriptSimple scriptScriptExe = (HAPManualSegmentScriptExpressionScriptSimple)scriptExe;

		StringBuffer funScript = new StringBuffer();
		List<Object> scriptSegmentEles = scriptScriptExe.getParts();
		for(Object scriptSegmentEle : scriptSegmentEles){
			if(scriptSegmentEle instanceof String){
				funScript.append((String)scriptSegmentEle);
			}
			else if(scriptSegmentEle instanceof HAPManualConstantInScript){
				HAPManualConstantInScript constantInScript = (HAPManualConstantInScript)scriptSegmentEle;
				Object constantValue = constantInScript.getValue();
				if(constantValue==null) {
					//if constant value not processed, then wait until runtime
					funScript.append(constantsDataParmName + "[\"" + ((HAPManualConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
				}
				else {
					funScript.append("("+HAPUtilityJavaScript.buildConstantValue(constantValue)+")");
				}
			}
			else if(scriptSegmentEle instanceof HAPManualVariableInScript){
				String varKey = ((HAPManualVariableInScript)scriptSegmentEle).getVariableKey();
				String varValueScript = variablesDataParmName + "[\"" + varKey +"\"]";
				funScript.append("("+varValueScript+"!=undefined?"+varValueScript+":''"+")");
			}
		}
		out.setFunctionBody(funScript.toString());
		return out;
	}
}
