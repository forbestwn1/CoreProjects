package com.nosliw.uiresource.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.expressionscript.HAPContextExpressionProcess;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.uiresource.page.HAPEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.HAPEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;

public class HAPUIResourceExpressionProcessor {

	public static void process(HAPUIDefinitionUnitResource uiResource, HAPRuntime runtime, HAPResourceManagerRoot resourceMan){
		
		//process all script expressions in resource
		processScriptExpression(uiResource, runtime);
		
		//process script expression which turn out to be constant
		processConstantExpressionInAttributeTag(uiResource);
		
	}
	
	//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
	private static void processConstantExpressionInAttributeTag(HAPUIDefinitionUnit uiDefinitionUnit){
		Set<HAPEmbededScriptExpressionInAttribute> removed = new HashSet<HAPEmbededScriptExpressionInAttribute>();
		Set<HAPEmbededScriptExpressionInAttribute> all = uiDefinitionUnit.getScriptExpressionsInTagAttributes();
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : all){
			if(embededScriptExpression.isConstant()){
				String value = embededScriptExpression.getValue();
				HAPUIDefinitionUnitTag tag = uiDefinitionUnit.getUITagesByName().get(embededScriptExpression.getUIId());
				tag.addAttribute(embededScriptExpression.getAttribute(), value);
				removed.add(embededScriptExpression);
			}
		}
		
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);
		
		for(HAPUIDefinitionUnit childTag : uiDefinitionUnit.getUITags())		processConstantExpressionInAttributeTag(childTag);
	}
	
	private static void processScriptExpression(HAPUIDefinitionUnit uiDefinitionUnit, HAPRuntime runtime){
		List<HAPScriptExpression> scriptExpressions = new ArrayList<HAPScriptExpression>();
		
		for(HAPEmbededScriptExpressionInContent scriptExpressionInConent : uiDefinitionUnit.getScriptExpressionsInContent())  scriptExpressions.addAll(scriptExpressionInConent.getScriptExpressionsList());
		for(HAPEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInAttributes())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		for(HAPEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInTagAttributes())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		processScriptExpression(scriptExpressions, uiDefinitionUnit, runtime);

		for(HAPUIDefinitionUnit child : uiDefinitionUnit.getUITags()){
			processScriptExpression(child, runtime);
		}
	}

	private static void processScriptExpression(List<HAPScriptExpression> scriptExpressions, HAPUIDefinitionUnit uiDefinitionUnit, HAPRuntime runtime){
		HAPContextExpressionProcess expContext = uiDefinitionUnit.getExpressionContext();
		for(HAPScriptExpression scriptExpression : scriptExpressions){
			scriptExpression.processExpressions(expContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));
			scriptExpression.discoverVarialbes();

			if(scriptExpression.getVariableNames().isEmpty()){
				//if script expression has no variable in it, then we can calculate its value
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpression, null, uiDefinitionUnit.getConstantValues());
				HAPServiceData serviceData = runtime.executeTaskSync(task);
				scriptExpression.setValue(serviceData.getData());
			}
		}
	}
}
