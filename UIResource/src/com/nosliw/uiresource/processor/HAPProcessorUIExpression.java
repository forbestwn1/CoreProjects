package com.nosliw.uiresource.processor;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPContextScriptExpressionProcess;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPProcessorUIExpression {

	public static void processUIExpression(HAPExecutableUIUnit exeUnit, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		processScriptExpression(exeUnit, runtime, expressionManager);
	}
	
	private static void processScriptExpression(HAPExecutableUIUnit exeUnit, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		List<HAPScriptExpression> scriptExpressions = new ArrayList<HAPScriptExpression>();
		
		for(HAPUIEmbededScriptExpressionInContent scriptExpressionInConent : exeUnit.getScriptExpressionsInContent())  scriptExpressions.addAll(scriptExpressionInConent.getScriptExpressionsList());
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : exeUnit.getScriptExpressionsInAttribute())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : exeUnit.getScriptExpressionsInTagAttribute())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		processScriptExpression(scriptExpressions, exeUnit, runtime, expressionManager);

	}

	private static void processScriptExpression(List<HAPScriptExpression> scriptExpressions, HAPExecutableUIUnit exeUnit, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		HAPContextScriptExpressionProcess expContext = exeUnit.getExpressionContext();
		for(HAPScriptExpression scriptExpression : scriptExpressions){
			scriptExpression.processExpressions(expContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), expressionManager);

			if(scriptExpression.getVariableNames().isEmpty()){
				//if script expression has no variable in it, then we can calculate its value
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpression, null, exeUnit.getConstantsValue());
				HAPServiceData serviceData = runtime.executeTaskSync(task);
				scriptExpression.setValue(serviceData.getData());
			}
		}
	}
}
