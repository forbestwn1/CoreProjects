package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import java.util.List;

import com.nosliw.core.application.common.scriptexpression.HAPConstantInScript;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPVariableInScript;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;

public class HAPSegmentScriptProcessorScript implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPSegmentScriptExpressionScript scriptScriptExe = (HAPSegmentScriptExpressionScript)scriptExe;

		StringBuffer funScript = new StringBuffer();
		List<Object> scriptSegmentEles = scriptScriptExe.getParts();
		for(Object scriptSegmentEle : scriptSegmentEles){
			if(scriptSegmentEle instanceof String){
				funScript.append((String)scriptSegmentEle);
			}
			else if(scriptSegmentEle instanceof HAPConstantInScript){
				HAPConstantInScript constantInScript = (HAPConstantInScript)scriptSegmentEle;
				Object constantValue = constantInScript.getValue();
				if(constantValue==null) {
					//if constant value not processed, then wait until runtime
					funScript.append(constantsDataParmName + "[\"" + ((HAPConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
				}
				else {
					funScript.append("("+HAPUtilityRuntimeJS.buildConstantValue(constantValue)+")");
				}
			}
			else if(scriptSegmentEle instanceof HAPVariableInScript){
				String varKey = ((HAPVariableInScript)scriptSegmentEle).getVariableKey();
				String varValueScript = variablesDataParmName + "[\"" + varKey +"\"]";
				funScript.append("("+varValueScript+"!=undefined?"+varValueScript+":''"+")");
			}
		}
		out.setFunctionBody(funScript.toString());
		return out;
	}
}
