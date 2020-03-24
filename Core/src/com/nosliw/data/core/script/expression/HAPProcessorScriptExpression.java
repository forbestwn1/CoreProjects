package com.nosliw.data.core.script.expression;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;

public class HAPProcessorScriptExpression {

	public static HAPScriptExpression processScriptExpression(HAPDefinitionScriptExpression scriptExpressionDefinition, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
		HAPScriptExpression out = new HAPScriptExpression(scriptExpressionDefinition);
		processScriptExpression(out, expressionContext, configure, expressionManager, runtime);
		return out;
	}
	
	public static void processScriptExpression(HAPScriptExpression scriptExpressionExe ,HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
		HAPDefinitionScriptExpression scriptExpressionDefinition = scriptExpressionExe.getDefinition();
		for(int i=0; i<scriptExpressionDefinition.getElements().size(); i++) {
			Object element = scriptExpressionDefinition.getElements().get(i);
			if(element instanceof HAPDefinitionExpression){
				//data expression element
				HAPDefinitionExpression expEle = ((HAPDefinitionExpression)element).cloneExpression();
				
				//preprocess attributes operand in expressions, some attributes operand can be combine into one variable operand
				HAPUtilityScriptExpression.processAttributeOperandInExpression(expEle, expressionContext.getDataVariables());

				//update with data constant
				HAPOperandUtility.updateConstantData(expEle.getOperand(), expressionContext.getDataConstants());
				
				//process expression
				HAPProcessTracker processTracker = new HAPProcessTracker();
				HAPExecutableExpression exeExpression = expressionManager.compileExpression(expEle, expressionContext.getExpressionDefinitionSuite(), null, configure, processTracker);
				scriptExpressionExe.addElement(exeExpression);
			}
			else if(element instanceof HAPScriptInScriptExpression) {
				//script element
				HAPScriptInScriptExpression scriptEle = ((HAPScriptInScriptExpression)element).cloneScriptInScriptExpression();
				
				//update with constant value
				scriptEle.updateConstantValue(expressionContext.getConstants());
				
				scriptExpressionExe.addElement(scriptEle);
			}
		}

		if(scriptExpressionExe.getVariableNames().isEmpty()){
			//if script expression has no variable in it, then we can calculate its value
			//execute script expression
			HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpressionExe, null, expressionContext.getConstants());
			HAPServiceData serviceData = runtime.executeTaskSync(task);
			scriptExpressionExe.setValue(serviceData.getData());
		}
	}
	
	public static HAPEmbededScriptExpression processEmbededScriptExpression(HAPDefinitionEmbededScriptExpression embededScriptExpressionDefinition, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
		HAPEmbededScriptExpression out = new HAPEmbededScriptExpression(embededScriptExpressionDefinition);
		processEmbededScriptExpression(out, expressionContext, configure, expressionManager, runtime);		
		return out;
	}

	public static void processEmbededScriptExpression(HAPEmbededScriptExpression embededScriptExpressionDefinitionExe, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
		HAPDefinitionEmbededScriptExpression embededScriptExpressionDefinition = embededScriptExpressionDefinitionExe.getDefinition();
		for(Object ele : embededScriptExpressionDefinition.getElements()) {
			if(ele instanceof String)   embededScriptExpressionDefinitionExe.addElement(ele);
			else if(ele instanceof HAPDefinitionScriptExpression) {
				HAPScriptExpression scriptExpression = processScriptExpression((HAPDefinitionScriptExpression)ele, expressionContext, configure, expressionManager, runtime);
				embededScriptExpressionDefinitionExe.addElement(scriptExpression);
			}
		}
	}
}
