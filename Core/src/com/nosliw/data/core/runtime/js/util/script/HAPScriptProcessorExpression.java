package com.nosliw.data.core.runtime.js.util.script;

import java.util.List;

import com.nosliw.data.core.script.expression.HAPExecutableScript;
import com.nosliw.data.core.script.expression.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression.expression.HAPExecutableScriptExpression;
import com.nosliw.data.core.script.expression.expression.HAPExecutableScriptSegExpression;
import com.nosliw.data.core.script.expression.expression.HAPExecutableScriptSegScript;
import com.nosliw.data.core.script.expression.expression.HAPVariableInScript;

public class HAPScriptProcessorExpression implements HAPScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(
			HAPExecutableScript scriptExe, 
			String funciontParmName,
			String expressionsDataParmName, 
			String constantsDataParmName, 
			String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		
		HAPExecutableScriptExpression expressionScript = (HAPExecutableScriptExpression)scriptExe;
		StringBuffer funScript = new StringBuffer();
		int i=0;
		for(HAPExecutableScript seg : expressionScript.getSegments()){
			if(seg instanceof HAPExecutableScriptSegExpression){
				HAPExecutableScriptSegExpression expressionSeg = (HAPExecutableScriptSegExpression)seg;
				funScript.append(expressionsDataParmName+"[\""+expressionSeg.getExpressionId()+"\"]");
			}
			else if(seg instanceof HAPExecutableScriptSegScript){
				HAPExecutableScriptSegScript scriptSegment = (HAPExecutableScriptSegScript)seg;
				List<Object> scriptSegmentEles = scriptSegment.getElements();
				for(Object scriptSegmentEle : scriptSegmentEles){
					if(scriptSegmentEle instanceof String){
						funScript.append((String)scriptSegmentEle);
					}
					else if(scriptSegmentEle instanceof HAPConstantInScript){
						funScript.append(constantsDataParmName + "[\"" + ((HAPConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
					}
					else if(scriptSegmentEle instanceof HAPVariableInScript){
						funScript.append(variablesDataParmName + "[\"" + ((HAPVariableInScript)scriptSegmentEle).getVariableName()+"\"]");
					}
				}
			}
			i++;
		}
		out.setFunctionBody(funScript.toString());
		return out;
	}

}
