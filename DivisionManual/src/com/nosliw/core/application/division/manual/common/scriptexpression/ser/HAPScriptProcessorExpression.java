package com.nosliw.core.application.division.manual.common.scriptexpression.ser;

import java.util.List;

import com.nosliw.core.application.common.scriptexpression.HAPSegmentScriptExpression;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;
import com.nosliw.data.core.script.expression1.HAPExecutableScript;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptWithSegment;
import com.nosliw.data.core.script.expression1.imp.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression1.imp.expression.HAPExecutableScriptSegExpression;
import com.nosliw.data.core.script.expression1.imp.expression.HAPExecutableScriptSegScript;
import com.nosliw.data.core.script.expression1.imp.expression.HAPVariableInScript;

public class HAPScriptProcessorExpression implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(
			HAPManualSegmentScriptExpression scriptExe, 
			String funciontParmName,
			String expressionsDataParmName, 
			String constantsDataParmName, 
			String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		
		HAPExecutableScriptWithSegment expressionScript = (HAPExecutableScriptWithSegment)scriptExe;
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
						String varName = ((HAPVariableInScript)scriptSegmentEle).getVariableName();
						String varValueScript = variablesDataParmName + "[\"" + varName +"\"]";
						funScript.append("("+varValueScript+"!=undefined?"+varValueScript+":''"+")");
					}
				}
			}
			i++;
		}
		out.setFunctionBody(funScript.toString());
		return out;
	}

}
