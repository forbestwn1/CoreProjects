package com.nosliw.uiresource.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPContextExpressionProcess;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPUIResourceExpressionProcessor {

	public static void process(HAPDefinitionUIUnitResource uiResource, HAPRuntime runtime, HAPResourceManagerRoot resourceMan){
		
		//process all script expressions in resource
		processScriptExpression(uiResource, runtime);
		
		//process script expression which turn out to be constant
		processConstantExpressionInAttributeTag(uiResource);
		
	}
	
	//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
	private static void processConstantExpressionInAttributeTag(HAPDefinitionUIUnit uiDefinitionUnit){
		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		Set<HAPUIEmbededScriptExpressionInAttribute> all = uiDefinitionUnit.getScriptExpressionsInTagAttributes();
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
			if(embededScriptExpression.isConstant()){
				String value = embededScriptExpression.getValue();
				HAPDefinitionUIUnitTag tag = uiDefinitionUnit.getUITagesByName().get(embededScriptExpression.getUIId());
				tag.addAttribute(embededScriptExpression.getAttribute(), value);
				removed.add(embededScriptExpression);
			}
		}
		
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);
		
		for(HAPDefinitionUIUnit childTag : uiDefinitionUnit.getUITags())		processConstantExpressionInAttributeTag(childTag);
	}
	
	private static void processScriptExpression(HAPDefinitionUIUnit uiDefinitionUnit, HAPRuntime runtime){
		List<HAPScriptExpression> scriptExpressions = new ArrayList<HAPScriptExpression>();
		
		for(HAPUIEmbededScriptExpressionInContent scriptExpressionInConent : uiDefinitionUnit.getScriptExpressionsInContent())  scriptExpressions.addAll(scriptExpressionInConent.getScriptExpressionsList());
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInAttributes())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInTagAttributes())  scriptExpressions.addAll(scriptExpressionInAttribute.getScriptExpressionsList()); 
		processScriptExpression(scriptExpressions, uiDefinitionUnit, runtime);

		for(HAPDefinitionUIUnit child : uiDefinitionUnit.getUITags()){
			processScriptExpression(child, runtime);
		}
	}

	private static void processScriptExpression(List<HAPScriptExpression> scriptExpressions, HAPDefinitionUIUnit uiDefinitionUnit, HAPRuntime runtime){
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
