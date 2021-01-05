package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptGroupImp;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;

public class HAPProcessorUIExpression {

	public static void processUIExpression(HAPExecutableUIUnit exeUnit, HAPRuntime runtime, HAPManagerExpression expressionManager, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIBody body = exeUnit.getBody();
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		HAPDefinitionScriptGroupImp scriptGroup = new HAPDefinitionScriptGroupImp();
		for(HAPDefinitionUIEmbededScriptExpressionInContent embededContent : uiUnitDef.getScriptExpressionsInContent()) {
			scriptGroup.addEntityElement(embededContent);
		}
		//embeded script in tag attribute 
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInAttribute()) {
			scriptGroup.addEntityElement(embededAttribute);
		}
		//embeded script in custom tag attribute
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInTagAttribute()) {
			scriptGroup.addEntityElement(embededAttribute);
		}

		HAPExecutableScriptGroup scriptGroupExe = HAPProcessorScript.processScript(exeUnit.getId(), scriptGroup, body.getExpressionContext(), expressionManager, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());
		body.setScriptGroupExecutable(scriptGroupExe);
		
//		//process all embeded script expression
//		List<HAPEmbededScriptExpression> embededScriptExpressions = HAPUtilityExecutable.getEmbededScriptExpressionFromExeUnit(exeUnit);
//		for(HAPEmbededScriptExpression embededScriptExpression : embededScriptExpressions) {
//			HAPProcessorScript.processEmbededScriptExpression(embededScriptExpression, exeUnit.getExpressionContext(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), expressionManager, runtime);
//		}
//
//		//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
//		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
//		Set<HAPUIEmbededScriptExpressionInAttribute> all = exeUnit.getScriptExpressionsInTagAttribute();
//		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
//			if(embededScriptExpression.isConstant()){
//				String value = embededScriptExpression.getValue();
//				HAPExecutableUIUnitTag tag = exeUnit.getUITagesByName().get(embededScriptExpression.getUIId());
//				tag.addAttribute(embededScriptExpression.getAttribute(), value);
//				removed.add(embededScriptExpression);
//			}
//		}
//		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);

		
		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			processUIExpression(childTag, runtime, expressionManager, runtimeEnv);			
		}
	}
	
}
