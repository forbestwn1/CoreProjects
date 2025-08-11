package com.nosliw.core.application.common.scriptexpression.serialize;

import java.util.List;

import com.nosliw.common.serialization.HAPUtilityJavaScript;
import com.nosliw.core.application.common.scriptexpressio.HAPConstantInScript;
import com.nosliw.core.application.common.scriptexpressio.HAPVariableInScript;
import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPSegmentScriptExpressionScriptSimple;

public class HAPSegmentScriptProcessorScriptSimple implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(HAPSegmentScriptExpression scriptExe, String funciontParmName,
			String expressionsDataParmName, String constantsDataParmName, String variablesDataParmName) {
		
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		HAPSegmentScriptExpressionScriptSimple scriptScriptExe = (HAPSegmentScriptExpressionScriptSimple)scriptExe;

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
					funScript.append("("+HAPUtilityJavaScript.buildConstantValue(constantValue)+")");
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
