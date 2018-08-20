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
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.resource.HAPResourceIdUITag;
import com.nosliw.uiresource.tag.HAPUITagId;

public class HAPResourceDependencyProcessor {

	public static void process(HAPDefinitionUIUnitResource uiResource, HAPResourceManagerRoot resourceMan){
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
	
	private static void discoverUITagsInUIUnit(HAPDefinitionUIUnit uiUnit, Set<String> names){
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(uiUnit.getType())){
			names.add(((HAPDefinitionUIUnitTag)uiUnit).getTagName());
		}
		for(HAPDefinitionUIUnitTag childUiUnit : uiUnit.getUITags()){
			discoverUITagsInUIUnit(childUiUnit, names);
		}
	}
	
	private static List<HAPExecuteExpression> discoverExpressionsInUIResource(HAPDefinitionUIUnitResource uiResource){
		List<HAPExecuteExpression> out = new ArrayList<HAPExecuteExpression>();
		discoverExpressionsInUIResourceUnit(uiResource, out);		
		return out;
	}
	
	private static void discoverExpressionsInUIResourceUnit(HAPDefinitionUIUnit uiResourceUnit, List<HAPExecuteExpression> out){
		out.addAll(getExpressions(uiResourceUnit));
		
		Iterator<HAPDefinitionUIUnitTag> it = uiResourceUnit.getUITags().iterator();
		while(it.hasNext()){
			discoverExpressionsInUIResourceUnit(it.next(), out);
		}
	}
	
	private static Set<HAPExecuteExpression> getExpressions(HAPDefinitionUIUnit uiDefinitionUnit){
		Set<HAPExecuteExpression> all = new HashSet<HAPExecuteExpression>();
		for(HAPUIEmbededScriptExpressionInContent scriptExpressionInContent : uiDefinitionUnit.getScriptExpressionsInContent())		all.addAll(getExpressions(scriptExpressionInContent));
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInAttributes())    all.addAll(getExpressions(scriptExpressionInAttribute));
		for(HAPUIEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInTagAttributes())		all.addAll(getExpressions(scriptExpressionInAttribute));
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
