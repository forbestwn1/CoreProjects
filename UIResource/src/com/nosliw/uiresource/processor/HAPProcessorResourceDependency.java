package com.nosliw.uiresource.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.resource.HAPResourceIdUITag;
import com.nosliw.uiresource.tag.HAPUITagId;

public class HAPProcessorResourceDependency {

	public static void process(HAPExecutableUIUnitResource uiResource, HAPResourceManagerRoot resourceMan){
		 Set<HAPResourceId> dependencyResourceIds = new LinkedHashSet();
		
		//resources need by expression
		List<HAPExecuteExpression> expressions = discoverExpressionsInUIResource(uiResource);
		for(HAPExecuteExpression exp : expressions){
			List<HAPResourceId> expressionDependency = HAPExpressionUtility.discoverResources(exp);
			dependencyResourceIds.addAll(expressionDependency);
		}
		
		//resource need by tag
		Set<String> tagNames = new HashSet<String>();
		discoverUITagsInUIUnit(uiResource, tagNames);
		for(String tagName : tagNames)		dependencyResourceIds.add(new HAPResourceIdUITag(new HAPUITagId(tagName)));
		
		Iterator<HAPResourceId> it = dependencyResourceIds.iterator();
		while(it.hasNext()){
			HAPResourceId resourceId = it.next();
			uiResource.getResourceDependency().add(new HAPResourceDependent(resourceId));
		}
	}
	
	private static void discoverUITagsInUIUnit(HAPExecutableUIUnit exeUIUnit, Set<String> names){
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(exeUIUnit.getType())){
			names.add(((HAPDefinitionUIUnitTag)exeUIUnit.getUIUnitDefinition()).getTagName());
		}
		for(HAPExecutableUIUnitTag childUiUnit : exeUIUnit.getUITags()){
			discoverUITagsInUIUnit(childUiUnit, names);
		}
	}
	
	private static List<HAPExecuteExpression> discoverExpressionsInUIResource(HAPExecutableUIUnitResource exeUIResource){
		List<HAPExecuteExpression> out = new ArrayList<HAPExecuteExpression>();
		discoverExpressionsInUIResourceUnit(exeUIResource, out);		
		return out;
	}
	
	private static void discoverExpressionsInUIResourceUnit(HAPExecutableUIUnit exeUIUnit, List<HAPExecuteExpression> out){
		out.addAll(getExpressions(exeUIUnit));
		
		Iterator<HAPExecutableUIUnitTag> it = exeUIUnit.getUITags().iterator();
		while(it.hasNext()){
			discoverExpressionsInUIResourceUnit(it.next(), out);
		}
	}
	
	private static Set<HAPExecuteExpression> getExpressions(HAPExecutableUIUnit uiExeUnit){
		Set<HAPExecuteExpression> all = new HashSet<HAPExecuteExpression>();
		for(HAPUIEmbededScriptExpressionInContent scriptExpressionInContent : uiExeUnit.getScriptExpressionsInContent())		all.addAll(getExpressions(scriptExpressionInContent));
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiExeUnit.getScriptExpressionsInAttribute())    all.addAll(getExpressions(scriptExpressionInAttribute));
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiExeUnit.getScriptExpressionsInTagAttribute())		all.addAll(getExpressions(scriptExpressionInAttribute));
		return all;
	}
	
	private static Set<HAPExecuteExpression> getExpressions(HAPEmbededScriptExpression embededScriptExpression){
		Set<HAPExecuteExpression> out = new HashSet<HAPExecuteExpression>();
		for(HAPScriptExpression scriptExpression : embededScriptExpression.getScriptExpressionsList()){
			out.addAll(scriptExpression.getExpressions().values());
		}
		return out;
	}
	
}
