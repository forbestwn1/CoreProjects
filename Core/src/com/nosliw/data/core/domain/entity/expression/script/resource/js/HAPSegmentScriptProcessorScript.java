package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import java.util.List;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableConstantInScript;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpressionScript;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableVariableInScript;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;
import com.nosliw.data.core.script.expression1.imp.expression.HAPConstantInScript;

public class HAPSegmentScriptProcessorScript implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPExecutableSegmentExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPExecutableSegmentExpressionScript scriptScriptExe = (HAPExecutableSegmentExpressionScript)scriptExe;

		StringBuffer funScript = new StringBuffer();
		List<Object> scriptSegmentEles = scriptScriptExe.getParts();
		for(Object scriptSegmentEle : scriptSegmentEles){
			if(scriptSegmentEle instanceof String){
				funScript.append((String)scriptSegmentEle);
			}
			else if(scriptSegmentEle instanceof HAPExecutableConstantInScript){
				HAPExecutableConstantInScript constantInScript = (HAPExecutableConstantInScript)scriptSegmentEle;
				Object constantValue = constantInScript.getValue();
				if(constantValue==null) {
					//if constant value not processed, then wait until runtime
					funScript.append(constantsDataParmName + "[\"" + ((HAPConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
				}
				else {
					funScript.append("("+HAPUtilityRuntimeJS.buildConstantValue(constantValue)+")");
				}
			}
			else if(scriptSegmentEle instanceof HAPExecutableVariableInScript){
				String varKey = ((HAPExecutableVariableInScript)scriptSegmentEle).getVariableKey();
				String varValueScript = variablesDataParmName + "[\"" + varKey +"\"]";
				funScript.append("("+varValueScript+"!=undefined?"+varValueScript+":''"+")");
			}
		}
		out.setFunctionBody(funScript.toString());
		return out;
	}
}
