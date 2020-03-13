package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.expression.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUtilityExecutable;

public class HAPProcessorUIExpression {

	public static void processUIExpression(HAPExecutableUIUnit exeUnit, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

		//process all embeded script expression
		List<HAPEmbededScriptExpression> embededScriptExpressions = HAPUtilityExecutable.getEmbededScriptExpressionFromExeUnit(exeUnit);
		for(HAPEmbededScriptExpression embededScriptExpression : embededScriptExpressions) {
			HAPProcessorScriptExpression.processEmbededScriptExpression(embededScriptExpression, exeUnit.getExpressionContext(), HAPExpressionProcessConfigureUtil.setDoDiscovery(null), expressionManager, runtime);
		}

		//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		Set<HAPUIEmbededScriptExpressionInAttribute> all = exeUnit.getScriptExpressionsInTagAttribute();
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
			if(embededScriptExpression.isConstant()){
				String value = embededScriptExpression.getValue();
				HAPExecutableUIUnitTag tag = exeUnit.getUITagesByName().get(embededScriptExpression.getUIId());
				tag.addAttribute(embededScriptExpression.getAttribute(), value);
				removed.add(embededScriptExpression);
			}
		}
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);

		
		//child tag
		for(HAPExecutableUIUnitTag childTag : exeUnit.getUITags()) {
			processUIExpression(childTag, runtime, expressionManager);			
		}
	}
	
}
